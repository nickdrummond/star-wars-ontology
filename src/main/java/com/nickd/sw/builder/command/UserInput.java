package com.nickd.sw.builder.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record UserInput(String command, List<String> params, String paramsAsString, int index, String fullText) {

    public static final String AUTOCOMPLETE_MARKER = "?";
    public static final String AUTOCOMPLETE_REGEX = "\\?";

    public UserInput(String s) {
        this(getCommand(s), getParamsAsList(s), getParamString(s), getNumber(s), s);
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
        return paramString.isEmpty() ? Collections.emptyList() : Arrays.asList(paramString.split(" "));
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
}
