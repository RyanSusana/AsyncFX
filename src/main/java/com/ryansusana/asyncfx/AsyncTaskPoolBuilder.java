package com.ryansusana.asyncfx;

import java.util.ArrayList;
import java.util.List;

public final class AsyncTaskPoolBuilder<T1, T2> {
    private List<AsyncTask<T1, T2>> tasksToExecute = new ArrayList<>();

    private AsyncTaskPoolBuilder() {
    }

    public static <T1, T2> AsyncTaskPoolBuilder<T1, T2> anAsyncTaskPool() {
        return new AsyncTaskPoolBuilder<>();
    }

    public AsyncTaskPoolBuilder<T1, T2> addTask(AsyncTask<T1, T2> taskToExecute) {
        this.tasksToExecute.add(taskToExecute);
        return this;
    }

    public AsyncTaskPoolBuilder<T1, T2> addTask(AsyncTaskBuilder<T1, T2> task) {
        return addTask(task.create());
    }


    public AsyncTaskPool<T1, T2> create() {
        return new AsyncTaskPool<>(tasksToExecute);
    }

    public ExecutedAsyncTask<T1, List<T2>> execute(T1... params) {
        return create().toTaskBuilder().execute(params);
    }
}
