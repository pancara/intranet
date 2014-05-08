package com.integrasolusi.pusda.intranet.web.form;

import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 8, 2011
 * Time         : 12:33:39 AM
 */
public class MessageForm {
    private List<Long> recipients = new LinkedList<Long>();
    private String content;

    public MessageForm() {
    }

    public List<Long> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Long> recipients) {
        this.recipients = recipients;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
