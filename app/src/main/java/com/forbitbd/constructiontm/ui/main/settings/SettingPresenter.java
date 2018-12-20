package com.forbitbd.constructiontm.ui.main.settings;


import com.forbitbd.constructiontm.utility.UserLocalStore;

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;
    private UserLocalStore userLocalStore;

    public SettingPresenter(SettingContract.View mView,UserLocalStore userLocalStore) {
        this.mView = mView;
        this.userLocalStore = userLocalStore;
    }

    @Override
    public void check() {

        if(userLocalStore.getLocal().equals(UserLocalStore.ENGLISH_LOCALE)){
            mView.checkEnglish();
        }else if(userLocalStore.getLocal().equals(UserLocalStore.BANGLA_LOCAL)){
            mView.checkBangla();
        }

    }

    @Override
    public void saveCheckedLang(int language) {
        if(language==0){
            userLocalStore.setEnglishLocal();
        }else{
            userLocalStore.setBanglaLocal();
        }


        mView.complete();
    }
}
