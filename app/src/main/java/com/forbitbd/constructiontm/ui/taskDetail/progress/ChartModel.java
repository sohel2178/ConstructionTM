package com.forbitbd.constructiontm.ui.taskDetail.progress;

public class ChartModel {

    private long date;
    private double amount;

    public ChartModel() {
    }

    public ChartModel(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void addAmount(double amount){
        this.amount = this.amount+amount;
    }
}
