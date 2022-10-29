namespace XadrezClient.Response
{
    class Partida : BaseResponse
    {
        public string? JogadorAtual { get; set; }
        public string? MatchId { get; set; }
        public string? Player { get; set; }
        public string[][] Tabuleiro { get; set; }
        public bool Xeque { get; set; }
        public int Turno { get; set; }
        public string? PegasPretas { get; set; }
        public string? PegasBrancas { get; set; }

        public string[,]? MatrizTabuleiro()
        {
            string[,] possiveis = new string[8, 8];
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    possiveis[i, j] = Tabuleiro[i][j];
                }
            }

            return possiveis;
        }
    }
}














