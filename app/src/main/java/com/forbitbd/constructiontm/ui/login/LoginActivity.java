package com.forbitbd.constructiontm.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.BaseActivity;
import com.forbitbd.constructiontm.ui.main.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    private static final int RC_SIGN_IN = 9001;

    private SignInButton signInButton;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this);

        signInButton = findViewById(R.id.google_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loginClick();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                mPresenter.startAutentication(result);
                break;
        }


    }




    @Override
    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void handledatabaseError() {
        hideProgressDialog();
        Toast.makeText(this, "Error Happen in Database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideDialogandFinish() {
        hideProgressDialog();
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }



    @Override
    public void showAutheticationFailureToast() {
        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
    }


}
