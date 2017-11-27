package com.almortah.almortah;

/**
 * Created by ALMAHRI on 10/21/17.
 */

public class Reservation {

    private String chaletID;
    private String chaletName;
    private String checkin;
    private String checkout;
    private String customerID;
    private String date;
    private String ownerID;
    private String payment;
    private String price;
    private String reservationID;

    public Reservation() {}


    public Reservation(String chaletID, String chaletName, String checkin, String checkout, String customerID, String date, String ownerID, String payment, String price, String reservationID) {
        this.chaletID = chaletID;
        this.chaletName = chaletName;
        this.checkin = checkin;
        this.checkout = checkout;
        this.customerID = customerID;
        this.date = date;
        this.ownerID = ownerID;
        this.payment = payment;
        this.price = price;
        this.reservationID = reservationID;
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
}
