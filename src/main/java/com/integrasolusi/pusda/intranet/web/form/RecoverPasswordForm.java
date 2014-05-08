package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 12:41:46 PM
 */
public class RecoverPasswordForm implements Serializable {
    private String userID;
    private String captcha;

    public RecoverPasswordForm() {
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
