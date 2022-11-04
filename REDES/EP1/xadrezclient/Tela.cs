using Xadrez.Xadrez;

namespace XadrezClient
{
    class Tela
    {
        public static void ImprimirPartida(MatchLogic partida)
        {
            Console.WriteLine("Partida: " + partida.MatchId);
            if(partida.Player == "Preta")
                Printer.Black("Você controla as peças Pretas\n");
            else 
                Console.WriteLine("Você controla as peças Brancas\n");
            
            ImprimirTabuleiro(partida.Tabuleiro);
            Console.WriteLine();
            ImprimirPecasCapturadas(partida);
            Console.WriteLine();
            Console.WriteLine("Turno: " + partida.Turno);
            if (!partida.Terminada())
            {
                Console.WriteLine("Aguardando Jogada: " + partida.JogadorAtual);
                if (partida.Xeque)
                    Console.WriteLine("XEQUE!");
            }
            else
            {
                Console.WriteLine("XEQUEMATE!");
                Console.WriteLine("Vencedor: " + partida.Vencedor);
            }

        }
        public static void ImprimirPecasCapturadas(MatchLogic partida)
        {
            Console.WriteLine("Peças Capturadas: ");
            Console.Write("Brancas: ");
            Console.WriteLine(partida.PegasBrancas);
            Console.Write("Pretas: ");
            Printer.Black(partida.PegasPretas);
        }

        public static void ImprimirTabuleiro(string[,] tab)
        {
            for (int i = 0; i < tab.GetLength(0); i++)
            {
                Console.Write(8 - i + " ");
                for (int j = 0; j < tab.GetLength(1); j++)
                {
                    ImprimirPeca(tab[i, j]);
                }
                Console.WriteLine();
            }
            Console.WriteLine("  a b c d e f g h");
        }

        public static void ImprimirTabuleiro(string [,] tab, bool[,] posicoes)
        {
            ConsoleColor original = Console.BackgroundColor;
            ConsoleColor destaque = ConsoleColor.DarkGray;

            for (int i = 0; i < tab.GetLength(0); i++)
            {
                Console.Write(8 - i + " ");
                for (int j = 0; j < tab.GetLength(1); j++)
                {
                    if (posicoes[i, j])
                        Console.BackgroundColor = destaque;
                    else
                        Console.BackgroundColor = original;

                    ImprimirPeca(tab[i,j]);
                    Console.BackgroundColor = original;
                }
                Console.WriteLine();
            }
            Console.WriteLine("  a b c d e f g h");
            Console.BackgroundColor = original;

        }

        public static void ImprimirPeca(string peca)
        {
            if (peca == "-")
                Console.Write("- ");
            else
            {
                if (peca.Contains("P+"))
                {
                    ConsoleColor aux = Console.ForegroundColor;
                    Console.ForegroundColor = ConsoleColor.Yellow;
                    Console.Write(peca.Replace("P+", "") + " ");
                    Console.ForegroundColor = aux;
                }
                else
                    Console.Write(peca + " ");
                    
            }
        }

        public static PosicaoXadrez LerPosicaoXadrez()
        {
            string s = Console.ReadLine();
            char coluna = s[0];
            int linha = int.Parse(s[1] + "");
            return new PosicaoXadrez(coluna, linha);
        }

        public static void ImprimirEstatisticas(List<string> list)
        {
            int glob = 1;
            foreach(var stat in list)
            {
                if(glob % 2 == 0)
                    Console.WriteLine(stat);
                else
                    Printer.Black(stat);

                glob++;
            }
        }
    }
}
