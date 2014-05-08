package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 4, 2011
 * Time         : 8:33:31 AM
 */
public class DataStoreDeleteForm implements Serializable {
    private boolean deleteTree = false;

    public DataStoreDeleteForm() {
    }

    public boolean isDeleteTree() {
        return deleteTree;
    }

    public void setDeleteTree(boolean deleteTree) {
        this.deleteTree = deleteTree;
    }
}