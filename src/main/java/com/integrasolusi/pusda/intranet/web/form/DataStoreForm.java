package com.integrasolusi.pusda.intranet.web.form;

import com.integrasolusi.pusda.intranet.persistence.DataStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 4, 2011
 * Time         : 8:33:31 AM
 */
public class DataStoreForm implements Serializable{
    private String name;
    private String description;
    private Integer storageType = DataStore.FOLDER;
    private MultipartFile file;

    public DataStoreForm() {
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

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
