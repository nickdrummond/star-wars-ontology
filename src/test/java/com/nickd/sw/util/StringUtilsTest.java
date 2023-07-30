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
}