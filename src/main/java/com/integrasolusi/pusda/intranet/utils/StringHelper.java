package com.integrasolusi.pusda.intranet.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 3:57:43 PM
 */
public class StringHelper {

    public static String generateRandomText() {
        return generateRandomText(5);
    }

    public static String generateRandomText(int length) {
        char[] dictionary = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder buffer = new StringBuilder();
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(dictionary.length);
            buffer.append(dictionary[index]);
        }
        return StringUtils.upperCase(buffer.toString());
    }

    public static String addNoiseToEmail(String email, char noiseChar) {
        String[] tokens = StringUtils.split(email, "@");
        int noiseCount = tokens[0].length() / 3;
        if (noiseCount < 3) noiseCount = 3;

        String s = StringUtils.substring(tokens[0], 0, tokens[0].length() - noiseCount + 1);

        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < noiseCount; i++)
            sb.append(noiseChar);
        if (tokens.length > 1) sb.append(tokens[1]);
        return sb.toString();
    }

    public static String addNoiseToEmail(String email) {
        return addNoiseToEmail(email, '_');
    }

    public static String removeHtmlTag(String text) {
        StringBuilder buffer = new StringBuilder();
        if (StringUtils.isEmpty(text))
            return buffer.toString();
        char[] chars = text.toCharArray();

        boolean appendToBuffer = true;
        for (char c : chars) {
            if (c == '<') {
                appendToBuffer = false;
                continue;
            }

            if (c == '>') {
                appendToBuffer = true;
                continue;
            }
            if (appendToBuffer) buffer.append(c);
        }
        return buffer.toString();
    }
}
