package com.integrasolusi.velocity;

import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

/**
 * Programmer   : pancara
 * Date         : Feb 8, 2011
 * Time         : 8:14:25 PM
 */
public class TextToolTest {
    @Test
    public void testToHtml() throws Exception {
        String text = "line 1\n line 2\n <a href=''>link</a>line3.";
        TextTool textTool = new TextTool();
        String html = textTool.toHtml(text);
        System.out.println("html = " + html);
    }
}
