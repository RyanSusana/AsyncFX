package com.ryansusana.asyncfx;

public class AsyncTaskBuilder<T1, T2, T3> {


    private During<T1, T3> during = params -> {
        throw new AsyncException("No background process specified.");
    };
    private After<T3> after = params -> {

    };
    private Before before = () -> {

    };


    public AsyncTaskBuilder<T1, T2, T3> inBackground(During<T1, T3> during) {
        this.during = during;
        return this;
    }

    public AsyncTaskBuilder<T1, T2, T3> after(After<T3> after) {
        this.after = after;
        return this;
    }

    public AsyncTaskBuilder<T1, T2, T3> before(Before before) {
        this.before = before;
        return this;
    }


    public AsyncTask<T1, T2, T3> create() {
        return new AsyncTask<T1, T2, T3>() {
            @Override
            public void before() {
                before.before();
            }

            @Override
            public T3 during(T1... params) {

                return during.during(params);
            }

            @Override
            public void after(T3 params) {
                after.after(params);
            }

        };
    }

    public ExecutedAsyncTask<T1, T2, T3> execute(T1... params) {
        return create().execute(params);
    }

}
