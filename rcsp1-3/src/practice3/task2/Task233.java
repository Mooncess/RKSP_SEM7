package practice3.task2;

import io.reactivex.Observable;

import java.util.Random;

public class Task233 {
    public static void main(String[] args) {
        Observable<Integer> randomNumbersStream = Observable.create(emitter -> {
            int count = new Random().nextInt(1);
            for (int i = 0; i < count; i++) {
                emitter.onNext(new Random().nextInt(100));
            }
            emitter.onComplete();
        });

        Observable<Integer> result = randomNumbersStream.lastElement().toObservable();
        result.subscribe(res -> System.out.println("Последнее число: " + res),
                throwable -> System.err.println("Ошибка: " + throwable));
    }
}
