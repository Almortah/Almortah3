package com.almortah.almortah;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ziyadalkhonein on 10/9/17.
 */

public class Chalet implements Parcelable {

    private String images;
    private String description;
    private String chaletNm;
    private String eidPrice;
    private String id;
    private String latitude;
    private String longitude;
    private String name;
    private String nbImages;
    private String normalPrice;
    private String ownerID;
    private String promotion;
    private String rating;
    private String weekendPrice;


    public Chalet() {}

    public Chalet(String images, String description,
                  String chaletNm, String eidPrice,
                  String id, String latitude, String longitude,
                  String name, String nbImages, String normalPrice,
                  String ownerID, String promotion, String weekendPrice) {
        this.images = images;
        this.description = description;
        this.chaletNm = chaletNm;
        this.eidPrice = eidPrice;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.nbImages = nbImages;
        this.normalPrice = normalPrice;
        this.ownerID = ownerID;
        this.promotion = promotion;
        this.weekendPrice = weekendPrice;
    }

    public Chalet(Parcel in) {
        this.images = in.readString();
        this.description = in.readString();
        this.chaletNm = in.readString();
        this.eidPrice = in.readString();
        this.id = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.name = in.readString();
        this.nbImages = in.readString();
        this.normalPrice = in.readString();
        this.ownerID = in.readString();
        this.promotion = in.readString();
        this.rating = in.readString();
        this.weekendPrice = in.readString();
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
        return nbImages;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(images);
        dest.writeString(description);
        dest.writeString(chaletNm);
        dest.writeString(eidPrice);
        dest.writeString(id);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(name);
        dest.writeString(nbImages);
        dest.writeString(normalPrice);
        dest.writeString(ownerID);
        dest.writeString(promotion);
        dest.writeString(rating);
        dest.writeString(weekendPrice);

    }

    public static final Parcelable.Creator<Chalet> CREATOR
            = new Parcelable.Creator<Chalet>() {
        public Chalet createFromParcel(Parcel in) {
            return new Chalet(in);
        }

        public Chalet[] newArray(int size) {
            return new Chalet[size];
        }
    };
}
