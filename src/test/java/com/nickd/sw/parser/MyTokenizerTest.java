package com.nickd.sw.parser;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MyTokenizerTest {

    @Test
    public void shouldTokenizeUsingSpaces() {
        MyTokenizer t = new MyTokenizer("a b c");
        assertEquals("a", t.consumeNext());
        assertEquals("b", t.consumeNext());
        assertEquals("c", t.consumeNext());
        assertEquals(List.of("|EOF|"), t.tokens());
        assertEquals(5, t.getPointer());
    }

    @Test
    public void shouldHandleLeadingSpace() {
        MyTokenizer t = new MyTokenizer("    a b c");
        assertEquals("a", t.consumeNext());
        assertEquals("b", t.consumeNext());
        assertEquals("c", t.consumeNext());
        assertEquals(List.of("|EOF|"), t.tokens());
        assertEquals(9, t.getPointer());
    }


    @Test
    public void shouldHandleTrailingSpace() {
        MyTokenizer t = new MyTokenizer("a b c    ");
        assertEquals("a", t.consumeNext());
        assertEquals("b", t.consumeNext());
        assertEquals("c", t.consumeNext());
        assertEquals(List.of("|EOF|"), t.tokens());
        assertEquals(9, t.getPointer());
    }

    @Test
    public void shouldHandleRandomSpace() {
        MyTokenizer t = new MyTokenizer("a    b  c");
        assertEquals("a", t.consumeNext());
        assertEquals("b", t.consumeNext());
        assertEquals("c", t.consumeNext());
        assertEquals(List.of("|EOF|"), t.tokens());
        assertEquals(9, t.getPointer());
    }


    @Test
    public void shouldHandleDifferentLengthTokens() {
        MyTokenizer t = new MyTokenizer("ABBA beegees corbyn");
        assertEquals("ABBA", t.consumeNext());
        assertEquals("beegees", t.consumeNext());
        assertEquals("corbyn", t.consumeNext());
        assertEquals(List.of("|EOF|"), t.tokens());
        assertEquals(19, t.getPointer());
    }


    @Test
    public void shouldKeepUnconsumedTokens() {
        MyTokenizer t = new MyTokenizer("ABBA beegees corbyn unconsumed1 unconsumed2");
        assertEquals("ABBA", t.consumeNext());
        assertEquals("beegees", t.consumeNext());
        assertEquals("corbyn", t.consumeNext());
        assertEquals(List.of("unconsumed1", "unconsumed2", "|EOF|"), t.tokens());
        assertEquals(19, t.getPointer());
    }
}