package com.ryansusana.asyncfx


inline fun <T1, T2> typedTask(buildTask: AsyncTaskBuilder<T1, T2>.() -> Unit): AsyncTask<T1, T2> {
    val newTask = AsyncTasks.newTypedTask<T1, T2>()


    newTask.buildTask()
    return newTask.create()
}

inline fun task(buildTask: AsyncTaskBuilder<Any, Any>.() -> Unit): AsyncTask<Any, Any> {
    return typedTask(buildTask)
}

inline fun <T1, T2> typedPool(buildPool: AsyncTaskPoolBuilder<T1, T2>.() -> Unit): AsyncTaskPool<T1, T2> {
    val newTask = AsyncTaskPoolBuilder.anAsyncTaskPool<T1, T2>()


    newTask.buildPool()
    return newTask.create()
}

inline fun pool(buildPool: AsyncTaskPoolBuilder<Any, Any>.() -> Unit): AsyncTaskPool<Any, Any> {
    return typedPool(buildPool)
}
