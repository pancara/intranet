package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 5, 2011
 * Time         : 5:15:29 PM
 */
public class SearchForm implements Serializable {
    private String keyword = "";

    public SearchForm() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
