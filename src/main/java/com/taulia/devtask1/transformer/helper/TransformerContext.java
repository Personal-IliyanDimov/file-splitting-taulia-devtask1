package com.taulia.devtask1.transformer.helper;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToCsvRowConverter;
import com.taulia.devtask1.io.reader.converter.InvoiceRecordToXmlElementConverter;
import com.taulia.devtask1.io.writer.CsvWriter;
import com.taulia.devtask1.io.writer.XmlWriter;
import com.taulia.devtask1.transformer.splitter.Split;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
public class TransformerContext {
    private Split currentSplit;
    private ArrayList<Split> splitArray;
    private File outputFolder;
    private OutputType outputType;
    private String outputSplitPrefix;
    private long outputSplitIndex;
    private String outputBuyerPrefix;
    private long outputBuyerIndex;
    private String outputOtherPrefix;
    private long outputOtherIndex;
    private String imagePrefix;
    private long imageIndex;

    private File nextInputFile;
    private File oldNextInputFile;

    private TransformerConfig config;

    public FileContext nextSplitContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputSplitPrefix);
        fileContext.setOutputIndex(outputSplitIndex ++);
        return fileContext;
    }

    public FileContext nextBuyerContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputBuyerPrefix);
        fileContext.setOutputIndex(outputBuyerIndex ++);
        return fileContext;
    }

    public FileContext nextOtherContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputOtherPrefix);
        fileContext.setOutputIndex(outputOtherIndex ++);
        return fileContext;
    }

    public File nextImageFile() {
        final long currentImageIndex = imageIndex ++;
        return new File(outputFolder, imagePrefix + "-" + currentImageIndex + ".img");

    }

    public TransformerContext copy() {
        final TransformerContext other = new TransformerContext();
        other.currentSplit = this.currentSplit;
        other.splitArray = this.splitArray;
        other.outputFolder = this.outputFolder;
        other.outputType = this.outputType;
        other.outputBuyerPrefix = this.outputBuyerPrefix;
        other.outputBuyerIndex = this.outputBuyerIndex;
        other.outputOtherPrefix = this.outputOtherPrefix;
        other.outputOtherIndex = this.outputOtherIndex;
        other.imagePrefix = this.imagePrefix;
        other.imageIndex = this.imageIndex;
        other.nextInputFile = this.nextInputFile;
        other.oldNextInputFile = this.oldNextInputFile;
        return other;
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
        private long outputIndex;
        private OutputType outputType;
        private File outputFile;
        private OutputWriter<InvoiceRecord> outputWriter;

        public OutputWriter<InvoiceRecord> getOrCreateOutputWriter() throws IOException {
            if (Objects.isNull(this.outputWriter)) {
                switch (outputType) {
                    case CSV:
                        this.outputFile = new File(outputFolder, outputPrefix + "-" + outputIndex + ".csv");
                        final CsvWriter csvWriter = new CsvWriter(outputFile, new InvoiceRecordToCsvRowConverter());
                        this.outputWriter = csvWriter;
                        break;

                    case XML:
                        this.outputFile = new File(outputFolder, outputPrefix + "-" + outputIndex + ".xml");
                        final XmlWriter xmlWriter = new XmlWriter(outputFile, new InvoiceRecordToXmlElementConverter());
                        this.outputWriter = xmlWriter;
                        break;
                }
            }
            return this.outputWriter;
        }
    }
}
