package com.forbitbd.constructiontm.ui.main.home;

import android.util.Log;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomePresenter implements HomeContract.Presenter {

    private MyDatabaseRef myDatabaseRef;
    private FirebaseUser mCurrentUser;
    private HomeContract.View mView;

    public HomePresenter(HomeContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void getUserFromDatabase() {
        myDatabaseRef.getUserRef().child(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        mView.updateUI(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("JJJJJ",databaseError.getDetails());
                    }
                });
    }

    @Override
    public void callCreateProjectActivity() {
        mView.startCreateProjectActivity();
    }

    @Override
    public void callForShowingAd() {
        mView.showInterAd();
    }
}
