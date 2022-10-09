package each.so.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputReader {
    private static final String BASE_PATH = "src/input/";

    // Ler toda a informação de um arquivo
    public Optional<String> readAll(String fileName) {
        try {
            List<String> content = Files.readAllLines(Paths.get(BASE_PATH + fileName));
            return Optional.of(String.join("", content).replaceAll("\n", ""));
        } catch (IOException e) {
            Logger.logln("Erro na leitura do arquivo: " + fileName + "\n" + e);
            return Optional.empty();
        }
    }

    // Retorna uma lista com todas as linhas de um arquivo
    public List<String> readLines(File file) {
        try {
            Stream<String> lines = Files.lines(file.toPath());
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            Logger.logln("Erro na leitura do arquivo: " + file.getName()+ "\n" + e);
            return Collections.<String>emptyList();
        }
    }

    public String[] readLines(String file) {
        try {
            Stream<String> lines = Files.lines(Paths.get(BASE_PATH + file));
            List<String> list = lines.collect(Collectors.toList());
            String[] array = new String[list.size()];
            return list.toArray(array);

        } catch (IOException e) {
            Logger.logln("Erro na leitura do arquivo: " + file+ "\n" + e);
            return new String [10];
        }
    }

    // Retorna todos os arquivos de um diretório
    public List<File> getFilesInFolder(String folderName) {
        try {
            Stream<Path> filePaths = Files.walk(Paths.get(BASE_PATH + folderName));

            return filePaths.map(Path::toFile).filter(File::isFile)
                    .sorted((file, other) -> file.getName().compareToIgnoreCase(other.getName()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            Logger.logln("Erro na leitura da pasta: " + folderName+ "\n" + e);
            return Collections.<File>emptyList();
        }
    }
}
