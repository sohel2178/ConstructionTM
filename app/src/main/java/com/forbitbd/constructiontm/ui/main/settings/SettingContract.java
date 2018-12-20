package com.forbitbd.constructiontm.ui.main.settings;

public interface SettingContract {

    interface Presenter{
        void check();
        void saveCheckedLang(int langValue);
    }

    interface View{
        void checkEnglish();
        void checkBangla();
        void complete();
    }
}
