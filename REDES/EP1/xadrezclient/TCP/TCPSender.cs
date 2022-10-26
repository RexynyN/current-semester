using System.Net.Sockets;
using System.Text.Json;

namespace XadrezClient.TCP
{
    class TCPSender
    {
        public string Server { get; set; }
        public Int32 Port { get; set; }

        public TCPSender()
        {
            Server = "127.0.0.1";
            Port = 13000;
        }

        public TCPSender(string server, int port)
        {
            Server = server;
            Port = port;
        }

        public string SendRequest(string package)
        {
            try
            {
                using TcpClient client = new TcpClient(Server, Port);

                Byte[] data = System.Text.Encoding.UTF8.GetBytes(package);

                NetworkStream stream = client.GetStream();

                stream.Write(data, 0, data.Length);

                Console.WriteLine("Sent: {0}", package);

                data = new Byte[256];

                String responseData = String.Empty;

                Int32 bytes = stream.Read(data, 0, data.Length);
                responseData = System.Text.Encoding.UTF8.GetString(data, 0, bytes);
                Console.WriteLine("Received: {0}", responseData);

                return responseData;
            }
            catch (ArgumentNullException e)
            {
                Console.WriteLine("ArgumentNullException: {0}", e);
                return "";
            }
            catch (SocketException e)
            {
                Console.WriteLine("SocketException: {0}", e);
                return "";
            }
        }
    }
}


