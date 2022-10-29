using System.Text.Json;
using Xadrez.Jogo;
using XadrezClient.Response;
using XadrezClient.TCP;

namespace XadrezClient
{
    class MatchLogic
    {
        public string MatchId { get; private set; }
        public string Player { get; private set; }
        public string[,] Tabuleiro { get; private set; }
        public int Turno { get; private set; }
        public string JogadorAtual { get; private set; }
        public bool Xeque { get; private set; }
        public string PegasBrancas { get; private set; }
        public string PegasPretas { get; private set; }

        private TCPSender Connection;

        public MatchLogic(Partida args)
        {
            MatchId = args.MatchId;
            Player = args.Player;
            Tabuleiro = args.MatrizTabuleiro();
            Turno = args.Turno;
            JogadorAtual = args.JogadorAtual;
            Xeque = args.Xeque;
            PegasBrancas = args.PegasBrancas;
            PegasPretas = args.PegasPretas;
            Connection = new TCPSender();
        }

        public bool Terminada() // 
        {
            string request = Util.CreateRequest("Terminada", new
            {
                MatchId = MatchId,
            });

            BaseResponse? response = JsonSerializer.Deserialize<BaseResponse>(Connection.SendRequest(request));

            return response?.Message == "OK";
        }

        public bool[,] MovimentosPossiveis(Posicao origem)
        {
            string request = Util.CreateRequest("MovimentosPossiveis", new
            {
                MatchId = MatchId,
                Player = Player,
                LinhaOrigem = origem.Linha,
                ColunaOrigem = origem.Coluna,
            });

            MovimentosPossiveis? response = JsonSerializer.Deserialize<MovimentosPossiveis>(Connection.SendRequest(request));

            if (response.Status == "NOK")
                throw new TabuleiroException(response.Message);

            System.Console.WriteLine(response.Posicoes);
            return response.MatrizPossiveis();
        }

        public void RealizaJogada(Posicao origem, Posicao destino)
        {
            string request = Util.CreateRequest("RealizaJogada", new
            {
                MatchId = MatchId,
                Player = Player,
                LinhaOrigem = origem.Linha,
                ColunaOrigem = origem.Coluna,
                LinhaDestino = destino.Linha,
                ColunaDestino = destino.Coluna,
            });

            Move? response = JsonSerializer.Deserialize<Move>(Connection.SendRequest(request));

            if (response.Status == "NOK")
                throw new TabuleiroException(response.Message);

            Tabuleiro = response?.MatrizTabuleiro();
            Turno = response.Turno;
            JogadorAtual = response.JogadorAtual;
            Xeque = response.Xeque;
            PegasBrancas = response.PegasBrancas;
            PegasPretas = response.PegasPretas;
        }

        public void EsperarTurno()
        {
            while (true)
            {
                Thread.Sleep(500);

                string request = Util.CreateRequest("EsperarTurno", new
                {
                    MatchId = MatchId,
                    Player = Player
                });

                BusyWait? busy = JsonSerializer.Deserialize<BusyWait>(Connection.SendRequest(request));

                if (busy.Message == "OK") // Pseudo, precisa saber o que retorna quando o server dá pau
                    break;
            }
        }

        static public MatchLogic CriarPartida()
        {
            string request = Util.CreateRequest("CriarPartida", new { Message = "The cake is a lie" });

            TCPSender sender = new TCPSender();

            Partida? response = JsonSerializer.Deserialize<Partida>(sender.SendRequest(request));

            if (response.Status == "NOK")
                return null;

            MatchLogic puppy = new MatchLogic(response);

            return puppy;
        }

        static public MatchLogic EntrarPartida(string id)
        {
            string request = Util.CreateRequest("EntrarPartida", new { MatchId = id });

            TCPSender sender = new TCPSender();

            Partida? response = JsonSerializer.Deserialize<Partida>(sender.SendRequest(request));

            if (response.Status == "NOK")
                return null;

            MatchLogic puppy = new MatchLogic(response);

            return puppy;
        }
    }
}