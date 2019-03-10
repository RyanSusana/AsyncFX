package com.ryansusana.asyncfx;


public class AsyncTasks {

    private AsyncTasks() {
    }

    public static <T1, T2> AsyncTaskBuilder<T1, T2> newTask(Class<T1> inputType, Class<T2> outputType) {
        return new AsyncTaskBuilder<>();
    }

    public static <T1, T2> AsyncTaskBuilder<T1, T2> newTask() {
        return new AsyncTaskBuilder<>();
    }

    public static <T> AsyncTaskBuilder<Void, T> newBasicTask() {
        return newTask();
    }

    public static AsyncTaskBuilder<Void, String> newStringTask() {
        return newBasicTask();
    }

}
