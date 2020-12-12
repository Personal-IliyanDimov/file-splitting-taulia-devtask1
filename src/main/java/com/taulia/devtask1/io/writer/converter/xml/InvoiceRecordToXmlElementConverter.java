package com.taulia.devtask1.io.writer.converter.xml;

import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.shared.xml.XmlNodeNames;
import org.apache.commons.text.StringEscapeUtils;

public class InvoiceRecordToXmlElementConverter {

    public String convert(InvoiceRecord record, String externalInvoiceImageLocation) {
        final StringBuilder sb = new StringBuilder();
        sb.append(buildStartTag(XmlNodeNames.ELEMENT_ROW))
          .append(buildElement(XmlNodeNames.ELEMENT_BUYER, record.getBuyer()))
          .append(buildElement(XmlNodeNames.ELEMENT_IMAGE_NAME, record.getImageName()))
          .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_DUE_DATE, record.getInvoiceDueDate()))
          .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_NUMBER, record.getInvoiceNumber()))
          .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_AMOUNT, record.getInvoiceAmount()))
          .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_CURRENCY, record.getInvoiceCurrency()))
          .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_STATUS, record.getInvoiceStatus()))
          .append(buildElement(XmlNodeNames.ELEMENT_SUPPLIER, record.getSupplier()))
          .append(buildElement(XmlNodeNames.ELEMENT_INVOICE_IMAGE_LOCATION, StringEscapeUtils.escapeXml11(externalInvoiceImageLocation)))
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
