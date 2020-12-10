package com.taulia.devtask1.transformer.consumer.io;

import com.taulia.devtask1.transformer.context.FileContext;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.File;

@RequiredArgsConstructor
@Getter
@ToString
public class OutputInfo<T> {
    private final File outputFile;
    private final TransformerOutputWriter<T> outputWriter;

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
