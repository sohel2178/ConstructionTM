package com.forbitbd.constructiontm.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.BaseActivity;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.ui.login.LoginActivity;
import com.forbitbd.constructiontm.ui.main.home.HomeFragment;
import com.forbitbd.constructiontm.ui.main.profile.ProfileFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

public class MainActivity extends BaseActivity implements MainContract.View{

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPresenter = new MainPresenter(this);
        //Initialize Ad
        new AdUtil(this);

        mPresenter.checkAndStart();

        setupWindowAnimations();

        //Log.d("HHHHHH",FirebaseInstanceId.getInstance().getToken());
        Log.d("HHHHHH","HHHH");

    }


    @Override
    protected void onResume() {
        super.onResume();

        setupConfig();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void checkAndStart() {

        mPresenter.checkCurrentUser();

        /*if(!isOnline()){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new NoInternetFragment()).commit();
        }else{

        }*/
    }

    @Override
    public void startLoginActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void loadHomeFragment() {
        setUpNavigationDrawer();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment())
                .commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    Bitmap scaledBitMap = MyUtil.getScaledBitmap(bitmap,100,100);

                    if(getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof ProfileFragment){
                        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
                        profileFragment.setBitmap(scaledBitMap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
