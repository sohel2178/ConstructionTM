package com.forbitbd.constructiontm.ui.main.home.ownProject;

import com.forbitbd.constructiontm.holder.ProjectHolder;
import com.forbitbd.constructiontm.model.Project;
import com.google.firebase.database.Query;

public interface OwnProjectContract {

    interface Presenter{
        void initializeFirebaseAdapter();
        void performTaskAsPerAction(int action);
        void actionClick(ProjectHolder holder, Project project, int action);
        void showDialog(Project project);
        void alertNegativeClick();
        void alertPositiveClick(Project project);
    }

    interface View{
        void setupAdapter(Query query);
        void showInterAd(ProjectHolder holder, Project project, int action);
        void startProjectActivity(Project project);
        void startEditProjectActivity();
        void deleteProject(Project project);
        void startShareProjectActivity();
        void startShareUserActivity();
        void showDeleteDialog(Project project);
        void dismissAlertDialog(String message);
    }
}
