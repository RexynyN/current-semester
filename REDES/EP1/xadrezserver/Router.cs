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
            // Console.WriteLine(action);
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

                // Controle
                case "ACTION=EsperarTurno":
                    return EsperarTurno(content);

                case "ACTION=Terminada":
                    return Terminada(content);

                // Chat
                case "ACTION=PostarMensagem":
                    return PostarMensagem(content);

                case "ACTION=PegarMensagem":
                    return PegarMensagem(content);

                // Estatisticas
                case "ACTION=PegarEstatisticas":
                    return PegarEstatisticas(content);

                default:
                    var response = new { Status = "NOK", Message = "BAD REQUEST" };
                    return JsonSerializer.Serialize(response);
            }
        }

        private static string PostarMensagem(string content)
        {
            Messagen? args = JsonSerializer.Deserialize<Messagen>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            match.Chat.SetMessage(args.Player, args.Message);

            return JsonSerializer.Serialize(new { Status = "OK", Message = "OK" });
        }

        private static string PegarMensagem(string content)
        {
            Partida? args = JsonSerializer.Deserialize<Partida>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            string[] messages = match.Chat.GetMessages(args.Player);

            if (messages.Length <= 0)
                return JsonSerializer.Serialize(new { Status = "OK", Message = "NOK" });

            return JsonSerializer.Serialize(new { Status = "OK", Message = "OK", Content = messages });
        }

        private static string PegarEstatisticas(string content)
        {
            Partida? args = JsonSerializer.Deserialize<Partida>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            if (!match.Terminada)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "A partida não acabou!" });

            string[] stats = match.Estatisticas(args.Player).GerarEstatisticas();

            return JsonSerializer.Serialize(new { Status = "OK", Message = "OK", Estatisticas = stats });
        }

        private static string Terminada(string content)
        {
            Partida? args = JsonSerializer.Deserialize<Partida>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            if (match.Terminada)
                return JsonSerializer.Serialize(new { Status = "OK", Message = "OK", Vencedor = match.Vencedor });

            return JsonSerializer.Serialize(new { Status = "OK", Message = "NOK", Vencedor = "" });
        }

        private static string CriarPartida()
        {
            // Cria uma nova partida e coloca no banco de dados
            PartidaXadrez match = new PartidaXadrez(Utils.CreateId());
            Globals.Partidas.Add(match);

            var response = new
            {
                Status = "OK",
                MatchId = match.Id,
                Player = "Branca",
                Tabuleiro = match.Tabuleiro(),
                Xeque = match.Xeque,
                Turno = match.Turno,
                JogadorAtual = match.JogadorAtual.ToString(),
                PegasPretas = match.PecasPegas(Cor.Preta),
                PegasBrancas = match.PecasPegas(Cor.Branca)
            };

            System.Console.WriteLine("Partida criada! ID: " + match.Id);
            return JsonSerializer.Serialize(response);
        }

        private static string EntrarPartida(string content)
        {
            EnterGame? args = JsonSerializer.Deserialize<EnterGame>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);

            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            var response = new
            {
                Status = "OK",
                MatchId = match.Id,
                Player = "Preta",
                Tabuleiro = match.Tabuleiro(),
                Xeque = match.Xeque,
                Turno = match.Turno,
                JogadorAtual = match.JogadorAtual.ToString(),
                PegasPretas = match.PecasPegas(Cor.Preta),
                PegasBrancas = match.PecasPegas(Cor.Branca)
            };

            System.Console.WriteLine("Jogador entrou na partida de ID " + match.Id);
            return JsonSerializer.Serialize(response);
        }

        private static string MovimentosPossiveis(string content)
        {
            Movimento? args = JsonSerializer.Deserialize<Movimento>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);

            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            if (args.Player != match.JogadorAtual.ToString())
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Não é sua vez!" });

            Posicao origem = args.Posicao();

            try
            {
                match.ValidarPosicaoDeOrigem(origem);
                bool[,] posicoes = match.Tab.Peca(origem).MovimentosPossiveis();

                bool[][] possiveis = ToJaggedArray(posicoes);

                return JsonSerializer.Serialize(new { Status = "OK", Posicoes = possiveis });
            }
            catch (TabuleiroException e)
            {
                return JsonSerializer.Serialize(new { Status = "NOK", Message = e.Message });
            }
        }

        private static bool[][] ToJaggedArray(bool[,] posicoes)
        {
            bool[][] possiveis = new bool[8][];
            for (int i = 0; i < 8; i++)
            {
                bool[] aux = new bool[8];
                for (int j = 0; j < 8; j++)
                {
                    aux[j] = posicoes[i, j];
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

            if (args?.Player != match.JogadorAtual.ToString())
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Não é a sua vez!" });

            try
            {
                match.ValidarPosicaoDeDestino(args.PosicaoOrigem(), args.PosicaoDestino());

                match.RealizaJogada(args.PosicaoOrigem(), args.PosicaoDestino());

                string[][] tabuleiro = match.Tabuleiro();

                if (match.Terminada)
                    return JsonSerializer.Serialize(new
                    {
                        Status = "OK",
                        Terminada = true,
                        Message = $"{match.JogadorAtual} ganhou a partida!",
                        Tabuleiro = match.Tabuleiro()
                    });

                return JsonSerializer.Serialize(new
                {
                    Status = "OK",
                    Message = "TODO",
                    Tabuleiro = match.Tabuleiro(),
                    Xeque = match.Xeque,
                    Turno = match.Turno,
                    JogadorAtual = match.JogadorAtual.ToString(),
                    PegasPretas = match.PecasPegas(Cor.Preta),
                    PegasBrancas = match.PecasPegas(Cor.Branca)
                });
            }
            catch (TabuleiroException e)
            {
                return JsonSerializer.Serialize(new { Status = "NOK", Message = e.Message });
            }
        }

        private static string EsperarTurno(string content)
        {
            Partida? args = JsonSerializer.Deserialize<Partida>(content);

            PartidaXadrez match = FindMatch(args?.MatchId);
            if (match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!" });

            if (match.Terminada)
                return JsonSerializer.Serialize(new
                {
                    Status = "OK",
                    Message = "OK",
                    Tabuleiro = match.Tabuleiro(),
                    JogadorAtual = match.JogadorAtual.ToString()
                });

            if (match.JogadorAtual.ToString() != args.Player)
                return JsonSerializer.Serialize(new
                {
                    Status = "OK",
                    Message = "NOK",
                    JogadorAtual = match.JogadorAtual.ToString()
                });

            return JsonSerializer.Serialize(new
            {
                Status = "OK",
                Message = "OK",
                Tabuleiro = match.Tabuleiro(),
                Xeque = match.Xeque,
                Turno = match.Turno,
                JogadorAtual = match.JogadorAtual.ToString(),
                PegasPretas = match.PecasPegas(Cor.Preta),
                PegasBrancas = match.PecasPegas(Cor.Branca)
            });
        }

        private static PartidaXadrez FindMatch(string? id)
        {
            try
            {
                return Globals.Partidas.Single(s => s.Id == id);
            }
            catch (InvalidOperationException)
            {
                return null;
            }
        }
    }
}