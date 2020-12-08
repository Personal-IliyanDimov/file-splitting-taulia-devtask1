package com.taulia.devtask1.io.reader.xml;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.shared.xml.XmlNodeNames;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class XmlReader implements InputReader<InvoiceRecord> {
    private final BasicXmlReader basicXmlReader;
    private final Function<Map<String,String>, InvoiceRecord> converter;

    public XmlReader(File inputFile, Function<Map<String,String>, InvoiceRecord> converter) {
        this.basicXmlReader = new BasicXmlReader(inputFile);
        this.converter = converter;
    }

    @Override
    public void process(Consumer<InvoiceRecord> recordConsumer) throws Exception {
        basicXmlReader.process(XmlNodeNames.getInvoiceRecordChildElements(), new Consumer<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> map) {
                final InvoiceRecord extendedInvoiceRecord = converter.apply(map);
                recordConsumer.accept(extendedInvoiceRecord);
            }
        });
    }
}
