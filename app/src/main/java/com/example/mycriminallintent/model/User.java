package com.example.mycriminallintent.model;

public class User {
    private String mUsername;
    private String mPassword;

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
}
