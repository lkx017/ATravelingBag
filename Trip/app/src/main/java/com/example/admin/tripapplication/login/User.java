package com.example.admin.tripapplication.login;

import java.io.Serializable;

/**
 * Created by admin on 2018-05-30.
 */

public class User implements Serializable {
    private String userID;
    private String userPassword;
    private String userImage;

    public User(){}
    public User(String userID, String userPassword, String userImage) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userImage = userImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
