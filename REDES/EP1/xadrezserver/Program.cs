using System.Net;
using System.Net.Sockets;
using System.Text.Json;

class MyTcpListener
{
    public class Partida
    {
        public DateTimeOffset Date { get; set; }
        public int TemperatureCelsius { get; set; }
        public string? Summary { get; set; }
    }

    public static void Main()
    {
        TcpListener? server = null;
        List<Partida> matches = new List<Partida>();
        
        try
        {
            // Set the TcpListener on port 13000.
            Int32 port = 13000;
            IPAddress localAddr = IPAddress.Parse("127.0.0.1");

            // TcpListener server = new TcpListener(port);
            server = new TcpListener(localAddr, port);

            // Start listening for client requests.
            server.Start();

            // Buffer for reading data
            Byte[] bytes = new Byte[256];
            String? data = null;

            // Enter the listening loop.
            while (true)
            {
                Console.Write("Waiting for a connection... ");

                // Perform a blocking call to accept requests.
                // You could also use server.AcceptSocket() here.
                TcpClient client = server.AcceptTcpClient();
                Console.WriteLine("Connected!");
                data = null;

                // Get a stream object for reading and writing
                NetworkStream stream = client.GetStream();
                int i;

                // Loop to receive all the data sent by the client.
                while ((i = stream.Read(bytes, 0, bytes.Length)) != 0)
                {
                    // Translate data bytes to a ASCII string.
                    data = System.Text.Encoding.ASCII.GetString(bytes, 0, i);
                    Console.WriteLine("Received: {0}", data);


                    Partida? obj =
                    JsonSerializer.Deserialize<Partida>(data);

                    Console.WriteLine($"Date: {obj?.Date}");
                    Console.WriteLine($"TemperatureCelsius: {obj?.TemperatureCelsius}");
                    Console.WriteLine($"Summary: {obj?.Summary}");

                    obj.TemperatureCelsius = 30;
                    obj.Date = DateTime.Parse("2022-04-16");
                    obj.Summary = "Extra Hot";

                    string message = JsonSerializer.Serialize(obj);


                    // Process the data sent by the client.
                    // data = data.ToUpper();

                    byte[] msg = System.Text.Encoding.ASCII.GetBytes(message);

                    // Send back a response.
                    stream.Write(msg, 0, msg.Length);
                    Console.WriteLine("Sent: {0}", message);

                }

                // Shutdown and end connection
                client.Close();
            }
        }
        catch (SocketException e)
        {
            Console.WriteLine("SocketException: {0}", e);
        }
        finally
        {
            // Stop listening for new clients.
            server?.Stop();
        }

        Console.WriteLine("\nHit enter to continue...");
        Console.Read();
    }

    // Pega o campo de action, para fazer o chaveamento para sua função
    public static void HandleAction(String message)
    {

    }
}