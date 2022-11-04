namespace XadrezServer.Requests
{
    public class Estatisticas
    {
        public int Movimentos { get; set; }
        public string PecasPegas { get; set; }
        public int NumPecas { get; set; }
        public string PecasPerdidas { get; set; }
        public int Turnos { get; set; }
        public string Vencedor { get; set; }
        public DateTime Comeco { get; set; }
        public DateTime Termino { get; set; }

        public Estatisticas()
        {
            Vencedor = "";
            Movimentos = 0;
            PecasPegas = "";
            PecasPerdidas = "";
            Turnos = 0;
            NumPecas = 1;
        }

        public string[] GerarEstatisticas()
        {
            double aux = 0;
            List<string> stats = new List<string>();
            TimeSpan span = Termino - Comeco;

            stats.Add($"Vencedor: {Vencedor}!");
            stats.Add($"Número de Turnos da Partida: {Turnos}");
            stats.Add($"Número de Movimentos Realizados: {Movimentos}");
            stats.Add($"Duração da Partida: {HoraCerta(span)}");
            aux = Turnos / span.TotalMinutes;
            stats.Add($"Média de Turnos por Minuto: {aux:F2}");
            stats.Add($"Peças Pegas: {PecasPegas}");
            stats.Add($"Pecas Perdidas: {PecasPerdidas}");

            aux = Movimentos / NumPecas;
            stats.Add($"Média de Movimentos por Peças Pegas: {aux:F2}");

            return stats.ToArray(); 
        }

        private string HoraCerta(TimeSpan ts)
        {
            string time = "";
            time += ts.Hours < 10 ? "0" + ts.Hours : ts.Hours;
            time += ":";
            time += ts.Minutes < 10 ? "0" + ts.Minutes : ts.Minutes;
            time += ":";
            time += ts.Seconds < 10 ? "0" + ts.Seconds : ts.Seconds;
            return time;
        }

    }
}