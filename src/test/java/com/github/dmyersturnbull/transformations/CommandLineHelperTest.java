package com.github.dmyersturnbull.transformations;

import org.apache.commons.cli.Option;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * Tests {@link CommandLineHelper}.
 * @author Douglas Myers-Turnbull
 */
public class CommandLineHelperTest {

    @Test
    public void testParse() throws Exception {
        new CommandLineHelper()
                .addOptions(Option.builder().argName("test").longOpt("test").build())
                .parse("--test", "testit!");
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