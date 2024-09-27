package practice1;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Task2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите число (или 'exit' для выхода): ");
            String userInput = scanner.nextLine();

            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }

            try {
                int number = Integer.parseInt(userInput);

                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> calculateSquare(number), executorService);

                future.thenAccept(result ->
                                System.out.println("\n[ Результат для числа " + number + " : " + result + " ]"))
                        .exceptionally(e -> {
                            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
                            return null;
                        });

            } catch (NumberFormatException e) {
                System.err.println("Неверный формат числа. Пожалуйста, введите целое число.");
            }
        }

        executorService.shutdown();
        scanner.close();
    }

    private static int calculateSquare(int number) {
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * number;
    }
}
