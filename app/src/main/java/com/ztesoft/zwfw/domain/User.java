package com.ztesoft.zwfw.domain;

import java.util.List;

/**
 * Created by BaoChengchen on 2017/9/5.
 */

public class User {

    String userId;
    String portalId;
    String userName;
    String memo;
    String contactInfo;
    List<String> userRoleType;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<String> getUserRoleType() {
        return userRoleType;
    }

    public void setUserRoleType(List<String> userRoleType) {
        this.userRoleType = userRoleType;
    }
}
