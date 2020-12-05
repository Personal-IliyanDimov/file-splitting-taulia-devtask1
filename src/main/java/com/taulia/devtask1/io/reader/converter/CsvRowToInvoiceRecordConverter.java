package com.taulia.devtask1.io.reader.converter;

import com.taulia.devtask1.io.data.InvoiceRecord;

import java.util.function.Function;

public class CsvRowToInvoiceRecordConverter implements Function<String, InvoiceRecord> {

    @Override
    public InvoiceRecord apply(String rowText) {
        final String[] parts = rowText.split(",");
        if (parts.length != 9) {
            throw new IllegalArgumentException("Structure/format of row is different from the expected one:" + rowText);
        }

        final InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.setBuyer(parts[0]);
        invoiceRecord.setImageName(parts[1]);
        invoiceRecord.setInvoiceImage(parts[2]);
        invoiceRecord.setInvoiceDueDate(parts[3]);
        invoiceRecord.setInvoiceNumber(parts[4]);
        invoiceRecord.setInvoiceAmount(parts[5]);
        invoiceRecord.setInvoiceCurrency(parts[6]);
        invoiceRecord.setInvoiceStatus(parts[7]);
        invoiceRecord.setSupplier(parts[8]);

        return invoiceRecord;
    }
}
