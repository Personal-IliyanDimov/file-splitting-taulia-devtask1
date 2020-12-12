package com.taulia.devtask1;

import com.taulia.devtask1.command.TransformCommand;
import com.taulia.devtask1.io.model.ExtendedInvoiceRecord;
import com.taulia.devtask1.io.model.InvoiceRecord;
import com.taulia.devtask1.transformer.context.GenericContext;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.io.model.TransformedItem;

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

		final long startTimeInMillis = System.currentTimeMillis();

		final TransformCommand command = new InvoiceTransformerCommand();
		command.executeCommand(inputFile, outputFolder, outputType);

		System.out.println("Execution takes(ms): " + (System.currentTimeMillis() - startTimeInMillis));
	}

	protected static class InvoiceTransformerCommand extends TransformCommand<TransformedItem<?>> {
		@Override
		protected GenericContext<TransformedItem<?>> buildGenericContext() {
			return new GenericContext<TransformedItem<?>>(
				o -> new TransformedItem<Object>(o),
				ti -> ti.getPayload(),
				ti -> {
					final Object payload = ti.getPayload();
					if (payload instanceof InvoiceRecord) {
						return ((InvoiceRecord) payload).getBuyer();
					}
					else if (payload instanceof ExtendedInvoiceRecord) {
						return ((ExtendedInvoiceRecord) payload).getInvoiceRecord().getBuyer();
					} else {
						throw new RuntimeException("Unknown payload type !!!");
					}
				},
				ti -> {
					final Object payload = ti.getPayload();
					if (payload instanceof InvoiceRecord) {
						return ((InvoiceRecord) payload).getBuyer();
					}
					else if (payload instanceof ExtendedInvoiceRecord) {
						return ((ExtendedInvoiceRecord) payload).getInvoiceRecord().getBuyer();
					} else {
						throw new RuntimeException("Unknown payload type !!!");
					}
				},
				fc -> {
					File file = null;
					switch (fc.getOutputType()) {
						case CSV:
							file = new File(fc.getOutputFolder(), fc.getOutputPrefix() + "-" + fc.getOutputIndex() + ".csv");
							break;

						case XML:
							file = new File(fc.getOutputFolder(), fc.getOutputPrefix() + "-" + fc.getOutputIndex() + ".xml");
							break;

						case INTXML:
							file = new File(fc.getOutputFolder(), fc.getOutputPrefix() + "-" + fc.getOutputIndex() + ".int-xml");
							break;
					}
					return file;
				}
			);
		}
	}
}
