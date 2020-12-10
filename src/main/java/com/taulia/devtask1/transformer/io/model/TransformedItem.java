package com.taulia.devtask1.transformer.io.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TransformedItem<T> {
    private final T payload;
}
