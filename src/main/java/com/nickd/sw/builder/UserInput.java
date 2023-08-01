package com.nickd.sw.builder;

import java.util.Collections;
import java.util.List;

import static com.nickd.sw.util.MyStringUtils.tokenize;

public class UserInput {

    public static final String AUTOCOMPLETE_MARKER = "?";
    public static final String AUTOCOMPLETE_REGEX = "\\?";

    private final String command;
    private final List<String> params;
    private final String paramsAsString;
    private final int index;
    private final String fullText;

    public UserInput(String s) {
        this(getCommand(s), getParamsAsList(s), getParamString(s), getNumber(s), s);
    }

    public UserInput(String command, List<String> params, String paramsAsString, int index, String fullText) {

        this.command = command;
        this.params = params;
        this.paramsAsString = paramsAsString;
        this.index = index;
        this.fullText = fullText;
    }

    private static String getCommand(String s) {
        int firstSpace = s.indexOf(" ");
        return firstSpace == -1 ? s : s.substring(0, firstSpace);
    }

    private static int getNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String getParamString(String s) {
        int firstSpace = s.indexOf(" ");
        return firstSpace == -1 ? "" : s.substring(firstSpace+1);
    }

    private static List<String> getParamsAsList(String s) {
        String paramString = getParamString(s);
        return paramString.isEmpty() ? Collections.emptyList() : tokenize(paramString);
    }

    public boolean isIndex() {
        return index > -1;
    }

    public boolean isEmpty() {
        return command.isEmpty();
    }

    public boolean isQuit() {
        return command.equals("quit");
    }

    public boolean isAutocompleteRequest() {
        return fullText.contains(AUTOCOMPLETE_MARKER);
    }

    public UserInput autocomplete(String replacement) {
        return new UserInput(fullText.replaceAll(autocompleteWord() + AUTOCOMPLETE_REGEX, replacement));
    }

    public String autocompleteWord() {
        for (String p : fullText.split(" ")) {
            if (p.endsWith(AUTOCOMPLETE_MARKER)) {
                return p.replaceAll(AUTOCOMPLETE_REGEX, "");
            }
        }
        return "";
    }

    public List<String> params() {
        return params;
    }

    public String fullText() {
        return fullText;
    }

    public String paramsAsString() {
        return paramsAsString;
    }

    public int index() {
        return index;
    }

    public String command() {
        return command;
    }

}
