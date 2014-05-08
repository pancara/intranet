package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:36:06 AM
 */
public class Publication implements Serializable {
    private Long id;
    private Integer version;
    private String title;
    private String content;
    private Date postDate = new Date();

    private String attachmentFilename;

    private Boolean published = false;

    public Publication() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
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

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getAttachmentFilename() {
        return attachmentFilename;
    }

    public void setAttachmentFilename(String attachmentFilename) {
        this.attachmentFilename = attachmentFilename;
    }
}
