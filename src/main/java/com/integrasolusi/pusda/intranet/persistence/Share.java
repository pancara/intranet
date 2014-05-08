package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 11:13:56 AM
 */
public class Share implements Serializable {
    private Long id;
    private Integer version;
    private Document document;
    private Person person;
    private boolean canModify = false;

    public Share() {
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }
}
