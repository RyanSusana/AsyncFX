package com.ryansusana.asyncfx;

import java.util.concurrent.TimeUnit;

public class ExecutedAsyncTask<T1, T2> {

    private final AsyncTask<T1, T2> task;

    public ExecutedAsyncTask(AsyncTask<T1, T2> task) {
        this.task = task;
    }

    public T2 andWaitFor(long time, TimeUnit timeUnit) throws InterruptedException {
        if (!task.countDownLatch.await(time, timeUnit)) {
            throw new AsyncException(String.format("Task failed to execute within %d %s", time, timeUnit.name()));
        }
        return task.param;
    }

    public T2 andWait() throws InterruptedException {
        task.countDownLatch.await();
        return task.param;
    }
}
