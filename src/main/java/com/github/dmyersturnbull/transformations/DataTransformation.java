/*
   Copyright 2015 PharmGKB
             2015 Douglas Myers-Turnbull

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.github.dmyersturnbull.transformations;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A transformation between files or streams of the same data type, with one input stream/file to each output stream/file.
 * Using {@link FileTransformationApplier}, can apply a transformation to every file in a directory.
 * Can also pipe from stdin to stdout.
 *
 * Example implementation:
 * @{code
 *  public class X implements DataTransformation, Function&lt;String, String&gt; {
 *      public static void main(String[] args) {
 *          new CommandLineRunner(cli -> .
 *      }
 *      {@literal}Override
 *      public void apply({@literal}Nonnull BufferedReader br, {@literal}Nonnull PrintWriter pw) {
 *          br.lines().map(this).forEach(pw::println);
 *      }
 *      public void apply({@literal}Nonnull line) {
 *          return line + "!";
 *      }
 *  }
 * }
 *
 * @author Douglas Myers-Turnbull
 */
public interface DataTransformation {

	/**
	 * @return The files that the transformation should can applied to.
	 * For example, ".vcf" and ".vfc.gz". {@link #apply(File, File)} will still operate on directories even if they are excluded here.
	 */
	@Nonnull
	FileFilter getApplicableFiles();

	/**
	 * Pipes from stdin to stdout, uncompressed.
	 * This should not be overridden.
	 */
	default void pipe() throws IOException {
        PrintWriter out = FileTransformationUtils.stdout();
		apply(FileTransformationUtils.stdin(), out);
        out.flush();

	}

	/**
	 * Applies the transformation to a single file if input and output are both files, or to every file in the input directory if both are directories.
	 * @see {@link FileTransformationApplier#applyToAll(File, File)}
	 */
	default void apply(@Nonnull File input, @Nonnull File output) throws IOException {
		new FileTransformationApplier(this, getApplicableFiles()).applyToAll(input, output);
	}

	void apply(@Nonnull BufferedReader br, @Nonnull PrintWriter pw) throws IOException;

}
