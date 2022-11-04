using Xadrez.Jogo;

namespace XadrezClient
{
    class Program
    {
        public static bool ThreadHandler = true;

        static void Main(string[] args)
        {
            StartMenu(false);
        }

        static void StartMenu(bool error)
        {

            int escolha = 0;
            while (true)
            {
                try
                {
                    Console.Clear();

                    if (error)
                        Printer.Error("Houve um erro ao jogar, tente novamente.\n");

                    Printer.Green("SUPER XADREZ MULTIPLAYER DA 04 EACH (EPIC EDITION):\n");
                    Console.WriteLine("1 - Criar uma partida");
                    Console.WriteLine("2 - Entrar em uma partida");
                    Console.WriteLine("3 - Sair do jogo");
                    Printer.BlackSingle("\nSua escolha: ");
                    escolha = int.Parse(Console.ReadLine());

                    if (escolha > 0 && escolha <= 3)
                        break;
                    else
                        Console.WriteLine("Opção inválida! Clique ENTER para tentar novamente!");

                }
                catch (Exception e)
                {
                    Console.WriteLine("Opção inválida! Clique ENTER para tentar novamente!");
                }

                Console.ReadKey();
            }

            try
            {
                if (escolha == 1)
                {
                    MatchLogic match = MatchLogic.CriarPartida();

                    if (match != null)
                        Xadrez(match);
                }
                else if (escolha == 2)
                {
                    Console.Clear();
                    EntrarPartida();
                }
            }
            catch (Exception e)
            {
                Printer.Error("Erro: " + e.Message);
                Printer.Error(e.StackTrace);
                Printer.Black("Pressione enter para voltar ao menu principal.");
                Console.ReadKey();
                StartMenu(true);
            }
        }

        public static void EntrarPartida()
        {
            string cod = "";
            while (true)
            {
                Console.Write("Digite o código de Jogo: ");
                cod = Console.ReadLine();

                MatchLogic match = MatchLogic.EntrarPartida(cod);
                if (match != null)
                {
                    Xadrez(match);
                    break;
                }

                Console.Clear();
                Printer.Error("O código não foi encontrado, tente novamente!");
            }
        }

        public static void Xadrez(MatchLogic partida)
        {
            while (!partida.Terminada())
            {
                try
                {
                    Console.Clear();
                    Tela.ImprimirPartida(partida);

                    if (partida.JogadorAtual == partida.Player)
                    {
                        Printer.Green("\nSua vez!");
                        Console.Write("Origem: ");
                        Posicao origem = Tela.LerPosicaoXadrez().ToPosicao();

                        bool[,] posicoes = partida.MovimentosPossiveis(origem);
                        Console.Clear();
                        Tela.ImprimirTabuleiro(partida.Tabuleiro, posicoes);

                        Console.WriteLine();
                        Console.Write("Destino: ");
                        Posicao destino = Tela.LerPosicaoXadrez().ToPosicao();

                        partida.RealizaJogada(origem, destino);
                        Console.Clear();
                        Tela.ImprimirPartida(partida);
                    }

                    if (!partida.Terminada())
                    {
                        Printer.Blue("\nEsperando o turno do oponente!");
                        partida.EsperarTurno();
                    }
                }
                catch (TabuleiroException e)
                {
                    Console.WriteLine(e.Message);
                    Console.ReadKey();
                }
                catch (Exception e)
                {
                    Console.WriteLine("Houve um erro na partida\nMensagem:" + e.Message);
                    Console.WriteLine("" + e.StackTrace);
                    Console.ReadKey();
                }
            }

            Console.Clear();
            Tela.ImprimirTabuleiro(partida.Tabuleiro);
            Console.WriteLine();
            if(partida.Vencedor != partida.Player)
                Printer.Error("Fim de Jogo! Dessa vez você não foi campeão :(");
            else 
                Printer.Green("Fim de Jogo! A vitória é sua! <|º_º|>");

            Console.WriteLine("Clique ENTER para Chat Pós-Jogo!");
            Console.ReadKey();

            Chat(partida);
        }

        public static void Chat (MatchLogic partida)
        {
            Console.Clear();
            Printer.Green("Chat Pós-Jogo (!sair/!exit para sair) ");
            
            string message = "";
            ThreadHandler = true;
            Thread mThread = new Thread(partida.MessageThread);
            mThread.Start();

            while(true)
            {
                message = Console.ReadLine().Trim();
                
                if(message == "!sair" || message == "!exit")
                    break;

                partida.MandarMensagem(message);
                Console.SetCursorPosition(0, Console.CursorTop-1);
                Printer.Black("Você: " + message);
            }

            ThreadHandler = false;
            partida.MandarMensagem("<Seu adversário saiu do chat>");

            Estatisticas(partida);
        }

        public static void Estatisticas (MatchLogic partida)
        {
            Console.Clear();
            Printer.Green("Estatíticas da Partida: " + partida.MatchId);
            Console.WriteLine();

            Tela.ImprimirEstatisticas(partida.Estatisticas());

            Console.WriteLine("\nPressione ENTER duas vezes para continuar.");
            Console.ReadKey();
            Console.ReadKey();
            StartMenu(false);
        }
    }

}