using System.Text.Json;
using Xadrez.Jogo;
using XadrezServer.Requests;
using XadrezServer.Util;
using XadrezServer.Xadrez;

namespace XadrezServer
{
    static class Router
    {
        public static string HandleAction(string action, string content)
        {
            Console.WriteLine(action);
            switch (action)
            {
                // Matchmaking 
                case "ACTION=CriarPartida":
                    return CriarPartida();

                case "ACTION=EntrarPartida":
                    return EntrarPartida(content);

                // Jogo mesmo
                case "ACTION=MovimentosPossiveis":
                    return MovimentosPossiveis(content);

                case "ACTION=RealizaJogada":
                    return RealizaJogada(content);


                case "ACTION=PegarEstatisticas":
                    return "";

                default:
                    var response = new { Status = "NOK", Message = "BAD REQUEST" };
                    return JsonSerializer.Serialize(response);
            }
        }

        private static string CriarPartida()
        {
            // Cria uma nova partida e coloca no banco de dados
            PartidaXadrez match = new PartidaXadrez(Utils.CreateId());
            Globals.Partidas.Add(match);

            var response = new { Status = "OK", MatchId = match.Id, Player = "Branca" };

            return JsonSerializer.Serialize(response);
        }

        private static string EntrarPartida(string content)
        {
            EnterGame? args = JsonSerializer.Deserialize<EnterGame>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);

            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            return JsonSerializer.Serialize(new { Status = "OK", MatchId = match.Id, Player = "Preta" });
        }

        private static string MovimentosPossiveis(string content)
        {
            Movimento? args = JsonSerializer.Deserialize<Movimento>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);

            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            Posicao origem = args.Posicao();

            try
            {
                match.ValidarPosicaoDeOrigem(origem);
                bool[,] posicoes = match.Tab.Peca(origem).MovimentosPossiveis();

                return JsonSerializer.Serialize(new { Status = "OK", Posicoes = posicoes });
            }
            catch (TabuleiroException e)
            {
                return JsonSerializer.Serialize(new { Status = "NOK", Message = e.Message });
            }
        }

        private static string RealizaJogada(string content)
        {
            Jogada? args = JsonSerializer.Deserialize<Jogada>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);

            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            try
            {
                match.ValidarPosicaoDeDestino(args.PosicaoOrigem(), args.PosicaoDestino());

                match.RealizaJogada(args.PosicaoOrigem(), args.PosicaoDestino());

                string [,] tabuleiro = match.Tabuleiro();

                if(match.Terminada)
                    return JsonSerializer.Serialize(new { Status = "OK", Terminada = true, 
                        Message = $"{match.JogadorAtual} ganhou a partida!", Tabuleiro = match.Tabuleiro()});

                return JsonSerializer.Serialize(new { Message = "TODO",  Tabuleiro = match.Tabuleiro()});
            }
            catch (TabuleiroException e)
            {
                return JsonSerializer.Serialize(new { Status = "NOK", Message = e.Message });
            }
        }

        private static PartidaXadrez FindMatch(string? id)
        {
            return Globals.Partidas.Single(s => s.Id == id);
        }
    }
}