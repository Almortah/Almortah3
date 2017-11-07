package com.almortah.almortah;

import java.io.Serializable;

/**
 * Created by ziyadalkhonein on 10/9/17.
 */

public class Chalet implements Serializable {

    private String id;
    private String images;
    private String latitude;
    private String description;
    private String longitude;
    private String chaletNm;
    private String eidPrice;
    private String name;
    private String normalPrice;
    private String ownerID;
    private String promotion;
    private String weekendPrice;
    private String nbOfImages;
    public Chalet() {
    }


    public Chalet(String id, String images, String latitude,
                  String description, String longitude, String chaletNm,
                  String eidPrice, String name, String normalPrice, String ownerID,
                  String promotion, String weekendPrice, String nbOfImages) {
        this.id = id;
        this.images = images;
        this.latitude = latitude;
        this.description = description;
        this.longitude = longitude;
        this.chaletNm = chaletNm;
        this.eidPrice = eidPrice;
        this.name = name;
        this.normalPrice = normalPrice;
        this.ownerID = ownerID;
        this.promotion = promotion;
        this.weekendPrice = weekendPrice;
        this.nbOfImages = nbOfImages;
    }

    public Chalet(Chalet chalet) {
    }


    public String getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public String getChaletNm() {
        return chaletNm;
    }

    public String getEidPrice() {
        return eidPrice;
    }

    public String getName() {
        return name;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getPromotion() {
        return promotion;
    }

    public String getWeekendPrice() {
        return weekendPrice;
    }

    public String getNbOfImages() {
        return nbOfImages;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
    public String getDescription() {
        return description;
    }
}
