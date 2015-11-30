package com.github.dmyersturnbull.transformations;

import org.apache.commons.cli.Option;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests {@link CommandLineHelper}.
 * @author Douglas Myers-Turnbull
 */
public class CommandLineHelperTest {

    @Test
    public void testParse() throws Exception {
        Optional<ExtendedCommandLine> cl = new CommandLineHelper()
                .addOptions(Option.builder().argName("test").longOpt("test").build())
                .parse("--test", "testit!");
        assertTrue(cl.isPresent());
    }

    @Test
    public void testFailParse() throws Exception {
        Optional<ExtendedCommandLine> cl = new CommandLineHelper()
                .addOptions(Option.builder().argName("test").longOpt("test").build())
                .parse("--r", "testit!");
        assertFalse(cl.isPresent());
    }

    @Test
    public void testMultiargs() throws Exception {
        Optional<ExtendedCommandLine> cl = new CommandLineHelper()
                .addOptions(Option.builder().argName("t").longOpt("t").numberOfArgs(Option.UNLIMITED_VALUES).build())
                .parse("-t", "1", "-t", "2", "-t", "3");
        assertTrue(cl.isPresent());
        assertEquals(Arrays.asList(1, 2, 3), cl.get().getListOf("t", Integer::new));
    }

    @Test
    public void testPrintHelp() throws Exception {
        StringWriter sw = new StringWriter();
        new CommandLineHelper()
                .setClass(CommandLineHelperTest.class)
                .addOptions(Option.builder().argName("test").longOpt("test").build())
                .setHelpHeader("This is a header")
                .setHelpFooter("This is a footer").printHelp(new PrintWriter(new BufferedWriter(sw)));
        assertEquals("usage: CommandLineHelperTest [-h] [--test]\n" +
                "This is a header\n" +
                "-h,--helpPrint usage\n" +
                "   --test\n" +
                "This is a footer\n",
                sw.toString());
    }

}