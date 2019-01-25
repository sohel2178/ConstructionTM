package com.forbitbd.constructiontm.model;

public class DailyWorkdone {

    private String date;
    private double amount;


    public DailyWorkdone() {
    }

    public DailyWorkdone(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void add(double amount){
        this.amount = this.amount+amount;
    }
}
