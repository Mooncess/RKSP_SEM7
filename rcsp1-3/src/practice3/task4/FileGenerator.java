package practice3.task4;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

// Генератор файлов
public class FileGenerator {
    private static AtomicInteger counter = new AtomicInteger(0);

    public Observable<File> generateFile() {
        return Observable
                .fromCallable(() -> {
                    try {
                        String[] fileTypes = {"XML", "JSON", "XLS"};
                        String fileType = fileTypes[(int) (Math.random() * 3)];
                        String fileName = fileType + counter;
                        counter.getAndIncrement();
                        int fileSize = (int) (Math.random() * 91) + 10;
                        Thread.sleep((long) (Math.random() * 901) + 100); // Имитация генерации файла
                        return new File(fileName, fileType, fileSize);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .repeat()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
