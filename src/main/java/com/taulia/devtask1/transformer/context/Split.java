package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.transformer.strategy.Strategy;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.function.Function;

@Getter
@Setter
public class Split {
    private boolean deleteInput;
    private File inputFile;
    private Strategy strategy;
    private SplitDetails splitDetails;

    @Getter
    @Setter
    public static class SplitDetails {
        private Long splitFactor;
        private Function<Long, Function<String, Long>> splitFunctionFactory;
    }
}
