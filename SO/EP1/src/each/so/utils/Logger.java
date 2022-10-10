package each.so.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class Logger {
    private static final String BASE_PATH = "src/logs/";
    private static List<String> lines = new ArrayList<String>();

    public static void log(String log) {
        System.out.print(log);
        lines.add(log);
    }

    public static void logln(String log) {
        System.out.println(log);
        lines.add(log + "\n");
    }

    // Salva os logs em um arquivo
    public static void salvarLog(int quantum) {
        String filename = nomeArquivo(quantum);
        File logfile = new File(BASE_PATH + filename);

        try (FileOutputStream fos = new FileOutputStream(logfile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(osw)) {
            logfile.createNewFile();

            for (String line : lines) {
                writer.append(line);
            }
        } catch (IOException e) {
            Logger.logln("Houve um erro ao salvar o arquivo de logs, veja o console para o output!");
            Logger.logln("Erro:" + e.toString());
        }
    }

    // Coloca o nome bonitinho :)
    private static String nomeArquivo(int quantum) {
        String s = Integer.toString(quantum);
        if (s.length() == 1) {
            s = "0" + s;
        }
        return "log" + s + ".txt";
    }
}
