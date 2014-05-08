package com.integrasolusi.pusda.intranet.web.form;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 10:26:59 AM
 */
public class NewsForm implements Serializable {
    private String title;
    private String text;
    private Boolean published = false;
    private MultipartFile picture;

    public NewsForm() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

}