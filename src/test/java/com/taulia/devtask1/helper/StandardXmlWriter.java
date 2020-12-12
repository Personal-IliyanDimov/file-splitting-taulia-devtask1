package com.taulia.devtask1.helper;

import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.shared.xml.XmlNodeNames;
import com.taulia.devtask1.io.writer.xml.AbstractXmlWriter;

import java.io.File;
import java.io.IOException;

public class StandardXmlWriter extends AbstractXmlWriter<InvoiceRecord> {

    private final StandardInvoiceRecordToXmlElementConverter converter;

    public StandardXmlWriter(File outputFile) {
        super(outputFile);
        this.converter = new StandardInvoiceRecordToXmlElementConverter();
    }

    @Override
    public void process(InvoiceRecord input, ImageContext context) throws IOException {
        getWriter().write(converter.convert(input) + System.lineSeparator());
    }

    private static class StandardInvoiceRecordToXmlElementConverter {

        public String convert(InvoiceRecord record) {
            final StringBuilder sb = new StringBuilder();
            sb.append(buildStartTag(XmlNodeNames.ELEMENT_ROW))
                    .append(buildElement(XmlNodeNames.ELEMENT_BUYER, record.getBuyer()))
                    .append(buildElement(XmlNodeNames.ELEMENT_IMAGE_NAME, record.getImageName()))
                    .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_IMAGE, record.getInvoiceImage()))
                    .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_DUE_DATE, record.getInvoiceDueDate()))
                    .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_NUMBER, record.getInvoiceNumber()))
                    .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_AMOUNT, record.getInvoiceAmount()))
                    .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_CURRENCY, record.getInvoiceCurrency()))
                    .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_STATUS, record.getInvoiceStatus()))
                    .append(buildElement(XmlNodeNames.ELEMENT_SUPPLIER, record.getSupplier()))
                    .append(buildEndTag(XmlNodeNames.ELEMENT_ROW));

            return sb.toString();
        }

        private String buildElement(String tagName, String text) {
            return buildStartTag(tagName) + text + buildEndTag(tagName);
        }

        private String buildStartTag(String tagName) {
            return "<"+ tagName + ">";
        }

        private String buildEndTag(String tagName) {
            return "</"+ tagName + ">";
        }
    }
}
