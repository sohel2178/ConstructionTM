package com.forbitbd.constructiontm.model;

import java.io.Serializable;

/**
 * Created by Sohel on 1/24/2018.
 */

public class Project implements Serializable {

    private String id;
    private String uid;
    private String project_name;
    private String project_description;
    private String project_location;
    private String start_date;
    private String completion_date;
    private String image_url;
    private long insert_time;

    public Project() {
    }

    public Project(String id, String uid, String project_name, String project_description, String project_location, String start_date, String completion_date) {
        this.id = id;
        this.uid = uid;
        this.project_name = project_name;
        this.project_description = project_description;
        this.project_location = project_location;
        this.start_date = start_date;
        this.completion_date = completion_date;
        this.image_url="";
        this.insert_time = System.currentTimeMillis();
    }

    public Project(String uid, String project_name, String project_description, String project_location, String start_date, String completion_date) {
        this("",uid,project_name,project_description,project_location,start_date,completion_date);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public String getProject_location() {
        return project_location;
    }

    public void setProject_location(String project_location) {
        this.project_location = project_location;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }


    public long getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(long insert_time) {
        this.insert_time = insert_time;
    }


}
