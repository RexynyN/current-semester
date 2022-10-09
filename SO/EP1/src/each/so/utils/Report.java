package each.so.utils;

// Mostra o relatório de uso do escalonador
public class Report {
    private final int qtdProcessos;
    private final int quantum;

    private int trocasProcesso;

    private int qtdInstrucoes;
    private int quantaUtilizado;


    public Report(int qtdProcessos, int quantum) {
        this.qtdProcessos = qtdProcessos;
        this.quantum = quantum;
        this.trocasProcesso = 0;
        this.quantaUtilizado = 0;
    }

    public void incrementaTrocasProcesso() {
        trocasProcesso++;
    }

    public void instrucoesPorQuantum(int instrucoes) {
        this.qtdInstrucoes += instrucoes;
        this.quantaUtilizado++;
    }

    // Relatório de métricas
    public void relatorio() {
        Logger.logln("\n");
        Logger.logln("==== RELATÓRIO DE EXECUÇÃO DO ESCALONADOR =====");
        Logger.logln("QUANTUM:               " + quantum);
        Logger.logln("MEDIA DE TROCAS:       " + (double) trocasProcesso / qtdProcessos);
        Logger.logln("MEDIA DE INSTRUCOES:   " + (double) qtdInstrucoes / quantaUtilizado);
    }
}
