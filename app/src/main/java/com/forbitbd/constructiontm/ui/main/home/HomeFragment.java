package com.forbitbd.constructiontm.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.User;
import com.forbitbd.constructiontm.ui.main.MainActivity;
import com.forbitbd.constructiontm.ui.main.home.ownProject.OwnProjectFragment;
import com.forbitbd.constructiontm.ui.main.home.sharedProject.SharedProjectFragment;
import com.forbitbd.constructiontm.ui.projectAdd.AddProjectActivity;
import com.forbitbd.constructiontm.ui.util.AdFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.springindicator.SpringIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends AdFragment implements
        AppBarLayout.OnOffsetChangedListener, View.OnClickListener,HomeContract.View{

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;

    private TextView tvCompanyName,tvCompanyDesc;

    private FloatingActionButton btnAdd;

    private TabsAdapter adapter;

    private HomePresenter mPresenter;

    private int counter;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAd();
        mPresenter = new HomePresenter(this);
    }

    private void setupAd() {
        getmInterstitialAd().setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getmInterstitialAd().loadAd(new AdRequest.Builder().build());

                mPresenter.callCreateProjectActivity();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setTitle(getString(R.string.app_name));
            ma.setupConfig();
        }
    }

    private void initView(View view) {

        SpringIndicator springIndicator = (SpringIndicator) view.findViewById(R.id.indicator);
        ViewPager viewPager  =  view.findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout =  view.findViewById(R.id.materialup_appbar);
        mProfileImage =  view.findViewById(R.id.materialup_profile_image);



        tvCompanyName = view.findViewById(R.id.tv_company_name);
        tvCompanyDesc = view.findViewById(R.id.tv_company_desc);

        btnAdd = view.findViewById(R.id.fab_add);
        btnAdd.setOnClickListener(this);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        adapter = new TabsAdapter(getChildFragmentManager());
        adapter.addFragment(new OwnProjectFragment());
        adapter.addFragment(new SharedProjectFragment());


        viewPager.setAdapter(adapter);
        springIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("TEST",position+" onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                //Log.d("TEST",position+" onPageSelected");

                if(position==1){
                    btnAdd.setVisibility(View.GONE);
                    setTitle(getString(R.string.shared_project_list));

                }else{
                    btnAdd.setVisibility(View.VISIBLE);
                    setTitle(getString(R.string.project_list));

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPresenter.getUserFromDatabase();


    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0){
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
        }


        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }

    }

    @Override
    public void onClick(View view) {
        mPresenter.callForShowingAd();
    }

    @Override
    public void updateUI(User user) {
        if(!user.getCompany_logo().equals("")){
            Picasso.with(getContext())
                    .load(user.getCompany_logo())
                    .into(mProfileImage);
        }

        if(user.getCompanyName()==null || user.getCompanyName().equals("")){
            tvCompanyName.setText(R.string.update_from_nav);
        }else{
            tvCompanyName.setText(user.getCompanyName());
        }

        if(user.getCompanyDesc()==null || user.getCompanyDesc().equals("")){
            tvCompanyDesc.setText(R.string.update_from_nav);
        }else {
            tvCompanyDesc.setText(user.getCompanyDesc());
        }



    }

    @Override
    public void startCreateProjectActivity() {

        Intent intent = new Intent(getContext(),AddProjectActivity.class);
        startActivity(intent);

    }

    @Override
    public void showInterAd() {
        counter++;

        if(counter%2==0){
            if(getmInterstitialAd().isLoaded()){
                showAd();
            }else {
                getmInterstitialAd().loadAd(new AdRequest.Builder().build());
                mPresenter.callCreateProjectActivity();
            }
        }else{
            mPresenter.callCreateProjectActivity();
        }


    }


    private void setTitle(String title){
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setTitle(title);
        }
    }


    public class TabsAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        TabsAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        public void addFragment(Fragment fragment){
            fragmentList.add(fragment);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String bal="";
            switch (position){
                case 0:
                    bal=getString(R.string.my_project);
                    break;

                case 1:
                    bal =getString(R.string.share_project);
                    break;
            }
            return bal;
        }
    }

    public TabsAdapter getAdapter() {
        return adapter;
    }
}
