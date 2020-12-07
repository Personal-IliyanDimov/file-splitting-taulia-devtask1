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
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class TransformerContext {
    private Split currentSplit;
    private ArrayList<Split> splitList;
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

    public void addSplits(List<Split> splits) {
        config.getTraverPolicy().addSplits(this, splits);
    }

    public void rotateCurrentSplit() throws IOException {
        if (this.currentSplit.isDeleteInput()) {
            final boolean success = currentSplit.getInputFile().delete();
            if (! success) {
                throw new RuntimeException("Unable to delete current split file: " +  currentSplit.getInputFile().toString());
            }
        }
        this.splitList.remove(currentSplit);

        final Split nextSplit = config.getTraverPolicy().nextSplit(this);
        this.currentSplit = nextSplit;
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
