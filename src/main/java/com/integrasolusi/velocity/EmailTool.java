package com.integrasolusi.velocity;

import org.apache.commons.lang.StringUtils;

/**
 * Programmer   : pancara
 * Date         : Jan 23, 2011
 * Time         : 8:14:27 PM
 */
public class EmailTool {
    public static final String DEFAULT_NOISE = "_";
    public static final String DEFAULT_NOISE_KEY = "noise";
    public static final String MINIMAL_NOISE_COUNT_KEY = "minimal_noise_count";

    private String noise = DEFAULT_NOISE;
    private int minimalNoiseCount = 8;

    public void configure(java.util.Map params) {
        String s = (String) params.get(DEFAULT_NOISE_KEY);
        if (s != null) noise = s;
        s = (String) params.get(MINIMAL_NOISE_COUNT_KEY);
        if (s != null) {
            try {
                minimalNoiseCount = Integer.parseInt(s);
            } catch (Exception e) {
            }
        }
    }

    public String encryptMail(String email, String noiseChar) {
        String[] tokens = StringUtils.split(email, "@");
        int noiseCount = tokens[0].length() / 3;
        noiseCount = Math.max(minimalNoiseCount, noiseCount);

        String s = StringUtils.substring(tokens[0], 0, tokens[0].length() - noiseCount + 1);

        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < noiseCount; i++)
            sb.append(noiseChar);
        sb.append("@");
        if (tokens.length > 1) sb.append(tokens[1]);
        return sb.toString();
    }

    public String encryptMail(String email) {
        return encryptMail(email, noise);
    }
}
