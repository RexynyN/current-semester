namespace XadrezClient.Response
{
    class MovimentosPossiveis : BaseResponse
    {
        public bool[][] Posicoes { get; set; }
        public string MatchId{ get; set; }
        public string Player { get; set; }

        public bool[,] MatrizPossiveis()
        {
            bool [,] possiveis = new bool [8,8];
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    possiveis[i, j] = Posicoes[i][j];
                }
            }

            return possiveis;
        }
    }
}