package com.taulia.devtask1.helper;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.writer.converter.csv.InvoiceRecordToCsvRowConverter;
import com.taulia.devtask1.io.writer.csv.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

public class FileGenerator {

    private static final long DISTINCT_BUYERS_COUNT = 2000;
    private static final long RECORDS_PER_BUYER_COUNT = 10;

    public void generate(File outputFile) throws IOException {
        OutputWriter writer = null;

        if (outputFile.getCanonicalPath().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            writer = new CsvWriter(outputFile, new InvoiceRecordToCsvRowConverter());
        }
        else if (outputFile.getCanonicalPath().toLowerCase(Locale.ROOT).endsWith(".xml")) {
            writer = new StandardXmlWriter(outputFile);
        }
        else {
            throw new IllegalArgumentException("Unknown file extension !!!");
        }


        writer.init();
        try {
            for (long i = 0; i < DISTINCT_BUYERS_COUNT; i ++) {
                final InvoiceRecord record = new InvoiceRecord();
                record.setBuyer("buyer" + i);
                record.setImageName("image-number-" + i);
                record.setInvoiceImage("image-" + String.valueOf(i).repeat(100*1024));
                record.setInvoiceAmount(String.valueOf(1));
                record.setInvoiceCurrency("currency" + i);
                record.setInvoiceNumber("invoice-number-" + i);
                record.setInvoiceDueDate(LocalDate.now().toString());
                record.setInvoiceStatus("status-" + i);
                record.setSupplier("supplier-" + i);

                for (long j = 0; j < RECORDS_PER_BUYER_COUNT; j ++) {
                    writer.process(record, null);
                }
            }

            writer.end();
        }
        finally {
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("At least single argument is expected .");
            return ;
        }

        final String outputFileinputFileAsString = args[0];
        final File outputFile = new File(outputFileinputFileAsString);
        if (outputFile.exists()) {
            System.out.println("Output file already exists: " + outputFileinputFileAsString);
            return ;
        }

        new FileGenerator().generate(outputFile);
    }
}
