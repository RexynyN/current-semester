using System.Text.Json;
using XadrezServer.Requests;
using XadrezServer.Util;
using XadrezServer.Xadrez;

namespace XadrezServer
{
    static class Router
    {
        public static string HandleAction(string action, string content)
        {
            switch (action)
            {
                case "CriarPartida":
                    return CriarPartida();

                case "EntrarPartida":
                    return EntrarPartida(content);

                case "ValidarPosicaoDeOrigem":
                    return "";

                case "MovimentosPossiveis":
                    return "";

                case "ValidarPosicaoDeDestino":
                    return "";

                case "RealizaJogada":
                    return "";

                case "PegarEstatisticas":
                    return "";

                default:
                    var response = new { Status = "NOK", Message = "BAD REQUEST" };
                    return JsonSerializer.Serialize(response);
            }
        }

        public static string CriarPartida()
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

            if(match == null)
                return JsonSerializer.Serialize(new { Status = "NOK", Message = "Partida não encontrada!"});

            return JsonSerializer.Serialize(new { Status = "OK", MatchId = match.Id, Player = "Preta" });
        }

        private static PartidaXadrez FindMatch(string? id)
        {
            return Globals.Partidas.Single(s => s.Id == id);
        }
    }
}