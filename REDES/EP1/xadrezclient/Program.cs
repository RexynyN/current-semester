using Xadrez.Xadrez;
using Xadrez.Jogo;

namespace Xadrez
{
    class Program
    {
        static void Main(string[] args)
        {
            StartMenu(false);
        }

        static void StartMenu(bool error)
        {
            Console.Clear();

            int escolha = 0;
            while(true)
            {
                try
                {
                    if (error)
                        Printer.Error("Houve um erro ao jogar, tente novamente.");

                    Console.WriteLine("SUPER XADREZ MULTIPLAYER DA 04 EACH:");
                    Console.WriteLine("1 - Criar uma partida");
                    Console.WriteLine("2 - Entrar em uma partida");
                    escolha = int.Parse(Console.ReadLine());

                    if(escolha > 0 || escolha <= 2)
                        break;
                    else 
                        Console.WriteLine("Opção inválida! Clique ENTER para tentar novamente!");

                }
                catch(Exception e)
                {
                    Console.WriteLine("Opção inválida! Clique ENTER para tentar novamente!");
                }

                Console.ReadKey();
            }

            try{
                if(escolha == 1)
                {
                    MatchLogic match = MatchLogic.CriarPartida();
                    
                    if(match != null)
                        Xadrez(match);
                }
                else if(escolha == 2)
                {
                    Console.Clear();
                    EntrarPartida();
                }
            }
            catch(Exception e)
            {
                Console.WriteLine("Erro: " + e.Message);
                Console.WriteLine("Pressione enter para voltar ao menu principal.")
                Console.ReadKey();
                StartMenu(true);
            }
        }

        public static void  EntrarPartida()
        {
            string cod = "";
            while(true)
            {
                Console.Write("Digite o código de Jogo: ")
                cod = Console.ReadLine();

                MatchLogic match = MatchLogic.EntrarPartida(cod)
                if(match != null)
                {
                    Xadrez(match);
                    break;
                }

                Console.Clear();
                Printer.Error("O código não foi encontrado, tente novamente!");
            }
        }

        public static void Xadrez (MatchLogic partida)
        {
            try
            {
                while (!partida.Terminada)
                {
                    try
                    {
                        Console.Clear();
                        Tela.ImprimirPartida(partida);
                        Console.WriteLine();
                        Console.WriteLine("Sua vez!");
                        Console.Write("Origem: ");
                        Posicao origem = Tela.LerPosicaoXadrez().ToPosicao();
                        
                        bool[,] posicoes = partida.MovimentosPossiveis(origem);
                        Console.Clear();
                        Tela.ImprimirTabuleiro(partida.Tab, posicoes);

                        Console.WriteLine();
                        Console.WriteLine("Sua vez!");
                        Console.Write("Destino: ");
                        Posicao destino = Tela.LerPosicaoXadrez().ToPosicao();

                        partida.RealizaJogada(origem, destino);
                        Tela.ImprimirTabuleiro(partida);
                        Console.WriteLine("Esperando o turno do oponente!");
                        partida.EsperarTurno(); // PseudoCódigo para implementar uma espera ocupada até o próximo turno
                    }
                    catch(TabuleiroException e )
                    {
                        Console.WriteLine(e.Message);
                        Console.ReadKey();
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine("Houve um erro na partida\nMensagem:" + e.Message);
                        Console.ReadKey();
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        }
}