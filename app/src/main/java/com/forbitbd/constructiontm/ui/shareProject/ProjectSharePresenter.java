package com.forbitbd.constructiontm.ui.shareProject;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.SharedUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ProjectSharePresenter implements ProjectShareContract.Presenter {
    private ProjectShareContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public ProjectSharePresenter(ProjectShareContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }


    @Override
    public void shareClick(ProjectPermission projectPermission, final String uid, final String projectId) {
        mView.showDialog();
        myDatabaseRef.getSharedProjectRef(uid)
                .child(projectId)
                .setValue(projectPermission, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        SharedUser sharedUser = new SharedUser(uid,1);

                        myDatabaseRef.getSharedUserRef(projectId).child(uid).setValue(sharedUser, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                mView.complete();

                            }
                        });
                    }
                });

    }
}
