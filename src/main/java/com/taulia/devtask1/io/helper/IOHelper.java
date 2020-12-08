package com.taulia.devtask1.io.helper;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.reader.converter.csv.CsvRowToInvoiceRecordConverter;
import com.taulia.devtask1.io.reader.converter.csv.MapToExtendedInvoiceRecordConverter;
import com.taulia.devtask1.io.reader.converter.csv.MapToInvoiceRecordConverter;
import com.taulia.devtask1.io.reader.csv.CsvReader;
import com.taulia.devtask1.io.reader.xml.ExtendedXmlReader;
import com.taulia.devtask1.io.reader.xml.XmlReader;
import com.taulia.devtask1.io.writer.converter.csv.InvoiceRecordToCsvRowConverter;
import com.taulia.devtask1.io.writer.converter.xml.ExtendedInvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.converter.xml.InvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.csv.CsvWriter;
import com.taulia.devtask1.io.writer.xml.ExtendedXmlWriter;
import com.taulia.devtask1.io.writer.xml.XmlWriter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class IOHelper {

    public InputReader<?> buildReader(File inputFile) throws IOException {
        InputReader<?> result;
        final String canonicalPath = inputFile.getCanonicalPath().toLowerCase(Locale.ROOT);

        if (canonicalPath.endsWith(".csv")) {
            result = new CsvReader(inputFile, new CsvRowToInvoiceRecordConverter());
        }
        else if (canonicalPath.endsWith(".xml")) {
            result = new XmlReader(inputFile, new MapToInvoiceRecordConverter());
        }
        else if (canonicalPath.endsWith(".extended-xml")) {
            result = new ExtendedXmlReader(inputFile, new MapToExtendedInvoiceRecordConverter());
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
            result = new ExtendedXmlWriter(inputFile, new ExtendedInvoiceRecordToXmlElementConverter());
        }
        else {
            throw new IllegalArgumentException("Only extensions like .csv and .xml and .i-xml are supported. " +
                    "Extension for file: " + canonicalPath + " is not one of them. " );
        }

        return result;
    }
}
