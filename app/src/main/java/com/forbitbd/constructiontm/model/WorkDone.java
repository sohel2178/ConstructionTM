package com.forbitbd.constructiontm.model;

import java.io.Serializable;

/**
 * Created by Sohel on 2/3/2018.
 */

public class WorkDone implements Serializable {

    private String work_done_id;
    private String task_id;
    private long date;
    private double amount;

    public WorkDone() {
    }

    public WorkDone(String work_done_id, String task_id, long date, double amount) {
        this.work_done_id = work_done_id;
        this.task_id = task_id;
        this.date = date;
        this.amount = amount;
    }

    public WorkDone(String task_id, double amount) {
        this("",task_id, System.currentTimeMillis(),amount);
    }

    public String getWork_done_id() {
        return work_done_id;
    }

    public void setWork_done_id(String work_done_id) {
        this.work_done_id = work_done_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
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
