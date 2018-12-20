package com.forbitbd.constructiontm.ui.main.nav;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavPresenter implements NavContract.Presenter {

    private NavContract.View mView;

    public NavPresenter(NavContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onHomeClick() {
        mView.loadHomeFragment();
    }

    @Override
    public void AboutUsClick() {

    }

    @Override
    public void ContactUsClick() {

    }

    @Override
    public void logoutClick() {
        mView.logout();
    }

    @Override
    public void updateNavUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            String userName = user.getDisplayName();
            String email = user.getEmail();
            String photoUrl = user.getPhotoUrl().toString();
            mView.updateNavUser(userName,email,photoUrl);
        }



    }
}
