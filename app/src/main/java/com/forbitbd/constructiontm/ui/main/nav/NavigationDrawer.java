package com.forbitbd.constructiontm.ui.main.nav;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.ui.login.LoginActivity;
import com.forbitbd.constructiontm.ui.main.MainActivity;
import com.forbitbd.constructiontm.ui.main.aboutUs.AboutUsFragment;
import com.forbitbd.constructiontm.ui.main.home.HomeFragment;
import com.forbitbd.constructiontm.ui.main.profile.ProfileFragment;
import com.forbitbd.constructiontm.ui.main.settings.SettingsFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment implements View.OnClickListener,NavContract.View {

    public static final String PREF_NAME ="mypref";
    public static final String KEY_USER_LEARNED_DRAWERR="user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;


    // View Initialize Here
    private CircleImageView ivProfile;
    private TextView tvName,tvEmail;


    private LinearLayout rvHome, rvAboutUs, rvUpdateInfo,rvSettings, rvLogOut;

    private NavPresenter mPresenter;


    public NavigationDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,"false"));

        // if saveInstanceState is not null its coming back from rotation
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }

        mPresenter = new NavPresenter(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        //Initialize View

        initView(view);

        return view;
    }

    private void initView(View view) {

        ivProfile = view.findViewById(R.id.iv_profile);
        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);

        rvHome = view.findViewById(R.id.home);
        rvAboutUs = view.findViewById(R.id.about_us);
        rvUpdateInfo = view.findViewById(R.id.update_info);
        rvSettings = view.findViewById(R.id.settings);
        rvLogOut = view.findViewById(R.id.logout);

        rvHome.setOnClickListener(this);
        rvAboutUs.setOnClickListener(this);
        rvUpdateInfo.setOnClickListener(this);
        rvSettings.setOnClickListener(this);
        rvLogOut.setOnClickListener(this);

        mPresenter.updateNavUser();


       /* if(mUser!=null){
            if(mUser.getPhotoUrl()!=null){
                Picasso.with(getContext())
                        .load(mUser.getPhotoUrl())
                        .into(ivProfile);
            }

            if(mUser.getDisplayName()!=null){
                tvName.setText(mUser.getDisplayName());
            }

            if(mUser.getEmail()!=null){
                tvEmail.setText(mUser.getEmail());
            }
        }*/




    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public void setUp(DrawerLayout layout, final Toolbar toolbar) {

        mDrawerLayout = layout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //if user gonna not seen the drawer before thats mean the drawer is open for the first time

                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    // save it in sharedpreferences
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,mUserLearnedDrawer+"");

                    getActivity().invalidateOptionsMenu();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public static void saveToPreferences(Context context, String key, String prefValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String key, String defaultValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(key,defaultValue);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.home:
                mPresenter.onHomeClick();
                break;
            case R.id.about_us:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof AboutUsFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new AboutUsFragment())
                            .commit();
                }

               //startActivity(new Intent(getContext(), NotificationActivity.class));

                break;
            case R.id.update_info:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof ProfileFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new ProfileFragment()).commit();
                }
                /*if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof EngineeringSupportFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new EngineeringSupportFragment()).commit();
                }*/
                break;

            case R.id.settings:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof SettingsFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new SettingsFragment()).commit();
                }
                /*if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof EngineeringSupportFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new EngineeringSupportFragment()).commit();
                }*/
                break;
            case R.id.logout:
                mDrawerLayout.closeDrawer(Gravity.START);
                if(getActivity() instanceof MainActivity){
                    MainActivity activity = (MainActivity) getActivity();
                    activity.signOut();
                    activity.getLocalDatabase().setIsUserSync(false);
                    activity.startLoginActivity();
                }

                break;

        }
    }

    @Override
    public void loadHomeFragment() {
        closeDrawer();
         if(!(getFragmentManager().findFragmentById(R.id.main_container) instanceof HomeFragment)){
             getFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
         }
    }

    @Override
    public void updateNavUser(String name, String email, String photoUrl) {
        tvName.setText(name);
        tvEmail.setText(email);

        if(photoUrl !=null && !photoUrl.equals("")){
            Picasso.with(getContext())
                    .load(photoUrl)
                    .into(ivProfile);
        }
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void logout() {
        closeDrawer();
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.signOut();
            activity.finish();
            activity.getLocalDatabase().setIsUserSync(false);
        }
        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}
