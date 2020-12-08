package com.taulia.devtask1.io.writer.xml;

import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.writer.converter.xml.InvoiceRecordToXmlElementConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class XmlWriter extends AbstractXmlWriter<InvoiceRecord> {

    private final InvoiceRecordToXmlElementConverter converter;

    public XmlWriter(File outputFile, InvoiceRecordToXmlElementConverter converter) {
        super(outputFile);
        this.converter = converter;
    }

    @Override
    public void process(InvoiceRecord input, ImageContext imageContext) throws IOException {
        File imageFile = null;
        if ((input.getInvoiceImage() != null) && (! input.getInvoiceImage().isBlank())) {
            imageFile = imageContext.generateFileName();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(imageFile))) {
                bw.write(input.getInvoiceImage());
            }
            input.setInvoiceImage(null);
        }

        getWriter().write(converter.convert(input, imageFile.toString()) + System.lineSeparator());
    }
}
