package com.taulia.devtask1.io.helper;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.reader.CsvReader;
import com.taulia.devtask1.io.reader.XmlReader;
import com.taulia.devtask1.io.reader.converter.csv.CsvRowToInvoiceRecordConverter;
import com.taulia.devtask1.io.reader.converter.csv.InvoiceRecordToCsvRowConverter;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.CsvWriter;
import com.taulia.devtask1.io.writer.XmlWriter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class IOHelper {

    public InputReader<?> buildReader(File inputFile) throws IOException {
        InputReader<InvoiceRecord> result;
        final String canonicalPath = inputFile.getCanonicalPath().toLowerCase(Locale.ROOT);

        if (canonicalPath.endsWith(".csv")) {
            result = new CsvReader(inputFile, new CsvRowToInvoiceRecordConverter());
        }
        else if (canonicalPath.endsWith(".xml")) {
            result = new XmlReader(inputFile);
        }
        else if (canonicalPath.endsWith(".i-xml")) {
            result = new XmlReader(inputFile);
        }
        else {
            throw new IllegalArgumentException("Only extensions like .csv and .xml and .i-xml are supported. " +
                                               "Extension for file: " + canonicalPath + " is not one of them. " );
        }

        return result;
    }

    public OutputWriter<?> buildWriter(File inputFile) throws IOException {
        OutputWriter<?> result;
        final String canonicalPath = inputFile.getCanonicalPath().toLowerCase(Locale.ROOT);

        if (canonicalPath.endsWith(".csv")) {
            result = new CsvWriter(inputFile, new InvoiceRecordToCsvRowConverter());
        }
        else if (canonicalPath.endsWith(".xml")) {
            result = new XmlWriter(inputFile, new InvoiceRecordToXmlElementConverter());
        }
        else if (canonicalPath.endsWith(".int-xml")) {
            result = new XmlWriter(inputFile, new InvoiceRecordToXmlElementConverter());
        }
        else {
            throw new IllegalArgumentException("Only extensions like .csv and .xml and .i-xml are supported. " +
                    "Extension for file: " + canonicalPath + " is not one of them. " );
        }

        return result;
    }
}
