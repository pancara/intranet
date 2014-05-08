package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 9:31:42 AM
 */
public class Gallery implements Serializable {
    private Long id;
    private Integer version;
    private String title;
    private String description;
    private Integer pictureCount;

    public Gallery() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(Integer pictureCount) {
        this.pictureCount = pictureCount;
    }
}