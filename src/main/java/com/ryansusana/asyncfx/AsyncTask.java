package com.ryansusana.asyncfx;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

public abstract class AsyncTask<T1, T2> {

    private boolean daemon = true;

    private T1[] params;

    protected abstract void before();

    protected abstract T2 during(T1... params) throws InterruptedException;

    protected abstract void after(T2 params);


    CountDownLatch countDownLatch = new CountDownLatch(1);
    T2 param = null;


    private final Thread backgroundThread = new Thread(() -> {
        try {
            param = during(params);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AsyncException("Background thread has been interrupted", e);
        } finally {
            countDownLatch.countDown();
        }

        Platform.runLater(() -> after(param));
    });

    public ExecutedAsyncTask<T1,  T2> execute(final T1... params) {
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