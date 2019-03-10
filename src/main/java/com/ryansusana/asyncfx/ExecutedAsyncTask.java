package com.ryansusana.asyncfx;

import java.util.concurrent.TimeUnit;

public class ExecutedAsyncTask<T1, T2, T3> {

    private final AsyncTask<T1, T2, T3> task;

    public ExecutedAsyncTask(AsyncTask<T1, T2, T3> task) {
        this.task = task;
    }

    public T3 andWaitFor(long time, TimeUnit timeUnit) throws InterruptedException {
        task.countDownLatch.await(time, timeUnit);
        return task.param;
    }

    public T3 andWait() throws InterruptedException {
        task.countDownLatch.await();
        return task.param;
    }
}
