package com.integrasolusi.pusda.intranet.persistence;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 3, 2011
 * Time         : 5:41:18 PM
 */
public class DataStore {
    public final static Integer FOLDER = 1;
    public final static Integer FILE = 2;
    public final static Integer DOC = 3;
    public final static Integer PDF = 4;
    public final static Integer SPREADSHEET = 5;
    public final static Integer PRESENTATION = 7;
    public final static Integer PICTURE = 6;

    private Long id;
    private Integer version;
    private String name;
    private String description;
    private String filename;
    private Integer storageType = FOLDER;
    private Date submitDate = new Date();


    private Integer index = 1;
    private Long size = -1L;

    private int childrenCount = 0;

    private DataStore parent;

    private List<DataStore> children = new LinkedList<DataStore>();

    public DataStore() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public DataStore getParent() {
        return parent;
    }

    public void setParent(DataStore parent) {
        this.parent = parent;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public List<DataStore> getChildren() {
        return children;
    }

    public void setChildren(List<DataStore> children) {
        this.children = children;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
