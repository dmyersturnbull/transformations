package com.github.dmyersturnbull.transformations;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Static utilities.
 * @author Douglas Myers-Turnbull
 */
public class FileTransformationUtils {

	private static final int sf_defaultBufferSize = 65536;

	@Nonnull
	public static BufferedReader stdin() {
		try {
			return new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8.name()), sf_defaultBufferSize);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	@Nonnull
	public static PrintWriter stdout() {
		try {
			return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8.name()), sf_defaultBufferSize), false);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	@Nonnull
	public static PrintWriter stderr() {
		try {
			return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.err, StandardCharsets.UTF_8.name()), sf_defaultBufferSize), false);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	@Nonnull
	public static BufferedReader getFastReader(@Nonnull File file) throws IOException {
		return getFastReader(file, sf_defaultBufferSize, StandardCharsets.UTF_8.name());
	}

	@Nonnull
	public static BufferedReader getFastReader(@Nonnull File file, int cacheSize) throws IOException {
		return getFastReader(file, cacheSize, StandardCharsets.UTF_8.name());
	}

	@Nonnull
	public static BufferedReader getFastReader(@Nonnull File file, int cacheSize, @Nonnull String encoding) throws IOException {
		if (file.getPath().endsWith("gz") || file.getPath().endsWith("gzip")) {
			return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file), cacheSize), encoding));
		} else {
			return new BufferedReader(new FileReader(file), cacheSize);
		}
	}

	@Nonnull
	public static PrintWriter getFastWriter(File file) throws IOException {
		return getFastWriter(file, sf_defaultBufferSize, StandardCharsets.UTF_8.name());
	}

	@Nonnull
	public static PrintWriter getFastWriter(File file, int cacheSize) throws IOException {
		return getFastWriter(file, cacheSize, StandardCharsets.UTF_8.name());
	}

	@Nonnull
	public static PrintWriter getFastWriter(File file, int cacheSize, @Nonnull String encoding) throws IOException {
		if (file.getPath().endsWith(".gz") || file.getPath().endsWith(".gzip")) {
			return new PrintWriter(new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)), encoding), cacheSize), false);
		} else {
			return new PrintWriter(new BufferedWriter(new FileWriter(file), cacheSize), false);
		}
	}

}
