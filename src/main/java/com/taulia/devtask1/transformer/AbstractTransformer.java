package com.taulia.devtask1.transformer;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.io.reader.CsvReader;
import com.taulia.devtask1.io.reader.XmlReader;
import com.taulia.devtask1.io.reader.converter.CsvRowToInvoiceRecordConverter;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public abstract class AbstractTransformer implements Transformer {

    @Override
    public TransformerContext transform(TransformerContext context) throws Exception {
        final TransformerConsumer transformerConsumer = getConsumer(context);

        final File inputFile = context.getInputFile();
        final InputReader<InvoiceRecord> inputReader = findReader(inputFile);
        inputReader.process(transformerConsumer.getRecordsConsumer());
        transformerConsumer.process();

        return getNextContext();
    }

    private InputReader<InvoiceRecord> findReader(File inputFile) throws IOException {
        InputReader<InvoiceRecord> result;
        final String cannonicalPath = inputFile.getCanonicalPath().toLowerCase(Locale.ROOT);
        if (cannonicalPath.endsWith(".csv")) {
            result = new CsvReader(inputFile, new CsvRowToInvoiceRecordConverter());
        }
        else if (cannonicalPath.endsWith(".xml")) {
            result = new XmlReader(inputFile);
        }
        else {
            throw new IllegalArgumentException("Only .csv and .xml are supported. File: " + cannonicalPath + " is not one of them. " );
        }

        return result;
    }

    protected abstract TransformerConsumer getConsumer(TransformerContext context);

    protected abstract TransformerContext getNextContext();
}
