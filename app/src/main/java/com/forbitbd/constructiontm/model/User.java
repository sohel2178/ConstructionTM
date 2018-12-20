package com.forbitbd.constructiontm.model;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 1/21/2018.
 */

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String photoUri;
    private String companyName;
    private String companyDesc;
    private String company_logo;
    private List<Project> projects;
    private int isActive;

    public User() {
    }

    public User(String uid, String name, String photoUri, String companyName) {
        this.uid = uid;
        this.name = name;
        this.photoUri = photoUri;
        this.companyName = companyName;
        projects = new ArrayList<>();
    }

    public User(FirebaseUser firebaseUser){
        this.uid = firebaseUser.getUid();
        this.name = firebaseUser.getDisplayName();
        this.photoUri = firebaseUser.getPhotoUrl().toString();
        this.email = firebaseUser.getEmail();
        this.companyName = "";
        this.company_logo="";
        this.companyDesc="";
        this.isActive=1;
        projects = new ArrayList<>();
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    private class Project{
        private String project_id;
        private String uid;
        private int project_type;
        private int access;

        public Project() {
        }

        public Project(String project_id, String uid, int project_type, int access) {
            this.project_id = project_id;
            this.uid = uid;
            this.project_type = project_type;
            this.access = access;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getProject_type() {
            return project_type;
        }

        public void setProject_type(int project_type) {
            this.project_type = project_type;
        }

        public int getAccess() {
            return access;
        }

        public void setAccess(int access) {
            this.access = access;
        }




    }
}
