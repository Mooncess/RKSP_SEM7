package practice3.task2;

import io.reactivex.Observable;

import java.util.Random;

public class Task213 {
    public static void main(String[] args) {
        Observable<Long> countObservable = Observable.create(emitter -> {
                    int count = new Random().nextInt(1001);
                    for (int i = 0; i < count; i++) {
                        emitter.onNext(new Random().nextInt(1001));
                    }
                    emitter.onComplete();
                })
                .count()
                .toObservable();

        // Подписка на Observable для получения результата
        countObservable.subscribe(count -> System.out.println("Сгенерировано чисел: " + count),
                throwable -> System.err.println("Ошибка: " + throwable));
    }
}
