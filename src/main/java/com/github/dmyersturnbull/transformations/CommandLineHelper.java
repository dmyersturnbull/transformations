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
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

/**
 * A skeleton Apache Commons CLI handler.
 * @author Douglas Myers-Turnbull
 */
public class CommandLineHelper {

	private static final Logger sf_logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final int sf_helpWidth = 120;

	private Optional<Class<?>> m_class; // null is NOT ok; probably Class<? extends DataTransformation>
	private String m_helpHeader; // null is ok
	private String m_helpFooter; // null is ok
	private Options m_options = new Options();

	public CommandLineHelper() {
        m_class = Optional.empty();
		m_options.addOption("h", "help", false, "Print usage");
	}

	@Nonnull
	public CommandLineHelper setClass(@Nullable Class<?> aClass) {
		m_class = Optional.ofNullable(aClass);
		return this;
	}

	@Nonnull
	public CommandLineHelper setHelpHeader(@Nullable String helpHeader) {
		m_helpHeader = helpHeader;
		return this;
	}

	@Nonnull
	public CommandLineHelper setHelpFooter(@Nullable String helpFooter) {
		m_helpFooter = helpFooter;
		return this;
	}

	@Nonnull
	public CommandLineHelper addOptions(@Nonnull Option... additionalOptions) {
		for (Option option : additionalOptions) {
			m_options.addOption(option);
		}
		return this;
	}

	/**
	 *
	 * @return {@code Optional.empty()} if the command line should not be run, or the values parsed otherwise
	 */
	@Nonnull
	public Optional<ExtendedCommandLine> parse(@Nonnull String... args) {

		ExtendedCommandLine cli;

		try {

			CommandLine x = new DefaultParser().parse(m_options, args);
			cli = new ExtendedCommandLine(x);

		} catch (ParseException e) {
			sf_logger.error("Couldn't parse command-line arguments", e);
			printHelp(FileTransformationUtils.stderr());
			return Optional.empty();
		}

		if (cli.has("h")) {
			printHelp(FileTransformationUtils.stdout());
			return Optional.empty();
		} else {
			return Optional.of(cli);
		}
	}

	public void printHelp() {
		printHelp(FileTransformationUtils.stderr());
	}

	public void printHelp(@Nonnull PrintWriter pw) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(pw, sf_helpWidth, m_class.map(Class::getSimpleName).orElse("<unknown class>"),
		                    m_helpHeader, m_options, 0, 0, m_helpFooter, true);
        pw.flush();
	}

}
