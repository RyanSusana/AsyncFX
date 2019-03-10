package com.ryansusana.asyncfx;


public class AsyncTasks {

    private AsyncTasks() {
    }

    public static <T1, T2> AsyncTaskBuilder<T1, T2> newTypedTask(Class<T1> inputType, Class<T2> outputType) {
        return new AsyncTaskBuilder<>();
    }

    public static <T1, T2> AsyncTaskBuilder<T1, T2> newTypedTask() {
        return new AsyncTaskBuilder<>();
    }

    public static AsyncTaskBuilder<Object, Object> newTask() {
        return new AsyncTaskBuilder<>();
    }

    public static <T1, T2> AsyncTaskPool<T1, T2> newTypedPool() {
        return new AsyncTaskPool<>();
    }

    public static <T1, T2> AsyncTaskPool<T1, T2> newTypedPool(Class<T1> inputType, Class<T2> outputType) {
        return new AsyncTaskPool<>();
    }

    public static AsyncTaskPool<Object, Object> newPool() {
        return new AsyncTaskPool<>();
    }
}
