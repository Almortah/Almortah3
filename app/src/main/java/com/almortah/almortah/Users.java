package com.almortah.almortah;

/**
 * Created by ziyadalkhonein on 10/7/17.
 */

public class Users {

    private String Name;
    private String email;
    private String nbChalets;
    private String phone;
    private String type;
    private String username;
    private String userID;
    private String isApproved;


    public Users(){}


    public String getIsApproved() {
        return isApproved;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return email;
    }

    public String getNbChalets() {
        return nbChalets;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }
}
