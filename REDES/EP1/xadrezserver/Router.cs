using System.Text.Json;
using XadrezServer.Util;
using XadrezServer.Xadrez;

namespace XadrezServer
{
    static class Router
    {
        public static string HandleAction(string action, string content)
        {
            switch(action)
            {
                case "CriarPartida":
                    PartidaXadrez part = new PartidaXadrez(Utils.CreateId());
                    Globals.Partidas.Add(part);
                    var response = new { Status = "OK", MatchId = part.Id, Player = "Branca" };
                    return JsonSerializer.Serialize(response);
                    
                case "EntrarPartida":
                    
                    return "";

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
                    var responser = new { Status = "NOK", Message = "BAD REQUEST"};
                    return JsonSerializer.Serialize(responser);            
            }
        }
    }
}