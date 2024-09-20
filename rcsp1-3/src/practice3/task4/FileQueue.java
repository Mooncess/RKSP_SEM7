package practice3.task4;

import io.reactivex.Observable;

public class FileQueue {
    private final Observable<File> fileObservable;

    public FileQueue(int capacity) {
        this.fileObservable = new FileGenerator().generateFile()
                .replay(capacity)
                .autoConnect();
    }

    public Observable<File> getFileObservable() {
        return fileObservable;
    }
}