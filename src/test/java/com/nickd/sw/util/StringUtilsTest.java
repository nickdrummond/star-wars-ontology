package com.nickd.sw.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StringUtilsTest {


    @Test
    public void testVarReplaceSmallest() {
        List<String> replacements = List.of("zero", "one", "two");
        String s = "&1";

        assertEquals("one", MyStringUtils.replaceVars(s, replacements));
    }

    @Test
    public void testVarReplaceOne() {
        List<String> replacements = List.of("zero", "one", "two");
        String s = "Some &1 like you";

        assertEquals("Some one like you", MyStringUtils.replaceVars(s, replacements));
    }


    @Test
    public void testVarReplaceMultiple() {
        List<String> replacements = List.of("zero", "one", "two", "three");
        String s = "Some &1 like you &2.";

        assertEquals("Some one like you two.", MyStringUtils.replaceVars(s, replacements));
    }

    @Test
    public void testTokeniseUsingWhitespace() {
        String s = "one two three four";
        assertEquals(List.of("one",  "two",  "three", "four"), MyStringUtils.tokenize(s));
    }

    @Test
    public void testTokeniseUsingVariableWhitespace() {
        String s = "one     two\n three      four";
        assertEquals(List.of("one",  "two",  "three", "four"), MyStringUtils.tokenize(s));
    }

    @Test
    public void testTokeniseStripQuotes() {
        String s = "one     two\n three      four \"My other thing\"";
        assertEquals(List.of("one",  "two",  "three", "four", "My other thing"), MyStringUtils.tokenize(s));
    }

    @Test
    public void testTokeniseLeaveQuotes() {
        String s = "one  \" blah with space around \"   two\n   three \"My other thing\"";
        List<String> expected = List.of("one", "\" blah with space around \"", "two", "three", "\"My other thing\"");
        assertEquals(expected, MyStringUtils.tokenize(s, false));
    }
}