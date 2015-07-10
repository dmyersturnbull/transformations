package com.github.dmyersturnbull.transformations;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runs a {@link DataTransformation} from stdin to stdout, from one file to another, or between files in a directory.
 * @author Douglas Myers-Turnbull
 */
public class CommandLineRunner {

	private static final Logger sf_logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final Constructor<? extends DataTransformation> m_constructor;
	private String[] m_args = {};
	private List<Option> m_additionalOptions = new ArrayList<>();

	public CommandLineRunner(@Nonnull Constructor<? extends DataTransformation> constructor) {
		m_constructor = constructor;
	}

	public CommandLineRunner setArgs(String... args) {
		m_args = args;
		return this;
	}

	public CommandLineRunner addOptions(Option... additionalOptions) {
		m_additionalOptions.addAll(Arrays.asList(additionalOptions));
		return this;
	}

	public void run() throws Exception {
		Options options = new Options();
		options.addOption("i", "input", false, "The input VCF file");
		options.addOption("o", "output", false, "The output VCF file");
		m_additionalOptions.forEach(options::addOption);
		CommandLine cli = new DefaultParser().parse(options, m_args);
		try {
			@Nullable File input = cli.hasOption("i")? new File(cli.getOptionValue("i")) : null;
			@Nullable File output = cli.hasOption("o")? new File(cli.getOptionValue("o")) : null;
			if (input == null ^ output == null) {
				printHelp(options, FileTransformationUtils.stderr());
			} else if (input == null) {
				m_constructor.construct(cli).pipe();
			} else {
				m_constructor.construct(cli).apply(input, output);
			}
		} catch (ParseException e) {
			sf_logger.error("Couldn't parse command-line arguments", e);
			printHelp(options, FileTransformationUtils.stderr());
		}
	}

	private void printHelp(Options options, PrintWriter pw) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printUsage(pw, 120, "", options);
	}

	@FunctionalInterface
	protected interface Constructor<T> {
		T construct(@Nonnull CommandLine cli) throws Exception;
	}

}
