package practice2;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task4v2 {
    private static Map<Path, List<Map<Integer, String>>> fileContentsMap = new HashMap<>();
    private static Map<Path, Short> fileHashes = new HashMap<>();
    public static void main(String[] args) throws IOException, InterruptedException {
        Path directory = Paths.get("src/practice2/basedir");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        firstObserve(directory);

        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path filePath = (Path) event.context();
                    System.out.println("Создан новый файл: " + filePath);
                    fileContentsMap.put(filePath,
                            readLinesFromFile(directory.resolve(filePath)));
                    calculateFileHash(directory.resolve(filePath));
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    Path filePath = (Path) event.context();
                    System.out.println("Файл изменен: " + filePath);
                    detectFileChanges(directory.resolve(filePath));
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    Path filePath = (Path) event.context();
                    System.out.println("Удален файл: " + filePath);
                    short checksum = fileHashes.get(directory.resolve(filePath));
                    System.out.printf("Контрольная сумма удаленного файла %s: 0x%04X%n", filePath, checksum);
                }
            }
            key.reset();
        }
    }
    private static void firstObserve(Path directory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory))
        {
            for (Path filePath : stream) {
                if (Files.isRegularFile(filePath)) {
                    fileContentsMap.put(filePath, readLinesFromFile(filePath));
                    calculateFileHash(filePath);
                }
            }
        }
    }
    private static void detectFileChanges(Path filePath) throws IOException {
        List<Map<Integer,String>> newFileContents = readLinesFromFile(filePath);
        List<Map<Integer,String>> oldFileContents = fileContentsMap.get(filePath);
        if (oldFileContents != null) {
            List<Map<Integer,String>> addedLines = newFileContents.stream()
                    .filter(line -> !oldFileContents.contains(line))
                    .toList();
            List<Map<Integer,String>> deletedLines = oldFileContents.stream()
                    .filter(line -> !newFileContents.contains(line))
                    .toList();
            if (!addedLines.isEmpty()) {
                System.out.println("Добавленные строки в файле " + filePath +
                        ":");
                addedLines.forEach(line -> System.out.println("+ " + line));
            }
            if (!deletedLines.isEmpty()) {
                System.out.println("Удаленные строки из файла " + filePath +
                        ":");
                deletedLines.forEach(line -> System.out.println("- " + line));
            }
        }

        else {
            if (!newFileContents.isEmpty()) {
                System.out.println("Добавленные строки в файле " + filePath +
                        ":");
                newFileContents.forEach(line -> System.out.println("+ " + line));
            }
        }
        calculateFileHash(filePath);
        fileContentsMap.put(filePath, newFileContents);
    }
    private static List<Map<Integer,String>> readLinesFromFile(Path filePath) throws
            IOException {
        List<Map<Integer,String>> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                Map<Integer,String> indexLine = new HashMap<>();
                indexLine.put(i, line);
                lines.add(indexLine);
                i++;
            }
        }
        return lines;
    }
    private static void calculateFileHash(Path filePath) throws IOException {
        fileHashes.put(filePath, Task3.calculateChecksum(filePath.toString()));
    }
}
