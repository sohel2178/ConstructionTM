package com.forbitbd.constructiontm.ui.main.aboutUs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.forbitbd.constructiontm.R;

import mehdi.sakout.aboutpage.AboutPage;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setDescription("SOHEL AHMED\nCivil Engineer\nand\nAndroid Developer")
                .setImage(R.drawable.profile)
                .addGroup("Connect with us")
                .addEmail("sohel.ahmed2178@gmail.com")
                //.addWebsite("http://techfastit.com")
                .addYoutube("UCc8SZuryOonRdGiLi-jd07Q")
                .addPlayStore("https://play.google.com/store/apps/details?id="+getContext().getPackageName())
                .create();


        return aboutPage;
    }

}
