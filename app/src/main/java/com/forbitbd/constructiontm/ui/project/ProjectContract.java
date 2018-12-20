package com.forbitbd.constructiontm.ui.project;


import com.forbitbd.constructiontm.model.ProjectPermission;

public interface ProjectContract {

    interface Presenter{
        void loadRightFragment(ProjectPermission projectPermission);
    }

    interface View{
        void showErrorToast(String message);
        void loadActivityFragment();
    }
}
