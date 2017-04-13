package com.choco.limsclient.Config;

/**
 * Created by choco on 2017/4/13.
 */

public class CurrentUserInformation {
    private static CurrentUserInformation cUI = null;
    private String userName;
    private String userId;
    private String userType;
    private String auth;        //登录成功后配置，作为后续与服务器交互的凭据

    private CurrentUserInformation() {
        userName = null;
        userId = null;
        userType = null;
        auth = null;
    }

    public static CurrentUserInformation getInstance() {
        if (cUI == null) {
            cUI = new CurrentUserInformation();
        }
        return cUI;
    }

    public String getUserName() {
        return userName;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }


}
