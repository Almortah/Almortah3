package com.almortah.almortah;

/**
 * Created by ALMAHRI on 10/21/17.
 */

public class Reservation {

    private String chaletName;
    private String chaletNb;
    private String checkin;
    private String checkout;
    private String customerID;
    private String date;
    private String ownerID;
    private String payment;
    private String price;

    public Reservation() {}

    public Reservation(String chaletName, String chaletNb, String checkin, String checkout, String customerID, String date, String ownerID, String payment, String price) {
        this.chaletName = chaletName;
        this.chaletNb = chaletNb;
        this.checkin = checkin;
        this.checkout = checkout;
        this.customerID = customerID;
        this.date = date;
        this.ownerID = ownerID;
        this.payment = payment;
        this.price = price;
    }

    public String getChaletNb() {
        return chaletNb;
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

    public String getChaletName() {
        return chaletName;
    }

    public String getPrice() {
        return price;
    }
}
