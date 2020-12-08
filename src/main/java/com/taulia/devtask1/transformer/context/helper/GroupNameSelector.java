package com.taulia.devtask1.transformer.context.helper;

public interface GroupNameSelector<T,R> {
    public R groupName(T input);
}
