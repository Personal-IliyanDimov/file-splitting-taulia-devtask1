package com.taulia.devtask1.io.reader;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.data.Record;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class CsvReader implements InputReader<Record> {

    private final File inputFile;
    private final Function<String, Record> converter;

    @Override
    public void process(Consumer<Record> recordConsumer) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String row = null;
            while ((row = br.readLine()) != null) {
                final Record record = converter.apply(row);
                recordConsumer.accept(record);
            }
        }
    }
}
