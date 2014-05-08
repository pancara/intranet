package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 6, 2011
 * Time         : 10:33:54 PM
 */
public class SelectDataStoreFolderForm implements Serializable {
    private Long folder;

    public SelectDataStoreFolderForm() {
    }

    public Long getFolder() {
        return folder;
    }

    public void setFolder(Long folder) {
        this.folder = folder;
    }
}
