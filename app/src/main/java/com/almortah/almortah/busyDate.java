package com.almortah.almortah;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 10/19/17.
 */

public class busyDate {
    private ArrayList<String> busyDates = new ArrayList<>();

    public busyDate(ArrayList<String> busyDates) {
        this.busyDates = busyDates;
    }

    public busyDate(){}

    public ArrayList<String> getBusyDates() {
        return busyDates;
    }

    public void setBusyDates(ArrayList<String> busyDates) {
        this.busyDates = busyDates;
    }
}
