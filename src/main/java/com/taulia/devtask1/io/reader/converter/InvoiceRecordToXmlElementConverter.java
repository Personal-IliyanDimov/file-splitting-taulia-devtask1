package com.taulia.devtask1.io.reader.converter;

import com.taulia.devtask1.io.data.InvoiceRecord;

public class InvoiceRecordToXmlElementConverter {
    private static final String ELEMENT_ROW = "row";
    private static final String ELEMENT_BUYER = "buyer";
    private static final String ELEMENT_IMAGENAME = "image_name";
    private static final String ELEMENT_INVOICEIMAGE = "invoice_image";
    private static final String ELEMENT_INVOICEDUEDATE = "invoice_due_date";
    private static final String ELEMENT_INVOICENUMBER = "invoice_number";
    private static final String ELEMENT_INVOICEAMOUNT = "invoice_amount";
    private static final String ELEMENT_INVOICECURRENCY = "invoice_currency";
    private static final String ELEMENT_INVOICESTATUS = "invoice_status";
    private static final String ELEMENT_SUPPLIER = "supplier";


    public String convert(InvoiceRecord record) {
        final StringBuilder sb = new StringBuilder();
        sb.append(buildStartTag(ELEMENT_ROW))
          .append(buildElement(ELEMENT_BUYER, record.getBuyer()))
          .append(buildElement(ELEMENT_IMAGENAME, record.getImageName()))
          .append(buildElement(ELEMENT_INVOICEIMAGE, record.getInvoiceImage()))
          .append(buildElement(ELEMENT_INVOICEDUEDATE, record.getInvoiceDueDate()))
          .append(buildElement(ELEMENT_INVOICENUMBER, record.getInvoiceNumber()))
          .append(buildElement(ELEMENT_INVOICEAMOUNT, record.getInvoiceAmount()))
          .append(buildElement(ELEMENT_INVOICECURRENCY, record.getInvoiceCurrency()))
          .append(buildElement(ELEMENT_INVOICESTATUS, record.getInvoiceStatus()))
          .append(buildElement(ELEMENT_SUPPLIER, record.getSupplier()))
          .append(buildEndTag(ELEMENT_ROW));

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
