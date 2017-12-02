package com.almortah.almortah;

/**
 * Created by ALMAHRI on 30/11/2017.
 */

public class Rating {
    private String chaletID;
    private String cleanRating;
    private String comment;
    private String customerID;
    private String customerName;
    private String priceRating;
    private String reicptRating;

    public Rating(){}

    public Rating(String chaletID, String cleanRating, String comment, String customerID, String customerName, String priceRating, String reicptRating) {
        this.chaletID = chaletID;
        this.cleanRating = cleanRating;
        this.comment = comment;
        this.customerID = customerID;
        this.customerName = customerName;
        this.priceRating = priceRating;
        this.reicptRating = reicptRating;
    }

    public String getChaletID() {
        return chaletID;
    }

    public String getCleanRating() {
        return cleanRating;
    }

    public String getComment() {
        return comment;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPriceRating() {
        return priceRating;
    }

    public String getReicptRating() {
        return reicptRating;
    }
}
