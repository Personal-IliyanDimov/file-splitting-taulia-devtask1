package com.taulia.devtask1;

import com.taulia.devtask1.transformer.CompositeTransformer;
import com.taulia.devtask1.transformer.Transformer;
import com.taulia.devtask1.transformer.disk.DiskTransformer;
import com.taulia.devtask1.transformer.helper.TransformerConfig;
import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.memory.InMemoryTransformer;
import com.taulia.devtask1.transformer.strategy.StrategySelector;

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

	public static class TransformCommand {

		private CompositeTransformer compositeTransformer;

		public TransformCommand() {
			final StrategySelector strategySelector = new StrategySelector();
			final Transformer diskTransformer = new DiskTransformer();
			final Transformer inMemoryTransformer = new InMemoryTransformer();
			compositeTransformer = new CompositeTransformer(strategySelector, diskTransformer, inMemoryTransformer);
		}

		public void executeCommand(File inputFile, File outputFolder, TransformerContext.OutputType outputType) {
			final TransformerConfig config = prepareTransformerConfig();
			final TransformerContext context = getTransformerContext(inputFile, outputFolder, outputType, config);

			try {
				compositeTransformer.transform(context);
			}
			catch (Exception exc) {
				cleanUp(context);
			}
		}

		private void cleanUp(TransformerContext context) {
			// clean up output folder
		}

		private TransformerConfig prepareTransformerConfig() {
			TransformerConfig config = new TransformerConfig();
			config.setMaxOpenHandlers(2);
			config.setMaxInMemoryFileSizeInBytes(512*1024);
			return config;
		}

		private TransformerContext getTransformerContext(File inputFile, File outputFolder, TransformerContext.OutputType outputType, TransformerConfig config) {
			final TransformerContext context = new TransformerContext();
			context.setInputFile(inputFile);
			context.setOutputFolder(outputFolder);
			context.setOutputType(outputType);
			context.setOutputBuyerPrefix("buyer");
			context.setOutputBuyerIndex(0L);
			context.setOutputOtherPrefix("other");
			context.setOutputOtherIndex(0L);
			context.setImagePrefix("image");
			context.setImageIndex(0L);
			context.setNextInputFile(null);
			context.setOldNextInputFile(null);
			context.setConfig(config);
			return context;
		}
	}

	private TransformerConfig config;

}
