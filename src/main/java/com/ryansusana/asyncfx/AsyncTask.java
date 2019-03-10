package com.ryansusana.asyncfx;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

public abstract class AsyncTask<T1, T2, T3> {

    private boolean daemon = true;

    private T1[] params;

    protected abstract void before();

    protected abstract T3 during(T1... params);

    protected abstract void after(T3 params);


    CountDownLatch countDownLatch = new CountDownLatch(1);
    T3 param = null;


    private final Thread backgroundThread = new Thread(() -> {
        try {
            param = during(params);
        } finally {
            countDownLatch.countDown();
        }

        Platform.runLater(() -> after(param));
    });

    public ExecutedAsyncTask<T1, T2, T3> execute(final T1... params) {
        this.params = params;
        Platform.runLater(() -> {

            before();

            backgroundThread.setDaemon(daemon);
            backgroundThread.start();
        });
        return new ExecutedAsyncTask<>(this);
    }


    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public final boolean isInterrupted() {
        return this.backgroundThread.isInterrupted();
    }

    public final boolean isAlive() {
        return this.backgroundThread.isAlive();
    }
}