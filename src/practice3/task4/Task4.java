package practice3.task4;

// Основной класс системы обработки файлов
public class Task4 {
    public static void main(String[] args) {
        int queueCapacity = 5;
        FileQueue fileQueue = new FileQueue(queueCapacity);
        String[] supportedFileTypes = {"XML", "JSON", "XLS"};
        for (String fileType : supportedFileTypes) {
            new FileProcessor(fileType)
                    .processFiles(fileQueue.getFileObservable())
                    .subscribe(
                            () -> {},
                            throwable -> System.err.println("Error processing file: " + throwable));
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

