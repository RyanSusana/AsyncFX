package com.ryansusana.asyncfx;

public class AsyncTaskBuilder<T1, T2> {


    private During<T1, T2> during = null;
    private After<T2> after = params -> {

    };
    private Before before = () -> {

    };


    public AsyncTaskBuilder<T1, T2> inBackground(During<T1, T2> during) {
        this.during = during;
        return this;
    }

    public AsyncTaskBuilder<T1, T2> after(After<T2> after) {
        this.after = after;
        return this;
    }

    public AsyncTaskBuilder<T1, T2> before(Before before) {
        this.before = before;
        return this;
    }


    public AsyncTask<T1, T2> create() {
        if (during == null) {
            throw new AsyncException("No background process specified.");
        }
        return new AsyncTask<T1, T2>() {
            @Override
            public void before() {
                before.before();
            }

            @Override
            public T2 during(T1... params) throws InterruptedException {

                return during.during(params);
            }

            @Override
            public void after(T2 params) {
                after.after(params);
            }

        };
    }

    public ExecutedAsyncTask<T1, T2> execute(T1... params) {
        return create().execute(params);
    }

}
