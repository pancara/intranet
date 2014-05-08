package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:25:50 AM
 */
public class MessageLog implements Serializable {
    private Long id;
    private Integer version;
    private Date postDate = new Date();
    private String description;

    public MessageLog() {
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

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
