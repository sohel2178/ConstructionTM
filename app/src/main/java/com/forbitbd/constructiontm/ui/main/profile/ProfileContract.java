package com.forbitbd.constructiontm.ui.main.profile;


import com.forbitbd.constructiontm.model.User;

public interface ProfileContract {

    interface Presenter {
        boolean validate(String companyName, String companyDesc, byte[] bytes);
        void updateInfo(String companyName, String companyDesc, byte[] bytes, boolean update);
        void browseClick();

        void requestForUser();
    }

    interface View{
        void clearPreErrors();
        void showDialog();
        void hideDialog();
        void showErrorMessage(int fieldId, String message);
        void complete();
        void openCropImageActivity();
        void updateInfo(User user);
    }
}
