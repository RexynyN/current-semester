package each.so;

import java.io.File;
import java.util.List;
import java.util.Optional;

import each.so.utils.*;
import each.so.escalonador.Escalonador;
import each.so.bcp.BCP;

public class Main {
    public static void main(String[] args) {
        InputReader reader = new InputReader();

        Optional<String> quantumReader = reader.readAll("quantum.txt");
        if (!quantumReader.isPresent()) {
            Logger.logln("Quantum não pôde ser lido, ou não existe. Abortando o programa.");
            Logger.salvarLog(0);
            return;
        }

        // Pega o valor de quantum
        int quantum = Integer.parseInt(quantumReader.get());
        Logger.logln("QUANTUM = " + quantum);

        // Lê as prioridades
        String[] priorities = reader.readLines("prioridades.txt");
        
        // Lê os arquivos na pasta de programas e inicializa o contador de métricas
        List<File> files = reader.getFilesInFolder("programas");
        Report report = new Report(files.size(), quantum);

        // Cria o escalonador e carrega com os programas
        Escalonador escalonador = new Escalonador(report, quantum);

        // Lê as intruções e cria processos com prioridades
        int index = 0;
        for (File file : files) {
            List<String> lines = reader.readLines(file);
            escalonador.carregaBCP(BCP.parse(lines, priorities[index]));
            index++;
        }

        // Roda o escalonador, mostra as métricas e salva os logs
        escalonador.inicia();
        report.relatorio();
        Logger.salvarLog(quantum);
    }
}
