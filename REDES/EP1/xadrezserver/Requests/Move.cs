using Xadrez.Jogo;

namespace XadrezServer.Requests
{
    class Move
    {
        public string MatchId { get; set; }
        public string Player { get; set; }
        public int LinhaOrigem { get; set; }
        public int ColunaOrigem { get; set; }
        public int LinhaDestino { get; set; }
        public int ColunaDestino { get; set; }

        public Posicao PosicaoOrigem()
        {
            return new Posicao(LinhaOrigem, ColunaOrigem);
        }

        public Posicao PosicaoDestino()
        {
            return new Posicao(LinhaDestino, ColunaDestino);
        }

        public Cor Cor()
        {
            try
            {
                return Enum.Parse<Cor>(Player);
            }
            catch (Exception e)
            {
                return new Cor();
            }
        }
    }
}