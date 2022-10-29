using System.Net.Sockets;
using System.Text;
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

                data = new Byte[32768];

                String responseData = String.Empty;


                byte[] buffer = new byte[1024];
                StringBuilder message = new StringBuilder();
                int numberOfBytesRead = 0;

                do{
                    numberOfBytesRead = stream.Read(buffer, 0, buffer.Length);

                    message.AppendFormat("{0}", Encoding.ASCII.GetString(buffer, 0, numberOfBytesRead));
                }
                while(stream.DataAvailable);

                Console.WriteLine("Received: {0}",  message.ToString());


                return message.ToString();
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