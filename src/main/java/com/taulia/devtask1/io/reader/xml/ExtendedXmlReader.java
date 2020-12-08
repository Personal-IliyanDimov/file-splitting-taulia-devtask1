package com.taulia.devtask1.io.reader.xml;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.model.ExtendedInvoiceRecord;
import com.taulia.devtask1.io.shared.xml.XmlNodeNames;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExtendedXmlReader implements InputReader<ExtendedInvoiceRecord> {
    private final BasicXmlReader basicXmlReader;
    private final Function<Map<String,String>, ExtendedInvoiceRecord> converter;

    public ExtendedXmlReader(File inputFile, Function<Map<String,String>, ExtendedInvoiceRecord> converter) {
        this.basicXmlReader = new BasicXmlReader(inputFile);
        this.converter = converter;
    }

    @Override
    public void process(Consumer<ExtendedInvoiceRecord> recordConsumer) throws Exception {
        basicXmlReader.process(XmlNodeNames.getExtendedInvoiceRecordChildElements(), new Consumer<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> map) {
                final ExtendedInvoiceRecord extendedInvoiceRecord = converter.apply(map);
                recordConsumer.accept(extendedInvoiceRecord);
            }
        });
    }
}
