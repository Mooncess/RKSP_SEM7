package practice2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
public class Task3 {
    public static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
            FileChannel fileChannel = fileInputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            short checksum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get();
                }
                buffer.clear();
            }
            checksum = (short) ~checksum;
            return checksum;
        }
    }
    public static void main(String[] args) {
        String filePath = "src/practice2/task1/sample.txt"; // Путь к файлу, для которого нужно
        try {
            short checksum = calculateChecksum(filePath);
            System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath, checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
