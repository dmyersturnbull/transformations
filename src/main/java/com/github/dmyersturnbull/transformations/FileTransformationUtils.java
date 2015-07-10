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
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Static utilities.
 * @author Douglas Myers-Turnbull
 */
public class FileTransformationUtils {

	@Nonnull
	public static BufferedReader stdin() throws IOException {
		return new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8.name()), 65536);
	}

	@Nonnull
	public static PrintWriter stdout() throws IOException {
		return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out), 65536), false);
	}

	@Nonnull
	public static PrintWriter stderr() throws IOException {
		return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.err), 65536), false);
	}

	@Nonnull
	public static BufferedReader getFastReader(@Nonnull File file) throws IOException {
		return getFastReader(file, 65536, StandardCharsets.UTF_8.name());
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
		return getFastWriter(file, 65536, StandardCharsets.UTF_8.name());
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
