namespace XadrezServer.Requests
{
    public class Movimento
    {
        public string MatchId { get; set; }
        public string Coluna { get; set; }
        public int Linha { get; set; }

        public Posicao Posicao()
        {
            return new Posicao(8 - Linha, char.ToLower(Coluna) - 'a');
        }
    }
}