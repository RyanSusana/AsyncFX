package com.ryansusana.asyncfx;

public interface During<T1, T2> {

    T2 during(T1... params) throws InterruptedException;

}
