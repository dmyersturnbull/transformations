package com.github.dmyersturnbull.transformations;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * A transformation between files or streams of the same data type, with one input stream/file to each output stream/file.
 * Using {@link FileTransformationApplier}, can apply a transformation to every file in a directory.
 * Can also pipe from stdin to stdout.
 * @author Douglas Myers-Turnbull
 */
public interface DataTransformation extends Function<String, String> {

	/**
	 * @return The files that the transformation should can applied to.
	 * For example, ".vcf" and ".vfc.gz". {@link #apply(File, File)} will still operate on directories even if they are excluded here.
	 */
	@Nonnull
	FileFilter getApplicableFiles();

	/**
	 * Pipes from stdin to stdout, uncompressed.
	 * This should not be overriden.
	 */
	default void pipe() throws IOException {
		apply(FileTransformationUtils.stdin(), FileTransformationUtils.stdout());
	}

	default void apply(@Nonnull Path input, @Nonnull Path output) throws IOException {
		apply(input.toFile(), output.toFile());
	}

	/**
	 * Applies the transformation to a single file if input and output are both files, or to every file in the input directory if both are directories.
	 * @see {@link FileTransformationApplier#applyToAll(File, File)}
	 */
	default void apply(@Nonnull File input, @Nonnull File output) throws IOException {
		new FileTransformationApplier(this, getApplicableFiles()).applyToAll(input, output);
	}

	default void apply(@Nonnull BufferedReader br, @Nonnull PrintWriter pw) throws IOException {
		br.lines().map(this).forEach(pw::println);
	}

}
