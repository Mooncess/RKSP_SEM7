package practice3.task2;

import io.reactivex.Observable;

import java.util.Random;

public class Task223 {
    public static void main(String[] args) {
        Observable<Integer> stream1 = Observable.range(0, 1000)
                .map(i -> new Random().nextInt(10));

        Observable<Integer> stream2 = Observable.range(0, 1000)
                .map(i -> new Random().nextInt(10));

        Observable.zip(stream1, stream2, (num1, num2) -> {
                    return "Первый: " + num1 + ", Второй: " + num2;
                })
                .subscribe(System.out::println,
                        throwable -> System.err.println("Ошибка: " + throwable));
    }
}

