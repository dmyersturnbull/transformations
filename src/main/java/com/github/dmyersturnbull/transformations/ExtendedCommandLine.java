package com.github.dmyersturnbull.transformations;

import org.apache.commons.cli.CommandLine;

import javax.annotation.Nonnull;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

/**
 * A more modern {@link CommandLine} that uses {@link Optional Optionals} and has some simple converters.
 * @author Douglas Myers-Turnbull
 */
public class ExtendedCommandLine {

	private final CommandLine m_commandLine;

	public ExtendedCommandLine(@Nonnull CommandLine commandLine) {
		m_commandLine = commandLine;
	}

	public boolean has(@Nonnull String opt) {
		return m_commandLine.hasOption(opt);
	}

	public boolean has(char opt) {
		return m_commandLine.hasOption(opt);
	}

	@Nonnull
	public Optional<String> get(@Nonnull String opt) {
		return Optional.ofNullable(m_commandLine.getOptionValue(opt));
	}

	@Nonnull
	public Optional<String> get(char opt) {
		return Optional.ofNullable(m_commandLine.getOptionValue(opt));
	}

	@Nonnull
	public Properties getProperties(@Nonnull String opt) {
		return m_commandLine.getOptionProperties(opt);
	}

	@Nonnull
	public Optional<Integer> getInt(@Nonnull String opt) {
		return get(opt).map(Integer::new);
	}

	@Nonnull
	public Optional<Long> getLong(@Nonnull String opt) {
		return get(opt).map(Long::new);
	}

	@Nonnull
	public Optional<Double> getDouble(@Nonnull String opt) {
		return get(opt).map(Double::new);
	}

	@Nonnull
	public Optional<BigDecimal> getBigDecimal(@Nonnull String opt) {
		return get(opt).map(BigDecimal::new);
	}

	@Nonnull
	public Optional<BigInteger> getBigInteger(@Nonnull String opt) {
		return get(opt).map(BigInteger::new);
	}

	@Nonnull
	public Optional<File> getFile(@Nonnull String opt) {
		return get(opt).map(File::new);
	}

	@Nonnull
	public Optional<Path> getPath(@Nonnull String opt) {
		return get(opt).map(Paths::get);
	}

	@Nonnull
	public Optional<URI> getUri(@Nonnull String opt) throws URISyntaxException {
		return get(opt).map((str) -> {
			try {
				return new URI(str);
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Option " + opt + " is not a valid URI", e);
			}
		});
	}

	@Nonnull
	public Optional<URL> getUrl(@Nonnull String opt) throws MalformedURLException {
		return get(opt).map((str) -> {
			try {
				return new URL(str);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Option " + opt + " is not a valid URL", e);
			}
		});
	}

}
