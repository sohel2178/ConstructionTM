package com.forbitbd.constructiontm.ui.main.nav;

public interface NavContract {

    interface Presenter{
        void onHomeClick();
        void AboutUsClick();
        void ContactUsClick();
        void logoutClick();
        void updateNavUser();
    }

    interface View{
        void loadHomeFragment();
        void updateNavUser(String name, String email, String photoUrl);

        void logout();
    }
}
