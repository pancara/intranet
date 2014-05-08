package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 2:38:58 PM
 */
public class Site implements Serializable {
    private Long id;
    private Integer version;
    private String name;
    private String url;
    private String filename;
    private Boolean active = true;
    private Integer index = 1;

    public Site() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
