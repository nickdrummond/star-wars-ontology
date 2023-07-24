package com.nickd.sw.parser;

import java.util.ArrayList;
import java.util.List;

public class MyTokenizer {

    public static final char DELIM = ' ';
    private static final String PAREN_OPEN = "(";
    private static final String PAREN_CLOSE = ")";
    private final String originalText;
    private int pointer;
    private final List<String> tokens;

    public MyTokenizer(String originalText) {
        this.originalText = originalText;
        this.pointer = skipWhitespace(DELIM, 0);
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

        if (remainder.startsWith(PAREN_OPEN)|| remainder.startsWith(PAREN_CLOSE)) {
            String token = remainder.substring(0, 1);
            pointer++;
            tokens.add(0, token);
            return token;
        }

        int endOfNextToken = remainder.indexOf(DELIM);

        String token = (endOfNextToken != -1) ? remainder.substring(0, endOfNextToken) : remainder;

        if (token.endsWith(PAREN_CLOSE) || token.endsWith(PAREN_OPEN)) {
            token = token.substring(0, token.length()-1);
        }

        pointer += token.length();
        pointer = skipWhitespace(DELIM, pointer);
        tokens.add(0, token);
        return token;
    }

    private int skipWhitespace(char whitespace, int i) {
        while (i < originalText.length() && originalText.charAt(i) == whitespace) {
            i++;
        }
        return i;
    }
}
