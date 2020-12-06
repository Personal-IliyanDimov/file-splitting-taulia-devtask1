package com.taulia.devtask1.io.writer;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToCsvRowConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class CsvWriter implements OutputWriter<InvoiceRecord> {
    private final File outputFile;
    private final InvoiceRecordToCsvRowConverter converter;

    private Writer writer;

    @Override
    public void init() throws IOException {
        if (! outputFile.createNewFile()) {
            throw new IllegalStateException("File name already exists:" + outputFile.toString());
        }

        writer = new PrintWriter(outputFile);
        writer.write(converter.getHeaders() + System.lineSeparator());
    }

    @Override
    public void process(InvoiceRecord input, ImageContext imageContext) throws IOException {
        writer.write(converter.convert(input) + System.lineSeparator());
    }

    @Override
    public void end() throws IOException {
    }

    @Override
    public void close() throws IOException {
        if (Objects.isNull(writer)) {
            return ;
        }

        writer.close();
    }
}
