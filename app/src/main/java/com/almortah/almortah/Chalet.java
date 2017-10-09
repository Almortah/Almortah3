package com.almortah.almortah;

/**
 * Created by ziyadalkhonein on 10/9/17.
 */

public class Chalet {

    private String chaletID;
    private String chaletName;
    private String chaletType;
    private String chaletLocation;


    private int chaletRating;

    public Chalet(String chaletID, String chaletName, String chaletType, String chaletLocation, int chaletRating) {
        this.chaletID = chaletID;
        this.chaletName = chaletName;
        this.chaletType = chaletType;
        this.chaletLocation = chaletLocation;
        this.chaletRating= chaletRating;
    }

    public String getChaletID() {
        return chaletID;
    }

    public void setChaletID(String chaletID) {
        this.chaletID = chaletID;
    }

    public String getChaletName() {
        return chaletName;
    }

    public void setChaletName(String chaletName) {
        this.chaletName = chaletName;
    }

    public String getChaletType() {
        return chaletType;
    }

    public void setChaletType(String chaletType) {
        this.chaletType = chaletType;
    }

    public String getChaletLocation() {
        return chaletLocation;
    }

    public void setChaletLocation(String chaletLocation) {
        this.chaletLocation = chaletLocation;
    }
    public int getChaletRating() {
        return chaletRating;
    }

    public void setChaletRating(int chaletRating) {
        this.chaletRating = chaletRating;
    }

}
