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


    public Users(){}

    public Users(String name, String email, String nbChalets, String phone, String type, String username) {
        Name = name;
        this.email = email;
        this.nbChalets = nbChalets;
        this.phone = phone;
        this.type = type;
        this.username = username;
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
}
