package com.forbitbd.constructiontm.ui.main.profile;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.database.MyStorageRef;
import com.forbitbd.constructiontm.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View mView;
    private MyDatabaseRef myDatabaseRef;
    private MyStorageRef myStorageRef;
    private FirebaseUser mCurrentUser;

    public ProfilePresenter(ProfileContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
        this.myStorageRef = new MyStorageRef();
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public boolean validate(String companyName, String companyDesc, byte[] bytes) {

        mView.clearPreErrors();

        if(TextUtils.isEmpty(companyName)){
            mView.showErrorMessage(FieldConstant.COMPANY_NAME,"Field is Empty");
            return false;
        }

        if(TextUtils.isEmpty(companyDesc)){
            mView.showErrorMessage(FieldConstant.COMPANY_DESC,"Field is Empty");
            return false;
        }

        if(bytes==null){
            mView.showErrorMessage(FieldConstant.COMPANY_LOGO,"Select Company Logo");
            return false;
        }

        return true;
    }

    @Override
    public void updateInfo(final String companyName, final String companyDesc, byte[] bytes, boolean isupdate) {
        mView.showDialog();
        if(isupdate){
            addintoDataBase(companyName,companyDesc,bytes);
        }else{
           updateDatabase(companyName,companyDesc);
        }

    }

    private void addintoDataBase(final String companyName, final String companyDesc, byte[] bytes){
        myStorageRef.getUserStoreRef().child(mCurrentUser.getUid()).putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        addInfoIntoDatabase(url,companyName,companyDesc);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        mView.hideDialog();
                    }
                });
    }

    private void addInfoIntoDatabase(String url, String companyName, String companyDesc){
        User user = new User(mCurrentUser);
        user.setCompany_logo(url);
        user.setCompanyName(companyName);
        user.setCompanyDesc(companyDesc);

        myDatabaseRef.getUserRef().child(mCurrentUser.getUid())
                .setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mView.complete();
                    }
                });

    }

    private void updateDatabase(String companyName, String companyDesc){

        Map<String,Object> map = new HashMap<>();
        map.put("companyName",companyName);
        map.put("companyDesc",companyDesc);

        myDatabaseRef.getUserRef().child(mCurrentUser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mView.complete();
            }
        });

    }

    @Override
    public void browseClick() {
        mView.openCropImageActivity();
    }

    @Override
    public void requestForUser() {
        myDatabaseRef.getUserRef().child(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user!=null){
                            mView.updateInfo(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
