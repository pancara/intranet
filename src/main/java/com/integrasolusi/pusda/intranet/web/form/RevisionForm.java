package com.integrasolusi.pusda.intranet.web.form;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 13, 2011
 * Time         : 8:28:14 AM
 */
public class RevisionForm implements Serializable {
    private String description;
    private MultipartFile document;

    public RevisionForm() {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }
}
