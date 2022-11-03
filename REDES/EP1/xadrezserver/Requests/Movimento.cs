using Xadrez.Jogo;

namespace XadrezServer.Requests
{
    class Movimento
    {
        public string MatchId { get; set; }
        public string Player { get; set; }
        public int ColunaOrigem { get; set; }
        public int LinhaOrigem { get; set; }

        public Posicao Posicao()
        {
            return new Posicao(LinhaOrigem, ColunaOrigem);
        }
    }
}