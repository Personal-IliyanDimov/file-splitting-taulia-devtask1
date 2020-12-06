package com.taulia.devtask1.io.reader;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.data.InvoiceRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class XmlReader implements InputReader {

    private final File inputFile;

    @Override
    public void process(Consumer recordConsumer) throws Exception {
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        final XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(inputFile));
        try {
            InvoiceRecord invoiceRecord = null;
            while (reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case XmlNodeNames.ELEMENT_ROW:
                            invoiceRecord = new InvoiceRecord();
                            break;
                        case XmlNodeNames.ELEMENT_BUYER:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setBuyer(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_IMAGE_NAME:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setImageName(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_INVOICE_IMAGE:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setInvoiceImage(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_INVOICE_DUE_DATE:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setInvoiceDueDate(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_INVOICE_NUMBER:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setInvoiceNumber(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_INVOICE_AMOUNT:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setInvoiceAmount(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_INVOICE_CURRENCY:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setInvoiceCurrency(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_INVOICE_STATUS:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setInvoiceStatus(readCharacterData(nextEvent));
                            break;
                        case XmlNodeNames.ELEMENT_SUPPLIER:
                            nextEvent = reader.nextEvent();
                            invoiceRecord.setSupplier(readCharacterData(nextEvent));
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XmlNodeNames.ELEMENT_ROW)) {
                        recordConsumer.accept(invoiceRecord);
                    }
                }
            }
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String readCharacterData(XMLEvent nextEvent) {
        return nextEvent.asCharacters().getData();
    }

    private static class XmlNodeNames {
        private static final String ELEMENT_ROW = "row";
        private static final String ELEMENT_BUYER = "buyer";
        private static final String ELEMENT_IMAGE_NAME = "image_name";
        private static final String ELEMENT_INVOICE_IMAGE = "invoice_image";
        private static final String ELEMENT_INVOICE_DUE_DATE = "invoice_due_date";
        private static final String ELEMENT_INVOICE_NUMBER = "invoice_number";
        private static final String ELEMENT_INVOICE_AMOUNT = "invoice_amount";
        private static final String ELEMENT_INVOICE_CURRENCY = "invoice_currency";
        private static final String ELEMENT_INVOICE_STATUS = "invoice_status";
        private static final String ELEMENT_SUPPLIER = "supplier";
    }
}
