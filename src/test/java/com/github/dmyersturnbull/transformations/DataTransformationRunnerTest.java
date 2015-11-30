package com.github.dmyersturnbull.transformations;

import com.google.common.io.Files;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Tests {@link DataTransformationRunner}.
 * @author Douglas Myers-Turnbull
 */
public class DataTransformationRunnerTest {

    @Test
    public void testRunOnFile() throws Exception {

        File input = Paths.get(getClass().getResource("my_yes.txt").toURI()).toFile();
        File output = File.createTempFile("test_output1", "txt");

        new DataTransformationRunner(cli -> new DT())
                .run("-i", input.getPath(), "-o", output.getPath());
        assertTrue(output.exists());
        assertEquals(Files.readLines(input, Charset.defaultCharset()), Files.readLines(output, Charset.defaultCharset()));

    }

    @Test(expected = Exception.class)
    public void testFail() throws Exception {
        new DataTransformationRunner(cli -> new DT())
                .run("-x", "t");

    }

    @Test
    public void testRunOnPipe() throws Exception {

        DT dt = new DT();
        new DataTransformationRunner(cli -> dt)
                .run();
        assertTrue(dt.m_piped);

    }

    @Test
    public void testRunOnDir() throws Exception {

        //noinspection ResultOfMethodCallIgnored
        File inputs = Paths.get(getClass().getResource("inputs/").toURI()).toFile();
        File outputs = new File("outputs/");
        outputs.mkdir();
        outputs.deleteOnExit();

        new DataTransformationRunner(cli -> new DT())
                .run("-i", inputs.getPath(), "-o", outputs.getPath());

        List<File> created = Arrays.asList(outputs.listFiles(f -> f.getName().endsWith("yes.txt")));
        assertEquals(2, created.size());
        assertTrue(created.contains(new File(outputs, "my1_yes.txt")));
        assertTrue(created.contains(new File(outputs, "my2_yes.txt")));
        for (String name : Arrays.asList("my1_yes.txt", "my2_yes.txt")) {
            assertEquals(Files.readLines(new File(inputs, name), Charset.defaultCharset()), Files.readLines(new File(outputs, name), Charset.defaultCharset()));
        }

    }

    private static class DT implements DataTransformation {

        boolean m_piped;

        @Override
        public void pipe() {
            m_piped = true;
        }

        @Nonnull
        @Override
        public FileFilter getApplicableFiles() {
            return f -> f.getName().endsWith("yes.txt");
        }

        @Override
        public void apply(@Nonnull BufferedReader br, @Nonnull PrintWriter pw) throws IOException {
            br.lines().forEach(pw::println);
        }

    }
}