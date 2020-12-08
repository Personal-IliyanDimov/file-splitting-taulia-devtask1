package com.taulia.devtask1.io.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceRecord {
    private String buyer;
    private String imageName;
    private String invoiceImage;
    private String invoiceDueDate;
    private String invoiceNumber;
    private String invoiceAmount;
    private String invoiceCurrency;
    private String invoiceStatus;
    private String supplier;
}
