package com.taulia.devtask1.command;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.helper.IOHelper;
import com.taulia.devtask1.io.model.ExtendedInvoiceRecord;
import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.io.writer.converter.csv.InvoiceRecordToCsvRowConverter;
import com.taulia.devtask1.io.writer.converter.xml.ExtendedInvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.converter.xml.InvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.csv.CsvWriter;
import com.taulia.devtask1.io.writer.xml.ExtendedXmlWriter;
import com.taulia.devtask1.io.writer.xml.XmlWriter;
import com.taulia.devtask1.transformer.context.IOContext;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import com.taulia.devtask1.transformer.io.helper.TransformerInputReaderAdapter;
import com.taulia.devtask1.transformer.io.helper.TransformerOutputWriterAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class IOContextImpl<T> implements IOContext<T> {

    private final TransformerContext<T> context;
    private final IOHelper helper;

    public IOContextImpl(TransformerContext<T> context) {
        this.context = context;
        this.helper = new IOHelper();
    }

    @Override
    public TransformerInputReader<T> buildReader(File inputFile) throws IOException {
        final InputReader<? extends Object> inputReader = helper.buildReader(context.getCurrentSplit().getInputFile());
        final TransformerInputReaderAdapter<T> adapter = new TransformerInputReaderAdapter<>(inputReader, context.getWrapperFunction());
        return adapter;
    }

    @Override
    public TransformerOutputWriter<T> buildWriter(File outputFile) throws IOException {
        TransformerOutputWriter<T> toWriter = null;

        final String canonicalPath = outputFile.getCanonicalPath().toLowerCase(Locale.ROOT);

        if (canonicalPath.endsWith(".csv")) {
            final OutputWriter<InvoiceRecord> delegateWriter = new CsvWriter(outputFile, new InvoiceRecordToCsvRowConverter());
            toWriter = new TransformerOutputWriterAdapter<T, InvoiceRecord>(delegateWriter,
                                                                            context.getUnwrapperFunction().andThen(obj -> (InvoiceRecord) obj),
                                                                            context.prepareImageContext());
        }
        else if (canonicalPath.endsWith(".xml")) {
            final OutputWriter<InvoiceRecord> delegateWriter = new XmlWriter(outputFile, new InvoiceRecordToXmlElementConverter());
            toWriter = new TransformerOutputWriterAdapter<T, InvoiceRecord>(delegateWriter,
                    context.getUnwrapperFunction().andThen(obj -> (InvoiceRecord) obj),
                    context.prepareImageContext());
        }
        else if (canonicalPath.endsWith(".int-xml")) {
            final OutputWriter<ExtendedInvoiceRecord> delegateWriter = new ExtendedXmlWriter(outputFile, new ExtendedInvoiceRecordToXmlElementConverter());
            toWriter = new TransformerOutputWriterAdapter<T, ExtendedInvoiceRecord>(delegateWriter,
                    context.getUnwrapperFunction().andThen(obj -> (ExtendedInvoiceRecord) obj),
                    context.prepareImageContext());
        }
        else {
            throw new IllegalArgumentException("Only extensions like .csv and .xml and .i-xml are supported. " +
                    "Extension for file: " + canonicalPath + " is not one of them. " );
        }

        return toWriter;
    }
}
