package com.taulia.devtask1.io.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtendedInvoiceRecord {
    private InvoiceRecord invoiceRecord;
    private String invoiceImageLocation;
}
