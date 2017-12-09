package com.almortah.almortah;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ALMAHRI on 10/21/17.
 */

public class Reservation implements Parcelable {

    private String chaletID;
    private String chaletName;
    private String checkin;
    private String checkout;
    private String confirm;
    private String customerID;
    private String date;
    private String ownerID;
    private String payment;
    private String price;
    private String rated;
    private String ratedCustomer;
    private String reservationID;

    public Reservation() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chaletID);
        dest.writeString(chaletName);
        dest.writeString(checkin);
        dest.writeString(checkout);
        dest.writeString(confirm);
        dest.writeString(customerID);
        dest.writeString(date);
        dest.writeString(ownerID);
        dest.writeString(payment);
        dest.writeString(price);
        dest.writeString(rated);
        dest.writeString(ratedCustomer);
        dest.writeString(reservationID);
    }

    public Reservation(Parcel in) {
        this.chaletID = in.readString();
        this.chaletName = in.readString();
        this.checkin = in.readString();
        this.checkout = in.readString();
        this.confirm = in.readString();
        this.customerID = in.readString();
        this.date = in.readString();
        this.ownerID = in.readString();
        this.payment = in.readString();
        this.price = in.readString();
        this.rated = in.readString();
        this.ratedCustomer = in.readString();
        this.reservationID = in.readString();
    }


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

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Reservation> CREATOR
            = new Parcelable.Creator<Reservation>() {
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public Reservation(String chaletID, String chaletName, String checkin, String checkout, String confirm, String customerID, String date, String ownerID, String payment, String price, String rated, String ratedCustomer, String reservationID) {
        this.chaletID = chaletID;
        this.chaletName = chaletName;
        this.checkin = checkin;
        this.checkout = checkout;
        this.confirm = confirm;
        this.customerID = customerID;
        this.date = date;
        this.ownerID = ownerID;
        this.payment = payment;
        this.price = price;
        this.rated = rated;
        this.ratedCustomer = ratedCustomer;
        this.reservationID = reservationID;
    }
}
