package com.integrasolusi.pusda.intranet.utils;

import org.junit.Test;

/**
 * Programmer   : pancara
 * Date         : 3/23/11
 * Time         : 1:08 AM
 */
public class ContentTypeUtilsTest {
    @Test
    public void testGetTypeByFilenameExt() throws Exception {
        String contentType = ContentTypeUtils.getTypeByFilenameExt("ppt");
        System.out.println("contentType = " + contentType);

    }
}
