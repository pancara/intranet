package com.integrasolusi.pusda.intranet.persistence;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 27, 2011
 * Time         : 4:31:46 PM
 */
public class Slide implements Serializable {
    public static final Integer SLIDE_FIRST = 1;
    public static final Integer SLIDE_SECOND = 2;

    private Long id;
    private Integer version;
    private String description;
    private String filename;
    private Integer type;

    public Slide() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
