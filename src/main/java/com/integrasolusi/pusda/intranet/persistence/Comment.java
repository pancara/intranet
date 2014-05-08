package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Programmer   : pancara
 * Date         : Jan 22, 2011
 * Time         : 4:46:22 PM
 */
public class Comment implements Serializable {
    private Long id;
    private Integer version;
    private String name;
    private String email;
    private String site;
    private String message;
    private Date postDate;

    public Comment() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
