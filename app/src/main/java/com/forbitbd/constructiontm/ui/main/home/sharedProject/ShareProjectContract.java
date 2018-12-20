package com.forbitbd.constructiontm.ui.main.home.sharedProject;

import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.google.firebase.database.Query;

public interface ShareProjectContract {

    interface Presenter{
        void initializeFirebaseAdapter();
        void performTaskAsPerAction(int action);
        void actionClick(ProjectPermission projectPermission, int action);

        void showDialog();
        void retrieveProject(String projectId);

        void alertNegativeClick();
        void alertPositiveClick(ProjectPermission projectPermission);

    }

    interface View{
        void setupAdapter(Query query);
        void showInterAd(ProjectPermission projectPermission, int action);
        void startProjectActivity();
        void startProjectActivity(Project project);
        void deleteProject();

        void showDeleteDialog();
        void dismissAlertDialog(String message);
    }
}
