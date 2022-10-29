
        // public static void Xadrez (MatchLogic partida)
        // {
        //     try
        //     {
        //         while (!partida.ChecarTerminada())
        //         {
        //             try
        //             {
        //                 Console.Clear();
        //                 Tela.ImprimirPartida(partida);
        //                 Console.WriteLine();
        //                 Console.Write("Origem: ");
        //                 Posicao origem = Tela.LerPosicaoXadrez().ToPosicao();
        //                 partida.ValidarPosicaoDeOrigem(origem);


        //                 bool[,] posicoes = partida.Tab.Peca(origem).MovimentosPossiveis();
        //                 Console.Clear();
        //                 Tela.ImprimirTabuleiro(partida.Tab, posicoes);

        //                 Console.WriteLine();
        //                 Console.Write("Destino: ");
        //                 Posicao destino = Tela.LerPosicaoXadrez().ToPosicao();
        //                 partida.ValidarPosicaoDeDestino(origem, destino);

        //                 partida.RealizaJogada(origem, destino);
        //                 connection.AwaitTurn(); // PseudoCódigo para implementar uma espera ocupada até o próximo turno
        //             }
        //             catch(TabuleiroException e)
        //             {
        //                 Console.WriteLine(e.Message);
        //                 Console.ReadKey();
        //             }
        //             catch (Exception e)
        //             {
        //                 Console.WriteLine(e.Message);
        //                 Console.ReadKey();
        //             }
        //         }
        //     }
        //     catch (Exception e)
        //     {
        //         Console.WriteLine("Ocorreu um erro na partida, pressione ENTER para voltar ao menu");
        //         Console.WriteLine("Mensagem: " + e.Message);
        //         Console.ReadKey();
        //         StartMenu(true);
        //     }
        // }