package com.taulia.devtask1.io.writer;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.model.ExtendedInvoiceRecord;
import com.taulia.devtask1.io.reader.converter.ExtendedInvoiceRecordToXmlElementConverter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@RequiredArgsConstructor
public class IntXmlWriter implements OutputWriter<ExtendedInvoiceRecord> {

    private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String ELEMENT_ROOT_START = "<root>";
    private static final String ELEMENT_ROOT_END = "</root>";

    private final File outputFile;
    private final ExtendedInvoiceRecordToXmlElementConverter converter;

    private Writer writer;

    @Override
    public void init() throws IOException {
        if (! outputFile.createNewFile()) {
            throw new IllegalStateException("File name already exists:" + outputFile.toString());
        }

        writer = new PrintWriter(outputFile);
        writer.write(START + System.lineSeparator());
        writer.write(ELEMENT_ROOT_START + System.lineSeparator());
    }

    @Override
    public void process(ExtendedInvoiceRecord input, ImageContext imageContext) throws IOException {
        writer.write(converter.convert(input) + System.lineSeparator());
    }

    @Override
    public void end() throws IOException {
        writer.write(ELEMENT_ROOT_END);
    }

    @Override
    public void close() throws IOException {
        if (writer == null) {
            return ;
        }
        writer.close();
    }
}
