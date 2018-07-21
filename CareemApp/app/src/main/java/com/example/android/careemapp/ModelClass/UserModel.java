package com.example.android.careemapp.ModelClass;

/**
 * Created by asher.ansari on 1/29/2018.
 */

public class UserModel {
    private String uid;
    private String name;
    private String email;
    private String psk;
    private String userRight;
    private String mobileId;

    public UserModel() {
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPsk() {
        return psk;
    }

    public String getUserRight() {
        return userRight;
    }

    public String getMobileId() {
        return mobileId;
    }

    public UserModel(String uid, String name, String email, String psk, String userRight, String mobileId) {

        this.uid = uid;
        this.name = name;
        this.email = email;
        this.psk = psk;
        this.userRight = userRight;
        this.mobileId = mobileId;
    }
}
