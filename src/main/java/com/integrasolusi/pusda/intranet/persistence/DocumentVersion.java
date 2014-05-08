package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:34:06 PM
 */
public class DocumentVersion implements Serializable {
    private Long id;
    private Integer version;
    private String description;
    private Integer versionNumber = 1;
    private Document document;
    private Date commitDate;
    private Person committer;
    private String filename;
    private Long size;

    public DocumentVersion() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    public Person getCommitter() {
        return committer;
    }

    public void setCommitter(Person committer) {
        this.committer = committer;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
