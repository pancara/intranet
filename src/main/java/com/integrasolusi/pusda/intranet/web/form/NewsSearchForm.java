package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:44:06 AM
 */
public class NewsSearchForm implements Serializable {
    private String keyword = "";
    private Integer page = 1;

    public NewsSearchForm() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}