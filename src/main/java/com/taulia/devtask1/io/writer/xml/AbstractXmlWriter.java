package com.taulia.devtask1.io.writer.xml;

import com.taulia.devtask1.io.OutputWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public abstract class AbstractXmlWriter<T> implements OutputWriter<T> {

    private static final String START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String ELEMENT_ROOT_START = "<root>";
    private static final String ELEMENT_ROOT_END = "</root>";

    private final File outputFile;
    private Writer writer;

    public AbstractXmlWriter(File outputFile) {
        this.outputFile = outputFile;
    }


    @Override
    public void init() throws IOException {
        if (! outputFile.createNewFile()) {
            throw new IllegalStateException("File name already exists:" + outputFile.toString());
        }

        writer = new PrintWriter(outputFile);
        writer.write(START + System.lineSeparator());
        writer.write(ELEMENT_ROOT_START + System.lineSeparator());
    }

    @Override
    public void end() throws IOException {
        writer.write(ELEMENT_ROOT_END);
    }

    @Override
    public void close() throws IOException {
        if (writer == null) {
            return ;
        }
        writer.close();
    }

    protected Writer getWriter() {
        return writer;
    }
}
