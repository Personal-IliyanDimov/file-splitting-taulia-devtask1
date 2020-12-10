package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.transformer.context.helper.GroupNameSelector;
import com.taulia.devtask1.transformer.context.helper.SplitSourceSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public class GenericContext<T> {
    private final Function<Object, T> wrapperFunction;
    private final Function<T, Object> unwrapperFunction;
    private final GroupNameSelector<T, String> groupNameSelector;
    private final SplitSourceSelector<T, String> splitSourceSelector;
    private final Function<FileContext, File> fileNameProducer;
}
