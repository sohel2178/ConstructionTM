package com.forbitbd.constructiontm.ui.main.home.sharedProject;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class ShareProjectPresenter implements ShareProjectContract.Presenter{
    private ShareProjectContract.View mView;
    private FirebaseUser mCurrentUser;
    private MyDatabaseRef myDatabaseRef;

    public ShareProjectPresenter(ShareProjectContract.View mView) {
        this.mView = mView;
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void initializeFirebaseAdapter() {
        Query query = myDatabaseRef.getSharedProjectRef(mCurrentUser.getUid()).orderByChild("isEnable").equalTo(1);
        mView.setupAdapter(query);

    }

    @Override
    public void performTaskAsPerAction(int action) {
        switch (action){
            case 1:
                mView.startProjectActivity();
                break;

            case 2:
                mView.deleteProject();
                break;
        }
    }

    @Override
    public void actionClick(ProjectPermission projectPermission, int action) {
        mView.showInterAd(projectPermission,action);
    }

    @Override
    public void showDialog() {
        mView.showDeleteDialog();
    }

    @Override
    public void retrieveProject(String projectId) {
        myDatabaseRef.getProjectRef().child(projectId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Project project = dataSnapshot.getValue(Project.class);

                        if(project!=null){
                            mView.startProjectActivity(project);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void alertNegativeClick() {
        mView.dismissAlertDialog(null);
    }

    @Override
    public void alertPositiveClick(final ProjectPermission projectPermission) {
         myDatabaseRef.getSharedUserRef(projectPermission.getProjectId())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);

        myDatabaseRef.getSharedProjectRef(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(projectPermission.getProjectId()).setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // Un Subscribe From Topic Message
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(projectPermission.getProjectId());
                        mView.dismissAlertDialog("Delete Project Successfully");
                    }
                });
    }
}
