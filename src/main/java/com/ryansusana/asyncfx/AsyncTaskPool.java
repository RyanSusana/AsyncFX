package com.ryansusana.asyncfx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AsyncTaskPool<T1, T2> {
    private final List<AsyncTask<T1, T2>> tasksToExecute;

    public AsyncTaskPool(List<AsyncTask<T1, T2>> tasksToExecute) {
        this.tasksToExecute = tasksToExecute;
    }

    public AsyncTaskPool() {
        this(new ArrayList<>());
    }


    public AsyncTaskPool<T1, T2> addTask(AsyncTask<T1, T2> task) {
        tasksToExecute.add(task);
        return this;
    }


    public ExecutedAsyncTask<T1, List<T2>> execute(T1... params) {
        return toTaskBuilder().execute(params);
    }


    public AsyncTaskBuilder<T1, List<T2>> toTaskBuilder() {
        return AsyncTasks.<T1, List<T2>>newTypedTask()
                .inBackground(params -> tasksToExecute
                        .stream()

                        //First execute the typedTask asynchronously
                        .map(task -> task.execute(params))

                        // Terminal  operator to ensure all tasks are running in async
                        .collect(Collectors.toList()).stream()

                        //Wait for the previously executed tasks
                        .map(executedTask -> {
                            try {
                                return executedTask.andWait();
                            } catch (InterruptedException e) {
                                //Yeah I hate people that interrupt threads.
                                Thread.currentThread().interrupt();
                                return null;
                            }
                        })
                        //Return a list
                        .collect(Collectors.toList())
                );
    }

}
