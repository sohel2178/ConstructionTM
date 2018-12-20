package com.forbitbd.constructiontm.ui.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdFragment extends Fragment {

    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInterAd();
    }

    private void setupInterAd() {
        MobileAds.initialize(getContext(),
                getString(R.string.ad_app_id));

        mInterstitialAd = new InterstitialAd(getContext());
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id_inters));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public InterstitialAd getmInterstitialAd() {
        return mInterstitialAd;
    }

    public void showAd(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void startNewActivity(Class activity, String projectId){
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra(Constant.PROJECT_ID,projectId);

        startActivity(intent);
    }
}
