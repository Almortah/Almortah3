package com.almortah.almortah;

/**
 * Created by ziyadalkhonein on 10/7/17.
 */

public class Users {

    private String userName;
    private String name;
    private String email;
    private String phoneNumebr;

    public Users(String userName,String name, String email, String phoneNumebr) {
        this.userName=userName;
        this.name = name;
        this.email = email;
        this.phoneNumebr=phoneNumebr;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
