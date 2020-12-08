package com.taulia.devtask1.io.writer.xml;

import com.taulia.devtask1.io.model.ExtendedInvoiceRecord;
import com.taulia.devtask1.io.writer.converter.xml.ExtendedInvoiceRecordToXmlElementConverter;

import java.io.File;
import java.io.IOException;

public class ExtendedXmlWriter extends AbstractXmlWriter<ExtendedInvoiceRecord> {

    private final ExtendedInvoiceRecordToXmlElementConverter converter;

    public ExtendedXmlWriter(File outputFile, ExtendedInvoiceRecordToXmlElementConverter converter) {
        super(outputFile);
        this.converter = converter;
    }

    @Override
    public void process(ExtendedInvoiceRecord input, ImageContext imageContext) throws IOException {
        getWriter().write(converter.convert(input) + System.lineSeparator());
    }
}
