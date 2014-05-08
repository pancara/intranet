package com.integrasolusi.pusda.intranet.persistence;

import com.integrasolusi.pusda.intranet.utils.RoleUtils;

import java.io.Serializable;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:16:01 AM
 */
public class Account implements Serializable {
    private Long id;
    private Integer version;
    private String userID;
    private String password;
    private String registrationToken;
    private Boolean confirmed = false;
    private Boolean approved = false;
    private Boolean active = false;
    private Integer role = RoleUtils.GUEST;

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
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
