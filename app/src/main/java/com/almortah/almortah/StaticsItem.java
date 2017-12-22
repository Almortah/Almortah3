package com.almortah.almortah;

/**
 * Created by ALMAHRI on 20/12/2017.
 */

public class StaticsItem {
    private String chaletName;
    private String totalReservation;
    private String avgRating;
    private String bestCustomer;
    private int totalRevenue = -1;
    private String priceRating;
    private String reicptRating;
    private String cleanRating;

    public StaticsItem(){}

    public StaticsItem(String chaletName, String totalReservation,
                       String avgRating, String bestCustomer,
                       int totalRevenue, String priceRating,
                       String reicptRating, String cleanRating) {
        this.chaletName = chaletName;
        this.totalReservation = totalReservation;
        this.avgRating = avgRating;
        this.bestCustomer = bestCustomer;
        this.totalRevenue = totalRevenue;
        this.priceRating = priceRating;
        this.reicptRating = reicptRating;
        this.cleanRating = cleanRating;
    }

    public String getChaletName() {
        return chaletName;
    }

    public String getTotalReservation() {
        return totalReservation;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public String getBestCustomer() {
        return bestCustomer;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public String getPriceRating() {
        return priceRating;
    }

    public String getReicptRating() {
        return reicptRating;
    }

    public String getCleanRating() {
        return cleanRating;
    }

    public void setTotalRevenue(int totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setChaletName(String chaletName) {
        this.chaletName = chaletName;
    }

    public void setTotalReservation(String totalReservation) {
        this.totalReservation = totalReservation;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public void setBestCustomer(String bestCustomer) {
        this.bestCustomer = bestCustomer;
    }

    public void setPriceRating(String priceRating) {
        this.priceRating = priceRating;
    }

    public void setReicptRating(String reicptRating) {
        this.reicptRating = reicptRating;
    }

    public void setCleanRating(String cleanRating) {
        this.cleanRating = cleanRating;
    }

    public boolean isOK() {
        if(
        chaletName == null ||
        totalReservation == null ||
        avgRating == null ||
        bestCustomer == null ||
        totalRevenue == -1 ||
        priceRating == null ||
        reicptRating == null ||
        cleanRating == null
                )
            return false;

        else return true;
    }
}
