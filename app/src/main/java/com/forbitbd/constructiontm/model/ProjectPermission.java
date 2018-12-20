package com.forbitbd.constructiontm.model;

import java.io.Serializable;

/**
 * Created by sohel on 02-05-18.
 */

public class ProjectPermission implements Serializable {

    private String projectId;
    private int activityWrite;
    private int isEnable;

    public ProjectPermission() {
    }

    public ProjectPermission(String projectId, int activityWrite, int isEnable) {
        this.projectId = projectId;
        this.activityWrite = activityWrite;
        this.isEnable = isEnable;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getActivityWrite() {
        return activityWrite;
    }

    public void setActivityWrite(int activityWrite) {
        this.activityWrite = activityWrite;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }
}
