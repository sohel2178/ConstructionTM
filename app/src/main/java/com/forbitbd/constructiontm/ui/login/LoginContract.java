package com.forbitbd.constructiontm.ui.login;


public interface LoginContract {

    interface Presenter{
        void loginClick();
    }

    interface View {
        void googleSignIn();
        void showDialog();
        void handledatabaseError();
        void hideDialogandFinish();
        void showAutheticationFailureToast();

    }
}
