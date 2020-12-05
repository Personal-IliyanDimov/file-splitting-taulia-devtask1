package com.taulia.devtask1.io.reader.converter;

import com.taulia.devtask1.io.data.InvoiceRecord;

public class InvoiceRecordToCsvRowConverter {

    public String getHeaders() {
        return "buyer,image_name,invoice_image,invoice_due_date,invoice_number,invoice_amount,invoice_currency,invoice_status,supplier";
    }

    public String convert(InvoiceRecord record) {
        final StringBuilder sb = new StringBuilder();
        sb.append(record.getBuyer()).append(',')
          .append(record.getImageName()).append(',')
          .append(record.getInvoiceImage()).append(',')
          .append(record.getInvoiceDueDate()).append(',')
          .append(record.getInvoiceNumber()).append(',')
          .append(record.getInvoiceAmount()).append(',')
          .append(record.getInvoiceCurrency()).append(',')
          .append(record.getInvoiceStatus()).append(',')
          .append(record.getSupplier());
        return sb.toString();
    }
}
