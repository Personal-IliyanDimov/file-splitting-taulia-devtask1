package com.taulia.devtask1.io.shared.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class XmlNodeNames {
    public static final String ELEMENT_ROW = "row";
    public static final String ELEMENT_BUYER = "buyer";
    public static final String ELEMENT_IMAGE_NAME = "image_name";
    public static final String ELEMENT_INVOICE_IMAGE = "invoice_image";
    public static final String ELEMENT_INVOICE_DUE_DATE = "invoice_due_date";
    public static final String ELEMENT_INVOICE_NUMBER = "invoice_number";
    public static final String ELEMENT_INVOICE_AMOUNT = "invoice_amount";
    public static final String ELEMENT_INVOICE_CURRENCY = "invoice_currency";
    public static final String ELEMENT_INVOICE_STATUS = "invoice_status";
    public static final String ELEMENT_SUPPLIER = "supplier";
    public static final String ELEMENT_INVOICE_IMAGE_LOCATION = "invoice_image_location";

    private static final Set<String> invoiceRecordChildElements = new HashSet<>(
        Arrays.asList(ELEMENT_BUYER, ELEMENT_IMAGE_NAME, ELEMENT_INVOICE_IMAGE,
                      ELEMENT_INVOICE_DUE_DATE, ELEMENT_INVOICE_NUMBER, ELEMENT_INVOICE_AMOUNT,
                      ELEMENT_INVOICE_CURRENCY, ELEMENT_INVOICE_STATUS, ELEMENT_SUPPLIER));

    private static final Set<String> extendedInvoiceRecordChildElements = new HashSet<>(
            Arrays.asList(ELEMENT_BUYER, ELEMENT_IMAGE_NAME, ELEMENT_INVOICE_IMAGE,
                    ELEMENT_INVOICE_DUE_DATE, ELEMENT_INVOICE_NUMBER, ELEMENT_INVOICE_AMOUNT,
                    ELEMENT_INVOICE_CURRENCY, ELEMENT_INVOICE_STATUS, ELEMENT_SUPPLIER,
                    ELEMENT_INVOICE_IMAGE_LOCATION));

    public static Set<String> getInvoiceRecordChildElements() {
        return invoiceRecordChildElements;
    }

    public static Set<String> getExtendedInvoiceRecordChildElements() {
        return extendedInvoiceRecordChildElements;
    }
}
