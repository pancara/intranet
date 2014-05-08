package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 12:16:21 PM
 */
public class ApproveUserForm implements Serializable {
    private Boolean doApprove;

    public ApproveUserForm() {
    }

    public ApproveUserForm(Boolean doApprove) {
        this.setDoApprove(doApprove);
    }

    public Boolean getDoApprove() {
        return doApprove;
    }

    public void setDoApprove(Boolean doApprove) {
        this.doApprove = doApprove;
    }
}
