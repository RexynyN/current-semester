using Xadrez.Xadrez;
using Xadrez.Jogo;

namespace Xadrez
{
    class Program
    {
        static void Main(string[] args)
        {
            Menu();
        }

        static void Menu
        ()
        {
            Console.Clear();

            int escolha = 0;
            while(true)
            {
                try
                {
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

            if(escolha == 1)
            {
                Logic.CriarPartida();
                Xadrez();
            }
            else if(escolha == 2)
            {
                Console.Clear();
                EntrarPartida();
            }
        }

        static EntrarPartida()
        {
            string cod = "";
            while(true)
            {
                Console.Write("Digite o código de Jogo: ")
                cod = Console.ReadLine();

                if(Logic.EntrarPartida(cod))
                    break;
            }
        }

        static void Xadrez ()
        {
            try
            {
                // Loop principal do Jogo
                PartidaXadrez partida = new PartidaXadrez();
                
                // pseudo
                string MatchId;
                Cor Player;
                
                while (!partida.Terminada)
                {
                    try
                    {
                        Console.Clear();
                        Tela.ImprimirPartida(partida);
                        Console.WriteLine();
                        Console.Write("Origem: ");
                        Posicao origem = Tela.LerPosicaoXadrez().ToPosicao();
                        partida.ValidarPosicaoDeOrigem(origem);


                        bool[,] posicoes = partida.Tab.Peca(origem).MovimentosPossiveis();
                        Console.Clear();
                        Tela.ImprimirTabuleiro(partida.Tab, posicoes);

                        Console.WriteLine();
                        Console.Write("Destino: ");
                        Posicao destino = Tela.LerPosicaoXadrez().ToPosicao();
                        partida.ValidarPosicaoDeDestino(origem, destino);

                        partida.RealizaJogada(origem, destino);
                        connection.AwaitTurn(); // PseudoCódigo para implementar uma espera ocupada até o próximo turno
                    }
                    catch(TabuleiroException e)
                    {
                        Console.WriteLine(e.Message);
                        Console.ReadKey();
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e.Message);
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