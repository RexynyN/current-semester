using System.Net.Sockets;
using System.Text.Json;

namespace XadrezClient
{
    public class WeatherForecast
    {
        public DateTimeOffset Date { get; set; }
        public int TemperatureCelsius { get; set; }
        public string? Summary { get; set; }
    }

    class Sender
    {
        static void Main(String [] args)
        {

            // string action = "ACTION=CriarPartida${}";
            string action = "ACTION=EntrarPartida${ \"MatchId\" = \"W9CFFS\"}";

            string message = JsonSerializer.Serialize(action);

            Console.WriteLine(message);
            
            String server = "127.0.0.1";
            
            // Create a TcpClient.
            // Note, for this client to work you need to have a TcpServer
            // connected to the same address as specified by the server, port
            // combination.
            Int32 port = 13000;
            try
            {
                // Prefer using declaration to ensure the instance is Disposed later.
                using TcpClient client = new TcpClient(server, port);

                // Translate the passed message into ASCII and store it as a Byte array.
                Byte[] data = System.Text.Encoding.UTF8.GetBytes(message);
                // Byte[] data = System.Text.Encoding.ASCII.GetBytes("ACTION=");


                // Get a client stream for reading and writing.
                NetworkStream stream = client.GetStream();

                // Send the message to the connected TcpServer.
                stream.Write(data, 0, data.Length);

                Console.WriteLine("Sent: {0}", message);

                // Buffer to store the response bytes.
                data = new Byte[256];

                // String to store the response ASCII representation.
                String responseData = String.Empty;

                // Read the first batch of the TcpServer response bytes.
                Int32 bytes = stream.Read(data, 0, data.Length);
                responseData = System.Text.Encoding.UTF8.GetString(data, 0, bytes);
                Console.WriteLine("Received: {0}", responseData);
            }
            catch (ArgumentNullException e)
            {
                Console.WriteLine("ArgumentNullException: {0}", e);
            }
            catch (SocketException e)
            {
                Console.WriteLine("SocketException: {0}", e);
            }

            Console.WriteLine("\n Press Enter to continue...");
            Console.Read();
        }
    }
}








// using Xadrez.Xadrez;
// using Xadrez.Jogo;


// namespace Xadrez
// {
//     class Program
//     {
//         static void Main(string[] args)
//         {
//             try
//             {
//                 // Loop principal do Jogo
//                 PartidaXadrez partida = new PartidaXadrez();
                
//                 // pseudo
//                 string MatchId;
//                 Cor Player;
                
//                 while (!partida.Terminada)
//                 {
//                     try
//                     {
//                         Console.Clear();
//                         Tela.ImprimirPartida(partida);
//                         Console.WriteLine();
//                         Console.Write("Origem: ");
//                         Posicao origem = Tela.LerPosicaoXadrez().ToPosicao();
//                         partida.ValidarPosicaoDeOrigem(origem);


//                         bool[,] posicoes = partida.Tab.Peca(origem).MovimentosPossiveis();
//                         Console.Clear();
//                         Tela.ImprimirTabuleiro(partida.Tab, posicoes);

//                         Console.WriteLine();
//                         Console.Write("Destino: ");
//                         Posicao destino = Tela.LerPosicaoXadrez().ToPosicao();
//                         partida.ValidarPosicaoDeDestino(origem, destino);

//                         partida.RealizaJogada(origem, destino);
//                         connection.AwaitTurn(); // PseudoCódigo para implementar uma espera ocupada até o próximo turno
//                     }
//                     catch(TabuleiroException e)
//                     {
//                         Console.WriteLine(e.Message);
//                         Console.ReadKey();
//                     }
//                     catch (Exception e)
//                     {
//                         Console.WriteLine(e.Message);
//                         Console.ReadKey();
//                     }
//                 }
//             }
//             catch (Exception e)
//             {
//                 Console.WriteLine(e.Message);
//             }
//         }
//     }
// }
