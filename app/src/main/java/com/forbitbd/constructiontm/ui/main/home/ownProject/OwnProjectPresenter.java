package com.forbitbd.constructiontm.ui.main.home.ownProject;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.holder.ProjectHolder;
import com.forbitbd.constructiontm.model.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class OwnProjectPresenter implements OwnProjectContract.Presenter {
    private OwnProjectContract.View mView;
    private MyDatabaseRef myDatabaseRef;
    private FirebaseUser mCurrentUser;

    public OwnProjectPresenter(OwnProjectContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void initializeFirebaseAdapter() {
        Query query = myDatabaseRef.getProjectRef().orderByChild("uid").equalTo(mCurrentUser.getUid());
        mView.setupAdapter(query);
    }

    @Override
    public void performTaskAsPerAction(int action) {
        switch (action){
            case 1:
                mView.startEditProjectActivity();
                break;
            case 3:
                mView.startShareProjectActivity();
                break;

            case 4:
                mView.startShareUserActivity();
                break;
        }
    }

    @Override
    public void actionClick(ProjectHolder holder, Project project, int action) {
        if(action==0){
            mView.startProjectActivity(project);
        }else if(action==2){
            mView.deleteProject(project);
        }else{
            mView.showInterAd(holder,project,action);
        }
    }

    @Override
    public void showDialog(Project project) {
        mView.showDeleteDialog(project);
    }

    @Override
    public void alertNegativeClick() {
        mView.dismissAlertDialog(null);
    }

    @Override
    public void alertPositiveClick(Project project) {
        myDatabaseRef.getProjectRef().child(project.getId()).setValue(null, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mView.dismissAlertDialog("Delete Project Successfully");
            }
        });
    }
}
