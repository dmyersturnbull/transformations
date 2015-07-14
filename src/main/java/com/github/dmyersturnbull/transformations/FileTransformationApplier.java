package com.github.dmyersturnbull.transformations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Static utilities to apply a {@link DataTransformation} to every file in a directory.
 * Note that this results in a cyclic dependency between this class and DataTransformation. This is unavoidable because
 * FileTransformation is an interface, but it's not a significant problem as long as this class doesn't grow.
 * @author Douglas Myers-Turnbull
 */
public class FileTransformationApplier {

	private static final Logger sf_logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final DataTransformation m_transformation;

	private final FileFilter m_filter;

	public FileTransformationApplier(@Nonnull DataTransformation transformation, @Nonnull FileFilter filter) {
		m_transformation = transformation;
		m_filter = filter;
	}

	/**
	 * Applies the transformation to a single file if input and output are both files, or to every file in the input directory if both are directories.
	 */
	public final void applyToAll(@Nonnull File input, @Nonnull File output) throws IOException {
		if (input.isDirectory() && output.isDirectory()) {
			for (File in : input.listFiles(m_filter)) {
				if (!in.isDirectory()) { // never apply to directories
					File out = new File(output + File.separator + in.getName());
					applyToFile(in, out);
				}
			}
		} else if (input.isDirectory() ^ output.isDirectory()) {
			throw new IOException(input.getPath() + " and " + output.getPath() + " must both or neither be directories");
		} else { // let it throw an exception if needed
			applyToFile(input, output);
		}
	}

	public void applyToFile(@Nonnull File input, @Nonnull File output) throws IOException {
		try {
			sf_logger.info("Transforming {} to {}", input.getPath(), output.getPath());
			// note that the output will be gzipped if its filename extension calls for it
			// similarly, the input will be read as gzip if its filename extension calls for it
			m_transformation.apply(FileTransformationUtils.getFastReader(input), FileTransformationUtils.getFastWriter(output));
			sf_logger.info("Done transforming {} to {}", input.getPath(), output.getPath());
		} catch (RuntimeException e) {
			throw new RuntimeException("Error while transforming file " + input.getPath() + " to " + output.getPath(), e);
		}
	}

	@Nonnull
	protected DataTransformation getTransformation() {
		return m_transformation;
	}
}
