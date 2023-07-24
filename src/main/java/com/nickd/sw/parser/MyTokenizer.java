package com.nickd.sw.parser;

import java.util.ArrayList;
import java.util.List;

public class MyTokenizer {

    public static final char DELIM = ' ';
    private final String originalText;
    private int pointer;
    private final List<String> tokens;

    public MyTokenizer(String originalText) {
        this.originalText = originalText;
        this.pointer = startOfNextToken(DELIM, 0);
        this.tokens = new ArrayList<>();
    }

    public MyTokenizer(MyTokenizer other) {
        this.originalText = other.originalText;
        this.pointer = other.pointer;
        this.tokens = new ArrayList<>(other.tokens);
    }

    public int getPointer() {
        return pointer;
    }

    public List<String> tokens() {
        return new ArrayList<>(tokens);
    }

    public String remainder() {
        return originalText.substring(pointer);
    }

    public String consumeNext() {
        String remainder = remainder();
        int endOfNextToken = remainder.indexOf(DELIM);
        if (endOfNextToken != -1) {
            String token = remainder.substring(0, endOfNextToken);
            pointer = startOfNextToken(DELIM, pointer + endOfNextToken);
            tokens.add(0, token);
            return token;
        }
        else {
            pointer += remainder.length();
            tokens.add(0, remainder);
            return remainder;
        }
    }

    private int startOfNextToken(char whitespace, int p) {
        int i = p;
        while (i < originalText.length() && originalText.charAt(i) == whitespace) {
            i++;
        }
        return i;
    }
}
