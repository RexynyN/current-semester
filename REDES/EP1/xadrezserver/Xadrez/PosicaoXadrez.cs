using System;
using System.Collections.Generic;
using System.Text;
using Xadrez.Jogo;


namespace XadrezServer.Xadrez
{
    class PosicaoXadrez
    {
        public char Coluna { get; set; }
        public int Linha { get; set; }

        public PosicaoXadrez(char coluna, int linha)
        {
            Coluna = coluna;
            Linha = linha;
        }

        public override string ToString()
        {
            return "" + Coluna + Linha;
        }

        public Posicao ToPosicao()
        {
            return new Posicao(8 - Linha, char.ToLower(Coluna) - 'a');
        }
    }
}
