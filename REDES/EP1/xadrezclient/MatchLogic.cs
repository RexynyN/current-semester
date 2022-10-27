 using XadrezClient.TCP;
using XadrezClient.Response;
using System.Text.Json;

namespace XadrezClient
{
    class MatchLogic
    {
        public string MatchId { get; private set; }
        public string Player { get; private set; }
        public string[,] Tabuleiro { get; private set; } 
        public TCPSender Connection { private get; private set; }

        public MatchLogic(string matchId, string player)
        {
            MatchId = matchId;
            Player = player;
            Connection = new TCPSender();
        }
        
        public bool[,] MovimentosPossiveis (Posicao pos)
        {

        }

        public void RealizaJogada(Posicao origem, Posicao destino)
        {
            throw new TabuleiroException("WAAAAAAAAAAA");
        }

        static public MatchLogic CriarPartida()
        {
            string request = Util.CreateRequest("CriarPartida", new { Message = "The cake is a lie" });

            TCPSender sender = new TCPSender();

            CreateMatch? response = JsonSerializer.Deserialize<CreateMatch>(sender.SendRequest(request));

            if(response.Status == "NOK")
                return null;

            MatchLogic puppy = new MatchLogic(response.MatchId, response.Player);

            return puppy;
        }

        static public MatchLogic EntrarPartida(string id)
        {
            string request = Util.CreateRequest("EntrarPartida", new { MatchId = id });

            TCPSender sender = new TCPSender();

            CreateMatch? response = JsonSerializer.Deserialize<CreateMatch>(sender.SendRequest(request));

            if(response.Status == "NOK")
                return null;

            MatchLogic puppy = new MatchLogic(response.MatchId, response.Player);

            return puppy;
        }
    }
}