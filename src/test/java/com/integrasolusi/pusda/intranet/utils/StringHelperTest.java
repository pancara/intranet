package com.integrasolusi.pusda.intranet.utils;

import org.junit.Test;

/**
 * Programmer   : pancara
 * Date         : Jan 23, 2011
 * Time         : 6:16:07 PM
 */
public class StringHelperTest {
    @Test
    public void testAddNoiseToEmail() throws Exception {
        String email = "horeton@gmail.com";
        String withNoise = StringHelper.addNoiseToEmail(email);
        System.out.println(withNoise);
    }
}
