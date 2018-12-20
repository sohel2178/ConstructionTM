package com.forbitbd.constructiontm.ui.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;


    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loginClick() {

        mView.googleSignIn();

    }

    public void startAutentication(GoogleSignInResult result) {
        mView.showDialog();
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();

            firebaseAuthWithGoogle(account);
        } else {
            mView.showAutheticationFailureToast();
        }

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user!=null){
                        final MyDatabaseRef myDatabaseRef = new MyDatabaseRef();


                        myDatabaseRef.getUserRef().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()==null){

                                    Log.d("HHHHH","IF Called");

                                    myDatabaseRef.getUserRef().child(user.getUid()).setValue(new User(user), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            mView.hideDialogandFinish();
                                        }
                                    });

                                }else{
                                    Log.d("HHHHH","IF Called");
                                    mView.hideDialogandFinish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("HHHHH",databaseError.getDetails());
                                    mView.handledatabaseError();
                            }
                        });

                    }

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mView.handledatabaseError();
            }
        });
    }
}
