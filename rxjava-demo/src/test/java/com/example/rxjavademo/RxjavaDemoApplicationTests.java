package com.example.rxjavademo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RxjavaDemoApplicationTests {

    @Test
    public void disposable() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
                emitter.onNext(4);
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable disposable) {
                mDisposable = disposable;
                System.out.println("subscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);
                i++;
                if (i == 2) {
                    System.out.println("dispose");
                    mDisposable.dispose();
                    System.out.println("isDisposed: " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("on error");
            }

            @Override
            public void onComplete() {
                System.out.println("complete");

            }
        };
        observable.subscribe(observer);
    }

}
