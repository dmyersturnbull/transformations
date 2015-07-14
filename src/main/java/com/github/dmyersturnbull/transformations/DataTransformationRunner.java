package com.github.dmyersturnbull.transformations;

import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

/**
 * Runs a {@link DataTransformation} from stdin to stdout, from one file to another, or between files in a directory.
 * @author Douglas Myers-Turnbull
 */
public class DataTransformationRunner {

	private static final Logger sf_logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final Constructor<? extends DataTransformation> m_constructor;

	private CommandLineHelper m_helper;

	public DataTransformationRunner(@Nonnull Constructor<? extends DataTransformation> constructor) {
		m_constructor = constructor;
		m_helper = new CommandLineHelper();
	}

	@Nonnull
	public DataTransformationRunner addOptions(@Nonnull Option... additionalOptions) {
		m_helper.addOptions(additionalOptions);
		return this;
	}

	@Nonnull
	public DataTransformationRunner setHelpFooter(@Nullable String helpFooter) {
		m_helper.setHelpFooter(helpFooter);
		return this;
	}

	@Nonnull
	public DataTransformationRunner setHelpHeader(@Nullable String helpHeader) {
		m_helper.setHelpHeader(helpHeader);
		return this;
	}

	@Nonnull
	public DataTransformationRunner setClass(@Nullable Class<? extends DataTransformation> aClass) {
		m_helper.setClass(aClass);
		return this;
	}

	public void run(@Nonnull String... args) throws Exception {
		m_helper.addOptions(
				new Option("i", "input", true, "The input file"),
				new Option("o", "output", true, "The output file")
		);

		Optional<ExtendedCommandLine> response = m_helper.parse(args);
		if (response.isPresent()) {

			ExtendedCommandLine cli = response.get();

			Optional<File> input = cli.getFile("i");
			Optional<File> output = cli.getFile("o");

			if (input.isPresent() ^ output.isPresent()) {
				m_helper.printHelp();
			} else if (!input.isPresent()) {
				m_constructor.construct(cli).pipe();
			} else {
				m_constructor.construct(cli).apply(input.get(), output.get());
			}
		}

	}

	@FunctionalInterface
	public interface Constructor<T> {
		T construct(@Nonnull ExtendedCommandLine cli) throws Exception;
	}

}
