package com.nickd.sw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtils {

    private static final Pattern whitespaceSeparatedValuesWithPotentialQuotes = Pattern.compile("(\"[^\"]+\")|(\\S+)");

    public static String replaceVars(String input, List<String> replacements) {
        Pattern pattern = Pattern.compile("&(?<value>\\d+)");

        return replace(input, pattern,
                match -> {
                    int i = Integer.parseInt(match.group("value"));
                    return replacements.size() > i ? replacements.get(i) : match.group();
                });
    }

    // see https://www.baeldung.com/java-regex-token-replacement
    public static String replace(String original, Pattern pattern, Function<Matcher, String> converter) {
        int lastIndex = 0;
        StringBuilder output = new StringBuilder();
        Matcher matcher = pattern.matcher(original);
        while (matcher.find()) {
            output.append(original, lastIndex, matcher.start())
                    .append(converter.apply(matcher));
            lastIndex = matcher.end();
        }
        if (lastIndex < original.length()) {
            output.append(original, lastIndex, original.length());
        }
        return output.toString();
    }

    public static String truncate(String original, int maxBeforeTruncate, int truncateLength) {
        if (original.length() < maxBeforeTruncate) {
            return original;
        }
        return original.substring(0, truncateLength) + "…";
    }

    public static String singleLine(String original) {
        return original.replaceAll("\n", " ");
    }

    public static List<String> tokenize(String s) {
        return tokenize(s, true);
    }

    public static List<String> tokenize(String s, boolean stripQuotes) {
        List<String> out = new ArrayList<>();
        Matcher matcher = whitespaceSeparatedValuesWithPotentialQuotes.matcher(s);
        while (matcher.find()) {
            out.add(stripQuotes ? stripQuotes(matcher.group()) : matcher.group());
        }
        return out;
    }

    private static String stripQuotes(String s) {
        return (s.startsWith("\"") && (s.endsWith("\""))) ? s.substring(1, s.length()-1) : s;
    }
}
