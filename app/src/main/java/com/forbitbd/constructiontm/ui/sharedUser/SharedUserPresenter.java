package com.forbitbd.constructiontm.ui.sharedUser;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.SharedUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
;

public class SharedUserPresenter implements SharedUserContract.Presenter {
    private SharedUserContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public SharedUserPresenter(SharedUserContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void requestForAllUsers(String vehicleId) {
        myDatabaseRef.getSharedUserRef(vehicleId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot x: dataSnapshot.getChildren()){
                            SharedUser sharedUser = x.getValue(SharedUser.class);
                            //sharedUserList.add(sharedUser);
                            mView.addSharedUser(sharedUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void deleteSharedUser(SharedUser sharedUser, final String projectId, final int position) {
        myDatabaseRef.getSharedUserRef(projectId)
                .child(sharedUser.getId()).setValue(null);

        myDatabaseRef.getSharedProjectRef(sharedUser.getId())
                .child(projectId)
                .setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        //FirebaseMessaging.getInstance().unsubscribeFromTopic(projectId);
                        mView.userDeleted(position);
                    }
                });



    }
}
