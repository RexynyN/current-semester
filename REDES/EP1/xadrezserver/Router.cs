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

                case "ACTION=EsperarTurno":
                    return EsperarTurno(content);

                case "ACTION=Terminada":
                    return Terminada(content);

                case "ACTION=PegarEstatisticas":
                    return "";

                default:
                    var response = new { Status = "NOK", Message = "BAD REQUEST" };
                    return JsonSerializer.Serialize(response);
            }
        }

        private static string Terminada(string content)
        {
            BusyWait? args = JsonSerializer.Deserialize<BusyWait>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            if(match.Terminada)
                return JsonSerializer.Serialize(new { Status = "OK", Message = "OK"});

            return JsonSerializer.Serialize(new { Status = "OK", Message = "NOK"});
        }

        private static string CriarPartida()
        {
            // Cria uma nova partida e coloca no banco de dados
            PartidaXadrez match = new PartidaXadrez(Utils.CreateId());
            Globals.Partidas.Add(match);

            var response = new { Status = "OK", MatchId = match.Id, Player = "Branca", Tabuleiro = match.Tabuleiro(),
                                                    Xeque = match.Xeque, Turno = match.Turno,
                                                    JogadorAtual = match.JogadorAtual.ToString(),
                                                    PegasPretas = match.PecasPegas(Cor.Preta),
                                                    PegasBrancas = match.PecasPegas(Cor.Branca)};

            return JsonSerializer.Serialize(response);
        }

        private static string EntrarPartida(string content)
        {
            EnterGame? args = JsonSerializer.Deserialize<EnterGame>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);

            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            var response = new { Status = "OK", MatchId = match.Id, Player = "Preta", Tabuleiro = match.Tabuleiro(),
                                        Xeque = match.Xeque, Turno = match.Turno,
                                        JogadorAtual = match.JogadorAtual.ToString(),
                                        PegasPretas = match.PecasPegas(Cor.Preta),
                                        PegasBrancas = match.PecasPegas(Cor.Branca)};

            return JsonSerializer.Serialize(response);
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
                System.Console.WriteLine(match.JogadorAtual);
                match.ValidarPosicaoDeOrigem(origem);
                bool[,] posicoes = match.Tab.Peca(origem).MovimentosPossiveis();

                bool [][] possiveis = ToJaggedArray(posicoes);

                return JsonSerializer.Serialize(new { Status = "OK", Posicoes = possiveis });
            }
            catch (TabuleiroException e)
            {
                return JsonSerializer.Serialize(new { Status = "NOK", Message = e.Message });
            }
        }

        private static bool[][] ToJaggedArray(bool[,] posicoes)
        {
            bool [] [] possiveis = new bool [8][];
            for (int i = 0; i < 8; i++)
            {
                bool [] aux = new bool [8];
                for (int j = 0; j < 8; j++)
                {
                    aux[j] = posicoes[i,j];
                }
                possiveis[i] = aux;
            }

            return possiveis;
        }

        private static string RealizaJogada(string content)
        {
            Move? args = JsonSerializer.Deserialize<Move>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });


            System.Console.WriteLine(args.Player);
            if(args?.Cor() != match.JogadorAtual)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Não é a sua vez!" });

            try
            {
                match.ValidarPosicaoDeDestino(args.PosicaoOrigem(), args.PosicaoDestino());

                match.RealizaJogada(args.PosicaoOrigem(), args.PosicaoDestino());

                string [][] tabuleiro = match.Tabuleiro();

                if(match.Terminada)
                    return JsonSerializer.Serialize(new { Status = "OK", Terminada = true, 
                        Message = $"{match.JogadorAtual} ganhou a partida!", Tabuleiro = match.Tabuleiro()});

                return JsonSerializer.Serialize(new { Message = "TODO",  Tabuleiro = match.Tabuleiro(), 
                                                    Xeque = match.Xeque, Turno = match.Turno,
                                                    JogadorAtual = match.JogadorAtual.ToString(),
                                                    PegasPretas = match.PecasPegas(Cor.Preta),
                                                    PegasBrancas = match.PecasPegas(Cor.Branca)});
            }
            catch (TabuleiroException e)
            {
                return JsonSerializer.Serialize(new { Status = "NOK", Message = e.Message });
            }
        }

        private static string EsperarTurno(string content)
        {
            BusyWait? args = JsonSerializer.Deserialize<BusyWait>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            if(match.JogadorAtual.ToString() != args.Player)
                return JsonSerializer.Serialize(new { Status = "OK", Message = "NOK",
                                            JogadorAtual = match.JogadorAtual.ToString() });

            return JsonSerializer.Serialize(new { Status = "OK", Message = "OK",
                                            JogadorAtual = match.JogadorAtual.ToString()});
        }

        private static PartidaXadrez FindMatch(string? id)
        {
            try{
                return Globals.Partidas.Single(s => s.Id == id);
            }
            catch(InvalidOperationException)
            {
                return null;
            }
        }
    }
}