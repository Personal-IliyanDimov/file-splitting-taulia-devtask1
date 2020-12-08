package com.taulia.devtask1.io.reader.converter.csv;

import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.shared.xml.XmlNodeNames;

import java.util.Map;
import java.util.function.Function;

public class MapToInvoiceRecordConverter implements Function<Map<String, String>, InvoiceRecord> {
    @Override
    public InvoiceRecord apply(Map<String, String> map) {
        InvoiceRecord record = new InvoiceRecord();
        record.setBuyer(map.get(XmlNodeNames.ELEMENT_BUYER));
        record.setImageName(map.get(XmlNodeNames.ELEMENT_IMAGE_NAME));
        record.setInvoiceImage(map.get(XmlNodeNames.ELEMENT_INVOICE_IMAGE));
        record.setInvoiceDueDate(map.get(XmlNodeNames.ELEMENT_INVOICE_DUE_DATE));
        record.setInvoiceNumber(map.get(XmlNodeNames.ELEMENT_INVOICE_NUMBER));
        record.setInvoiceAmount(map.get(XmlNodeNames.ELEMENT_INVOICE_AMOUNT));
        record.setInvoiceCurrency(map.get(XmlNodeNames.ELEMENT_INVOICE_CURRENCY));
        record.setInvoiceStatus(map.get(XmlNodeNames.ELEMENT_INVOICE_STATUS));
        record.setSupplier(map.get(XmlNodeNames.ELEMENT_SUPPLIER));

        return record;
    }
}
