package com.forbitbd.constructiontm.utility;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.database.MyStorageRef;
import com.forbitbd.constructiontm.ui.main.MainActivity;


/**
 * Created by Genius 03 on 1/22/2018.
 */

public class BaseFragment extends Fragment {

    private MyDatabaseRef myDatabaseRef;
    private MyStorageRef myStorageRef;

    public void dummy(final boolean isVisible, final View bannerSlider, View btnTransitionTest, ViewGroup rlContainer){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition btnTr = new ChangeBounds();
            btnTr.setPathMotion(new ArcMotion());


            Transition transition = new TransitionSet()

                    .addTransition(new Slide(Gravity.TOP))
                    .addTransition(btnTr)
                    .addListener(new Transition.TransitionListener() {
                        @Override
                        public void onTransitionStart(Transition transition) {
                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {

                            if(!isVisible){
                                bannerSlider.setVisibility(View.GONE);
                            }

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


            TransitionManager.beginDelayedTransition(rlContainer,transition);


            bannerSlider.setVisibility(isVisible ? View.VISIBLE: View.INVISIBLE);

            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) btnTransitionTest.getLayoutParams();

            if(isVisible){
                Log.d("GGGG","Called");
                //param.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
                rlContainer.addView(bannerSlider,0);


            }else{
                rlContainer.removeViewAt(0);

                //param.gravity=Gravity.TOP|Gravity.CENTER_HORIZONTAL;
            }

            btnTransitionTest.setLayoutParams(param);


        }



    }


    public UserLocalStore getLocalDatabase(){

        UserLocalStore userLocalStore = null;

        if(getActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            userLocalStore = activity.getLocalDatabase();
        }

        return userLocalStore;
    }


    /*public FirebaseUser getCurrentUser(){
        FirebaseUser firebaseUser = null;
        if(getActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            firebaseUser = activity.getCurrentUser();
        }
        return firebaseUser;
    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.myDatabaseRef = new MyDatabaseRef();
        this.myStorageRef = new MyStorageRef();
    }

    public MyDatabaseRef getDatabaseRef(){
        if(myDatabaseRef!=null){
            return myDatabaseRef;
        }else {
            return new MyDatabaseRef();
        }
    }

    public MyStorageRef getStorageRef(){
        if(myStorageRef!=null){
            return myStorageRef;
        }else {
            return new MyStorageRef();
        }
    }
}
