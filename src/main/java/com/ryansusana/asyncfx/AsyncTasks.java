package com.ryansusana.asyncfx;


public class AsyncTasks {

    private AsyncTasks() {
    }

    public static <T1, T2, T3> AsyncTaskBuilder<T1, T2, T3> newTask(Class<T1> t1Class, Class<T2> t2Class, Class<T3> t3Class) {
        return new AsyncTaskBuilder<>();
    }

    public static <T1, T2, T3> AsyncTaskBuilder<T1, T2, T3> newTask() {
        return new AsyncTaskBuilder<>();
    }

    public static <T> AsyncTaskBuilder<Void, Void, T> newBasicTask() {
        return newTask();
    }

    public static AsyncTaskBuilder<Void, Void, String> newStringTask() {
        return newBasicTask();
    }

}
