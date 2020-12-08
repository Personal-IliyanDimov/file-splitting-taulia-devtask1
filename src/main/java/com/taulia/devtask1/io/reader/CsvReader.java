package com.taulia.devtask1.io.reader;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.model.InvoiceRecord;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class CsvReader implements InputReader<InvoiceRecord> {

    private final File inputFile;
    private final Function<String, InvoiceRecord> converter;

    @Override
    public void process(Consumer<InvoiceRecord> recordConsumer) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            boolean headers = false;
            String row = null;
            while ((row = br.readLine()) != null) {
                if (! headers) {
                    headers = true;
                    continue;
                }
                final InvoiceRecord record = converter.apply(row);
                recordConsumer.accept(record);
            }
        }
    }
}
