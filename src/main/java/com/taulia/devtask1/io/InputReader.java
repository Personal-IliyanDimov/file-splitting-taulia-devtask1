package com.taulia.devtask1.io;

import java.io.IOException;
import java.util.function.Consumer;

public interface InputReader<T> {

    public void process(Consumer<T> recordConsumer) throws IOException;
}
