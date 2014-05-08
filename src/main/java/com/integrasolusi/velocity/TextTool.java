package com.integrasolusi.velocity;

import com.integrasolusi.pusda.intranet.utils.StringHelper;
import com.opensymphony.util.TextUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Programmer   : pancara
 * Date         : Feb 8, 2011
 * Time         : 6:11:07 PM
 */
public class TextTool {

    public void configure(java.util.Map params) {
        // do nothing
    }

    public String toHtml(String text) {
        return TextUtils.plainTextToHtml(text, true);
    }

    public String substring(String text, int length) {
        return StringUtils.abbreviate(text, length);
    }

    public String removeHtmlTag(String text) {
        return StringHelper.removeHtmlTag(text);
    }

    public String[] toLines(String text) {
        return StringUtils.split(text, "\n");
    }
}

