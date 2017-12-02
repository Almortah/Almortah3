package com.almortah.almortah;

/**
 * Created by ALMAHRI on 10/21/17.
 */

public class Reservation {

    private String confirm;
    private String chaletID;
    private String chaletName;
    private String checkin;
    private String checkout;
    private String customerID;
    private String date;
    private String ownerID;
    private String payment;
    private String price;
    private String rated;
    private String ratedCustomer;
    private String reservationID;

    public Reservation() {}


    public String getRated() {
        return rated;
    }

    public String getChaletID() {
        return chaletID;
    }

    public String getChaletName() {
        return chaletName;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getDate() {
        return date;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getPayment() {
        return payment;
    }

    public String getPrice() {
        return price;
    }

    public String getReservationID() {
        return reservationID;
    }

    public String getConfirm() {
        return confirm;
    }

    public String getRatedCustomer() {
        return ratedCustomer;
    }
}
