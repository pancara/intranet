package com.integrasolusi.pusda.intranet.web.form;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 12:49:51 AM
 */
public class AccountStatusForm {
    private Boolean confirmed;
    private Boolean approved;
    private Boolean active;
    private Integer role = 1;

    public AccountStatusForm() {
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
