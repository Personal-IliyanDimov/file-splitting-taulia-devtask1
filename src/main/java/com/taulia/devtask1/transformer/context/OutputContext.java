package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import com.taulia.devtask1.transformer.io.helper.TransformerIOHelper;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class OutputContext<T> {
    private File outputFile;
    private TransformerOutputWriter<T> outputWriter;

    public TransformerOutputWriter<T> getOrCreateOutputWriter() throws IOException {
        final TransformerIOHelper helper = new TransformerIOHelper();
        return helper.buildWriter(buildOutputFile(fileContext), fileContext);
    }

    private File buildOutputFile(FileContext fileContext) {
        return null;
    }

//
//    public <T> TransformerOutputWriter<T> getOrCreateOutputWriter() throws IOException {
//        if (Objects.isNull(this.outputWriter)) {
//            switch (outputType) {
//                case CSV:
//                    this.outputFile = new File(outputFolder, outputPrefix + "-" + outputIndex + ".csv");
//                    final CsvWriter csvWriter = new CsvWriter(outputFile, new InvoiceRecordToCsvRowConverter());
//                    this.outputWriter = csvWriter;
//                    break;
//
//                case XML:
//                    this.outputFile = new File(outputFolder, outputPrefix + "-" + outputIndex + ".xml");
//                    final XmlWriter xmlWriter = new XmlWriter(outputFile, new InvoiceRecordToXmlElementConverter());
//                    this.outputWriter = xmlWriter;
//                    break;
//            }
//        }
//        this.outputWriter;
//
//        return null;
//    }

}
