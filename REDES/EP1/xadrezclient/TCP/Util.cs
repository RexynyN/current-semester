using System.Text.Json;

namespace XadrezClient.TCP
{
    static class Util
    {
        public static string CreateRequest(string action, object content)
        {
            string data = $"ACTION={content}$";
            data += JsonSerializer.Serialize(content);

            return data;
        }

        public static bool BusyWait(string action, object content)
        {
            while(true)
            {
                Thread.Sleep(1000);

                BusyWait busy = JsonSerializer.Deserialize<BusyWait>(TCP.SendRequest(
                    CreateRequest(action, content)));
                
                if (busy.Seggs) // Pseudo, precisa saber o que retorna quando o server dá pau
                    break;
            }
        }
    }
}