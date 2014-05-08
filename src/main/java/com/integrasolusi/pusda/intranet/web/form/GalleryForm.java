package com.integrasolusi.pusda.intranet.web.form;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 5:35:47 AM
 */
public class GalleryForm implements Serializable {
    private Long id;
    private String title;
    private String description;
    private MultipartFile picture;

    public GalleryForm() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
