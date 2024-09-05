package practice1.task3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
public class File {
    private String fileName;
    private String fileType;
    private int fileSize;
    public File(String fileName, String fileType, int fileSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
    public String getFileType() {
        return fileType;
    }
    public int getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }
}


