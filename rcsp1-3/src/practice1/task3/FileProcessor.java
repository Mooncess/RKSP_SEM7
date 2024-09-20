package practice1.task3;

import java.util.concurrent.BlockingQueue;

public class FileProcessor implements Runnable {
    private BlockingQueue<File> queue;
    private String allowedFileType;
    public FileProcessor(BlockingQueue<File> queue, String allowedFileType) {
        this.queue = queue;
        this.allowedFileType = allowedFileType;
    }
    @Override
    public void run() {
        while (true) {
            try {
                File file = queue.peek();

                if (file != null) {
                    if (file.getFileType().equals(allowedFileType)) {
                        long processingTime = file.getFileSize() * 7L;
                        Thread.sleep(processingTime);
                        System.out.println("Обработан файл типа " +
                                file.getFileType() + " с именем " + file.getFileName() +
                                " и с размером " + file.getFileSize() + ". Время обработки: " + processingTime + " мс.");
                        queue.remove(file);
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}