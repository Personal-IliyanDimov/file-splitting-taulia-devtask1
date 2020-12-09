package com.taulia.devtask1.transformer.context.helper;

@FunctionalInterface
public interface SplitSourceSelector<T,R> {
    public R splitSource(T input);
}
