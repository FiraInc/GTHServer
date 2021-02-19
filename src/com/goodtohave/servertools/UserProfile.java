package com.goodtohave.servertools;

public class UserProfile {

    private String UID;
    private String email;

    protected UserProfile(String UID, String email) {

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
