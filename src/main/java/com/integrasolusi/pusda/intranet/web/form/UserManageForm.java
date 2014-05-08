package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 15, 2011
 * Time         : 6:15:30 PM
 */
public class UserManageForm implements Serializable {
    private String keyword = "";
    private Boolean useAdvancedFilter = false;
    private AdvanceFilter advanceFilter = new AdvanceFilter();
    private Integer page = 1;

    public UserManageForm() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public AdvanceFilter getAdvanceFilter() {
        return advanceFilter;
    }

    public void setAdvanceFilter(AdvanceFilter advanceFilter) {
        this.advanceFilter = advanceFilter;
    }

    public Boolean getUseAdvancedFilter() {
        return useAdvancedFilter;
    }

    public void setUseAdvancedFilter(Boolean useAdvancedFilter) {
        this.useAdvancedFilter = useAdvancedFilter;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public static class AdvanceFilter {
        private Boolean confirmed = false;
        private Boolean approved = false;
        private Boolean active = false;

        public AdvanceFilter() {
        }

        public Boolean getConfirmed() {
            return confirmed;
        }

        public void setConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
        }

        public Boolean getApproved() {
            return approved;
        }

        public void setApproved(Boolean approved) {
            this.approved = approved;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }
    }
}
