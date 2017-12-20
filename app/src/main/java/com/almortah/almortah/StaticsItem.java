package com.almortah.almortah;

/**
 * Created by ALMAHRI on 20/12/2017.
 */

public class StaticsItem {
    private String chaletName;
    private String totalReservation;
    private String avgRating;
    private String bestCustomer;
    private int totalRevenue;
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
}
