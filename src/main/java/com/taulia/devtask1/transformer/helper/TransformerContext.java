package com.taulia.devtask1.transformer.helper;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToCsvRowConverter;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.CsvWriter;
import com.taulia.devtask1.io.writer.XmlWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Getter
@Setter
public class TransformerContext {
    private File inputFile;
    private File outputFolder;
    private OutputType outputType;
    private String outputBuyerPrefix;
    private Long outputBuyerIndex;
    private String outputOtherPrefix;
    private Long outputOtherIndex;
    private TransformerConfig config;


    public FileContext nextBuyerContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputBuyerPrefix);
        fileContext.setOutputIndex(outputBuyerIndex ++);
        return fileContext;
    }


    public static enum OutputType {
        CSV,
        XML
    }

    @Getter
    @Setter
    @ToString
    public static class FileContext {
        private File outputFolder;
        private String outputPrefix;
        private Long outputIndex;
        private OutputType outputType;
        private OutputWriter<InvoiceRecord> outputWriter;

        public OutputWriter<InvoiceRecord> getOrCreateOutputWriter() throws IOException {
            OutputWriter<InvoiceRecord> outputWriter = getOutputWriter();
            if (Objects.isNull(outputWriter)) {
                switch (outputType) {
                    case CSV:
                        final File outputCsvFile = new File(outputFolder, outputPrefix + "-" + outputIndex + ".csv");
                        final CsvWriter csvWriter = new CsvWriter(outputCsvFile, new InvoiceRecordToCsvRowConverter());
                        outputWriter = csvWriter;
                        break;

                    case XML:
                        final File outputXmlFile = new File(outputFolder, outputPrefix + "-" + outputIndex + ".xml");
                        final XmlWriter xmlWriter = new XmlWriter(outputXmlFile, new InvoiceRecordToXmlElementConverter());
                        outputWriter = xmlWriter;
                        break;
                }
            }
            return outputWriter;
        }
    }
}
