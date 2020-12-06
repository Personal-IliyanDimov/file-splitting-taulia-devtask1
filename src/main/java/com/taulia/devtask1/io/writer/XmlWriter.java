package com.taulia.devtask1.io.writer;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToXmlElementConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@Slf4j
@RequiredArgsConstructor
public class XmlWriter implements OutputWriter<InvoiceRecord> {
    private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String ELEMENT_ROOT_START = "<root>";
    private static final String ELEMENT_ROOT_END = "</root>";

    private final File outputFile;
    private final InvoiceRecordToXmlElementConverter converter;

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
    public void process(InvoiceRecord input, ImageContext imageContext) throws IOException {
        File imageFile = null;
        if ((input.getInvoiceImage() != null) && (! input.getInvoiceImage().isBlank())) {
            imageFile = imageContext.generateFileName();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(imageFile))) {
                bw.write(input.getInvoiceImage());
            }
        }

        writer.write(converter.convert(input, imageFile.toString()) + System.lineSeparator());
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
