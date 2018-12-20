package com.forbitbd.constructiontm.ui.searchUser;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUserPresenter implements SearchUserContract.Presenter {

    private SearchUserContract.View mView;
    private MyDatabaseRef myDatabaseRef;
    private FirebaseUser mCurrentUser;

    public SearchUserPresenter(SearchUserContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void requestForData(final String value, int before, int after) {

        if(after>before){
            if(after==1){
                //Request

                myDatabaseRef.getUserRef().orderByChild("email")
                        .startAt(value).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> userList = new ArrayList<>();
                        for (DataSnapshot x: dataSnapshot.getChildren()){
                            User user = x.getValue(User.class);
                            if(!user.getUid().equals(mCurrentUser.getUid())){
                                userList.add(user);
                            }
                        }

                        mView.updateData(userList,value);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }else{
                mView.updateAdapter(value);
            }
        }else {
            mView.updateAdapter(value);
        }

    }
}
