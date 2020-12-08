package com.taulia.devtask1.io.reader.converter.csv;

import com.taulia.devtask1.io.model.ExtendedInvoiceRecord;
import com.taulia.devtask1.io.shared.xml.XmlNodeNames;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class MapToExtendedInvoiceRecordConverter implements Function<Map<String, String>, ExtendedInvoiceRecord> {

    private MapToInvoiceRecordConverter invoiceRecordConverter;

    @Override
    public ExtendedInvoiceRecord apply(Map<String, String> map) {
        ExtendedInvoiceRecord extendedInvoiceRecord = new ExtendedInvoiceRecord();
        extendedInvoiceRecord.setInvoiceRecord(invoiceRecordConverter.apply(map));
        extendedInvoiceRecord.setInvoiceImageLocation(map.get(XmlNodeNames.ELEMENT_INVOICE_IMAGE_LOCATION));
        return extendedInvoiceRecord;
    }
}
