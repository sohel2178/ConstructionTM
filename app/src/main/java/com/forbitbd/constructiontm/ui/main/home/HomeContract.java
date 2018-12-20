package com.forbitbd.constructiontm.ui.main.home;


import com.forbitbd.constructiontm.model.User;

public interface HomeContract {

    interface Presenter{
        void getUserFromDatabase();
        void callCreateProjectActivity();
        void callForShowingAd();
    }

    interface View{
        void updateUI(User user);
        void startCreateProjectActivity();
        void showInterAd();
    }
}
