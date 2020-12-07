package com.taulia.devtask1;

import com.taulia.devtask1.transformer.helper.TransformerContext;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class CommandRunner {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("At least two arguments are expected .");
			return ;
		}

		final String inputFileAsString = args[0];
		final String outputFolderAsString = args[1];

		final File inputFile = new File(inputFileAsString);
		if (! inputFile.exists()) {
			System.out.println("Input file does not exist: " + inputFileAsString);
			return ;
		}

		TransformerContext.OutputType outputType = null;
		if (inputFile.getCanonicalPath().toLowerCase(Locale.ROOT).endsWith(".csv")) {
			outputType = TransformerContext.OutputType.CSV;
		}
		else if (inputFile.getCanonicalPath().toLowerCase(Locale.ROOT).endsWith(".xml")) {
			outputType = TransformerContext.OutputType.XML;
		}
		else {
			System.out.println("Input file must be of type .csv or .xml: " + inputFileAsString);
		}

		final File outputFolder = new File(outputFolderAsString);
		if ( (!outputFolder.exists()) || (! outputFolder.isDirectory())) {
			System.out.println("Output folder does not exist or is not a folder: " + outputFolderAsString);
			return ;
		}

		final TransformCommand command = new TransformCommand();
		command.executeCommand(inputFile, outputFolder, outputType);
	}

}
