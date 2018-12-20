package com.forbitbd.constructiontm.ui.projectAdd;


import com.forbitbd.constructiontm.model.Project;

public interface AddProjectContract {

    interface Presenter{
        boolean validate(Project project);
        void addProjectToDatabase(Project project);
    }

    interface View{
        void showValidationError(String message, int fieldId);
        void showDialog();
        void hideDialog();

        void clearPreError();
    }

}
