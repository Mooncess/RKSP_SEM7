package practice3.task4;

public class File {
    private String fileName;
    private final String fileType;
    private final int fileSize;
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
