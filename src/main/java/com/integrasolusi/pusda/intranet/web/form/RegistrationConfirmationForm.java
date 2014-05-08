package com.integrasolusi.pusda.intranet.web.form;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 20, 2011
 * Time         : 4:17:03 PM
 */
public class RegistrationConfirmationForm implements Serializable {
    private String token;
    private Long id;
    private String captcha;

    public RegistrationConfirmationForm() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
