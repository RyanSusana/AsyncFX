package com.ryansusana.asyncfx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AsyncTaskPool<T1, T2, T3> {
    private final List<AsyncTask<T1, T2, T3>> tasksToExecute;

    public AsyncTaskPool() {
        tasksToExecute = new ArrayList<>();
    }

    public AsyncTaskPool<T1, T2, T3> addTask(AsyncTask<T1, T2, T3> task) {
        tasksToExecute.add(task);
        return this;
    }

    public ExecutedAsyncTask<T1, T2, List<T3>> execute(T1... params) {
        return toTaskBuilder().execute(params);
    }


    public AsyncTaskBuilder<T1, T2, List<T3>> toTaskBuilder() {
        return AsyncTasks.<T1, T2, List<T3>>newTask()
                .inBackground(params -> tasksToExecute
                        .stream()

                        //First execute the task asynchronously
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
