package com.forbitbd.constructiontm.ui.projectAdd;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Project;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class AddProjectPresenter implements AddProjectContract.Presenter {
    private AddProjectContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public AddProjectPresenter(AddProjectContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public boolean validate(Project project) {

        mView.clearPreError();

        if(project.getProject_name().equals("") || project.getProject_name()==null){
            mView.showValidationError("Project Name Empty",1);
            return false;
        }

        if(project.getProject_location().equals("") || project.getProject_location()==null){
            mView.showValidationError("Project Location Empty",2);
            return false;
        }

        if(project.getProject_description().equals("") || project.getProject_description()==null){
            mView.showValidationError("Project Description Empty",3);
            return false;
        }



        if(project.getStart_date().equals("") || project.getStart_date()==null){
            mView.showValidationError("Select Project Start Date",4);
            return false;
        }

        if(project.getCompletion_date().equals("") || project.getCompletion_date()==null){
            mView.showValidationError("Select Project Completion Date",5);
            return false;
        }


        return true;
    }

    @Override
    public void addProjectToDatabase(final Project project) {
        String refKey = myDatabaseRef.getProjectRef().push().getKey();
        project.setId(refKey);
        mView.showDialog();

        myDatabaseRef.getProjectRef().child(refKey).setValue(project, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mView.hideDialog();
            }
        });


    }
}
