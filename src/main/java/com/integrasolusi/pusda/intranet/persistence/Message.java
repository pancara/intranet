package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 10:02:21 PM
 */
public class Message implements Serializable {
    public static final int REGULAR = 1;
    public static final int NOTIFICATION = 2;

    private Long id;
    private Integer version;
    private String content;
    private Person sender;
    private Person recipient;
    private Date postDate = new Date();
    private boolean hasRead = false;
    private int type = Message.REGULAR;
    private MessageLog log;

    public Message() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageLog getLog() {
        return log;
    }

    public void setLog(MessageLog log) {
        this.log = log;
    }
}
