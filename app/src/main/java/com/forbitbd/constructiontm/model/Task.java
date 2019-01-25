package com.forbitbd.constructiontm.model;

import com.forbitbd.constructiontm.utility.MyUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sohel on 1/29/2018.
 */

public class Task implements Serializable {

    private String task_id;
    private String project_id;
    private String task_name;
    private long task_start_date;
    private long task_finished_date;
    private double task_volume_of_works;
    private String unit;
    private double task_unit_rate;
    private double task_volume_of_work_done;
    private long task_insert_time;


    public Task() {
    }


    public Task(String task_id, String project_id, String task_name, long task_start_date, long task_finished_date, double task_volume_of_works, String unit, double task_unit_rate, double task_volume_of_work_done, long task_last_modified) {
        this.task_id = task_id;
        this.project_id = project_id;
        this.task_name = task_name;
        this.task_start_date = task_start_date;
        this.task_finished_date = task_finished_date;
        this.task_volume_of_works = task_volume_of_works;
        this.unit = unit;
        this.task_unit_rate = task_unit_rate;
        this.task_volume_of_work_done = task_volume_of_work_done;
        this.task_insert_time = task_last_modified;
    }

    public Task(String project_id, String task_name, long task_start_date, long task_finished_date, double task_volume_of_works, String unit , double task_unit_rate) {
        this("",project_id,task_name,task_start_date,task_finished_date,task_volume_of_works,unit,task_unit_rate,0, System.currentTimeMillis());
    }


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public long getTask_start_date() {
        return task_start_date;
    }

    public void setTask_start_date(long task_start_date) {
        this.task_start_date = task_start_date;
    }

    public long getTask_finished_date() {
        return task_finished_date;
    }

    public void setTask_finished_date(long task_finished_date) {
        this.task_finished_date = task_finished_date;
    }

    public double getTask_volume_of_works() {
        return task_volume_of_works;
    }

    public void setTask_volume_of_works(double task_volume_of_works) {
        this.task_volume_of_works = task_volume_of_works;
    }

    public double getTask_unit_rate() {
        return task_unit_rate;
    }

    public void setTask_unit_rate(double task_unit_rate) {
        this.task_unit_rate = task_unit_rate;
    }

    public double getTask_volume_of_work_done() {
        return task_volume_of_work_done;
    }

    public void setTask_volume_of_work_done(double task_volume_of_work_done) {
        this.task_volume_of_work_done = task_volume_of_work_done;
    }

    public long getTask_insert_time() {
        return task_insert_time;
    }

    public void setTask_insert_time(long task_insert_time) {
        this.task_insert_time = task_insert_time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public int getRemainingDays(){
        return MyUtil.getDuration(task_finished_date,new Date().getTime());
    }

    public int getState(){
        Date date = new Date();
        if(MyUtil.getEndingTime(getTask_finished_date())>date.getTime()
                && getTask_volume_of_work_done()!=getTask_volume_of_works()){
            return 1;
        }else if(getTask_volume_of_work_done()==getTask_volume_of_works()){
            return 2;
        }else if(MyUtil.getEndingTime(getTask_finished_date())<date.getTime()
                && getTask_volume_of_work_done()!=getTask_volume_of_works()){
            return 3;
        }else{
            return 0;
        }
    }
}
