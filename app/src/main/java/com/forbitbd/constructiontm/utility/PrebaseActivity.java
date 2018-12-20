package com.forbitbd.constructiontm.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.ui.main.nav.NavigationDrawer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Locale;


/**
 * Created by sohel on 29-01-18.
 */

public class PrebaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;

    private TextView tvTitle;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private FragmentManager manager;
    private NavigationDrawer drawerFragment;


    private FloatingActionButton fabAdd;
    private RelativeLayout mRootLayout;

    private UserLocalStore userLocalStore;
    private Locale locale;
    private Configuration configuration;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constant.ALERT_TYPE);

           // playSound(type);
        }
    };

   /* public void playSound(String type){
        MediaPlayer mediaPlayer = null;

        if(type.equals(Constant.TRANSACTION_ALERT)){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert);
        }else if(type.equals(Constant.CHAT_ALERT)){
            //Todo Change After
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert);
        }

        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }*/




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupConfig();
        //Google Api Client

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(Constant.ACTION);
        registerReceiver(myReceiver,intentFilter);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    public void setupConfig(){
        userLocalStore = new UserLocalStore(getApplicationContext());
        locale = new Locale(userLocalStore.getLocal());
        Locale.setDefault(locale);

        configuration  = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home){
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setUpNavigationDrawer(){
        setupToolbar();
        manager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment =
                (NavigationDrawer) manager.findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp( mDrawerLayout, toolbar);
        //getSupportActionBar().setTitle(Constant.HOME);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this,R.style.MyTheme);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }





    public void setupWindowAnimations() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // Re-enter transition is executed when returning to this activity
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_short));
            getWindow().setReenterTransition(slideTransition);
            getWindow().setExitTransition(slideTransition);
        }

    }

    public void openCropImageActivity(boolean square){

        if(square){
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }else{
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4,3)
                    .start(this);
        }


    }

    public void openImagePicker(){

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.OFF)
                .start(this);

    }



    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public void setTitle(String title){
        if(tvTitle!=null){
        }else {
            tvTitle = findViewById(R.id.title);
        }
        tvTitle.setText(title);
    }

    public UserLocalStore getLocalDatabase(){
        return this.userLocalStore;
    }

    public void transitActivity(Intent intent){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
            startActivity(intent, transitionActivityOptions.toBundle());
        }else{
            startActivity(intent);
        }


    }

    public void setupSlideAnimations() {
        // Re-enter transition is executed when returning to this activity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            getWindow().setReenterTransition(slideTransition);
            getWindow().setExitTransition(slideTransition);
        }

    }

    public void setupFadeAnimation(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Visibility enterTransition = buildEnterTransition();

            Visibility enterTransition = new Fade();
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            enterTransition.excludeTarget(100,true); // You can replace any integer for |R.id.square_red|
            getWindow().setEnterTransition(enterTransition);
        }

    }

    public int getPixelFromDp(float dp){
        Resources resources = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );

        return px;
    }

    public int getDpFromPixel(int px){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public int getScreenWidth(){

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        //int height = metrics.heightPixels;

        return width;
    }

    public int getScreenHeight(){

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;
        //int height = metrics.heightPixels;

        return height;
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public void hideKey(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void initCircularReveal(String title, String trnsitionName){

        fabAdd = findViewById(R.id.fab_add);
        mRootLayout = findViewById(R.id.root_layout);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            fabAdd.setTransitionName(trnsitionName);

            Transition transition = TransitionInflater.from(getApplicationContext())
                    .inflateTransition(R.transition.changebounds_with_arcmotion);
            getWindow().setSharedElementEnterTransition(transition);
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        transition.removeListener(this);
                    }
                    startReveal();

                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }else {
            fabAdd.setVisibility(View.INVISIBLE);
            mRootLayout.setVisibility(View.VISIBLE);
        }
    }


    public void startReveal(RelativeLayout mRootLayout, final FloatingActionButton fabAdd) {

        // get the center for the clipping circle

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = mRootLayout.getWidth() / 2;
            int cy = mRootLayout.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)

            Animator anim = ViewAnimationUtils.createCircularReveal(mRootLayout, cx, cy, 0, finalRadius);
            anim.addListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // make the view visible and start the animation
                    fabAdd.setVisibility(View.INVISIBLE);

                }
            });

            // Todo Visible Root Layout Here
            mRootLayout.setVisibility(View.VISIBLE);
            anim.start();
        }



    }

   /* public void initCircularReveal(String title, String trnsitionName){

        fabAdd = findViewById(R.id.fab_add);
        mRootLayout = findViewById(R.id.root_layout);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            fabAdd.setTransitionName(trnsitionName);

            Transition transition = TransitionInflater.from(getApplicationContext())
                    .inflateTransition(R.transition.changebounds_with_arcmotion);
            getWindow().setSharedElementEnterTransition(transition);
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        transition.removeListener(this);
                    }
                    startReveal();

                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }else {
            fabAdd.setVisibility(View.INVISIBLE);
            mRootLayout.setVisibility(View.VISIBLE);
        }
    }*/


    private void startReveal() {

        // get the center for the clipping circle

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = mRootLayout.getWidth() / 2;
            int cy = mRootLayout.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)

            Animator anim = ViewAnimationUtils.createCircularReveal(mRootLayout, cx, cy, 0, finalRadius);
            anim.addListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // make the view visible and start the animation
                    fabAdd.setVisibility(View.INVISIBLE);

                }
            });

            // Todo Visible Root Layout Here
            mRootLayout.setVisibility(View.VISIBLE);
            anim.start();
        }

    }

    public void hide(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = mRootLayout.getWidth() / 2;
            int cy = mRootLayout.getHeight() / 2;

            // get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(mRootLayout, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // Todo InVisible Root Layout Here(Must)
                    mRootLayout.setVisibility(View.INVISIBLE);
                    fabAdd.setVisibility(View.VISIBLE);
                    PrebaseActivity.super.onBackPressed();
                }
            });
            // start the animation
            anim.start();
        }else{
            mRootLayout.setVisibility(View.INVISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
            PrebaseActivity.super.onBackPressed();
        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(mRootLayout==null){
            super.onBackPressed();
        }else{
            hide();
        }

    }


}
