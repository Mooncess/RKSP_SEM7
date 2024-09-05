package practice1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Task1 {
    public static List<Integer> generateArray10000() {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int randomNumber = random.nextInt();
            list.add(randomNumber);
        }
        return list;
    }

    private static int findMaxInRange(List<Integer> sublist) throws
            InterruptedException {
        int max = Integer.MIN_VALUE;
        for (int number : sublist) {
            Thread.sleep(1);
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    public static int findMaxNumber(List<Integer> list) throws InterruptedException {
        int maxNumber = Integer.MIN_VALUE;
        for (int number : list) {
            Thread.sleep(1);
            if (number > maxNumber) {
                maxNumber = number;
            }
        }
        return maxNumber;
    }

    public static int findMaxNumberMultithreading(List<Integer> list) throws
            InterruptedException, ExecutionException {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        List<Callable<Integer>> tasks = new ArrayList<>();
        int batchSize = list.size() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int startIndex = i * batchSize;
            final int endIndex = (i == numberOfThreads - 1) ? list.size() : (i +
                    1) * batchSize;
            tasks.add(() -> findMaxInRange(list.subList(startIndex, endIndex)));
        }

        List<Future<Integer>> futures = executorService.invokeAll(tasks);

        int maxNumber = Integer.MIN_VALUE;

        for (Future<Integer> future : futures) {
            int partialMax = future.get();
            Thread.sleep(1);
            if (partialMax > maxNumber) {
                maxNumber = partialMax;
            }
        }

        executorService.shutdown();

        return maxNumber;
    }

    static class MaxFinderTask extends RecursiveTask<Integer> {
        private List<Integer> list;
        private int start;
        private int end;

        MaxFinderTask(List<Integer> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= 1000) {
                try {
                    return findMaxInRange(list.subList(start, end));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            int middle = start + (end - start) / 2;

            MaxFinderTask leftTask = new MaxFinderTask(list, start, middle);
            MaxFinderTask rightTask = new MaxFinderTask(list, middle, end);

            leftTask.fork();
            rightTask.fork();

            int rightResult = rightTask.join();
            int leftResult = leftTask.join();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return Math.max(leftResult, rightResult);
        }

        public static int findMaxNumberFork(List<Integer> list) {
            if (list == null || list.isEmpty()) {
                throw new IllegalArgumentException("Список пуст или равен null");
            }
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MaxFinderTask task = new MaxFinderTask(list, 0, list.size());
            return forkJoinPool.invoke(task);
        }

        public static void main(String[] args) throws InterruptedException,
                ExecutionException {
            List<Integer> testList = generateArray10000();

            long startTime = System.nanoTime();
            int result = findMaxNumber(testList);
            long endTime = System.nanoTime();
            long durationInMilliseconds = (endTime - startTime) / 1000000;
            System.out.println("Время выполнения последовательной функции: " +
                    durationInMilliseconds + " миллисекунд. Результат - " + result);

            startTime = System.nanoTime();
            result = findMaxNumberMultithreading(testList);
            endTime = System.nanoTime();
            durationInMilliseconds = (endTime - startTime) / 1000000;
            System.out.println("Время выполнения многопоточной функции: " +
                    durationInMilliseconds + " миллисекунд. Результат - " + result);

            startTime = System.nanoTime();
            result = findMaxNumberFork(testList);
            endTime = System.nanoTime();
            durationInMilliseconds = (endTime - startTime) / 1000000;
            System.out.println("Время выполнения форк функции: " +
                    durationInMilliseconds + " миллисекунд. Результат - " + result);
        }
    }
}