package com.hwonchul.movie.util;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

/**
 * Single liveData Wrapper
 *
 * @param <T>
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    // 이미 Event가 발생했는지 판단하는 boolean
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasActiveObservers()) {
            // 이 livedata의 단점은 여러 구독자일 때 한 구독자에게만 간다는 것이 문제점이다.
            Timber.d("Multiple observers registered but only one will be notified of changes.");
        }

        // setValue로 변경됐을 때 true일 때만 감지한다.
        // 이를 통해 1회성 처리를 할 수 있다.
        super.observe(owner, t -> {
            // true일 때만 false가 일어난다.
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    @MainThread
    public void call() {
        setValue(null);
    }
}
