package com.forbitbd.constructiontm.ui.shareProject;


import com.forbitbd.constructiontm.model.ProjectPermission;

public interface ProjectShareContract {

    interface Presenter{
        void shareClick(ProjectPermission projectPermission, String uid, String projectId);
    }

    interface View{
        void showDialog();
        void complete();
    }


}
