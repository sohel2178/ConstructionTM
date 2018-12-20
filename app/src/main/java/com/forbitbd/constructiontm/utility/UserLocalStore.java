package com.forbitbd.constructiontm.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Genius 03 on 1/21/2018.
 */

public class UserLocalStore {

    private static final String SP_NAME ="userDetails";
    private static final String IS_USER_SYNC ="IS_USER_SYNC";
    private static final String COMPANY_NAME ="COMPANY_NAME";
    private static final String COMPANY_LOGO ="COMPANY_LOGO";
    private static final String COMPANY_DESC="COMPANY_DESC";

    public static final String BANGLA_LOCAL = "bn";
    public static final String ENGLISH_LOCALE = "en_US";
    public static final String LOCALE = "LOCALE";

    public static final String BANGLA_CURRENCY = "৳";
    public static final String ENGLISH_CURRENCY  = "$";
    public static final String CURRENCY  = "CURRENCY";

    private SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public boolean isUserSync(){
        return userLocalDatabase.getBoolean(IS_USER_SYNC,false);
    }

    public void setIsUserSync(boolean value){
        userLocalDatabase.edit().putBoolean(IS_USER_SYNC,value).apply();
    }

    public String getCompanyName(){
        return userLocalDatabase.getString(COMPANY_NAME,"");
    }

    public void setCompanyName(String companyName){
        userLocalDatabase.edit().putString(COMPANY_NAME,companyName).apply();
    }

    public String getCompanyLogo(){
        return userLocalDatabase.getString(COMPANY_LOGO,"");
    }

    public void setCompanyLogo(String companyLogo){
        userLocalDatabase.edit().putString(COMPANY_LOGO,companyLogo).apply();
    }

    public String getCompanyDesc(){
        return userLocalDatabase.getString(COMPANY_DESC,"");
    }

    public void setCompanyDesc(String companyDesc){
        userLocalDatabase.edit().putString(COMPANY_DESC,companyDesc).apply();
    }


    public String getLocal(){
        return userLocalDatabase.getString(LOCALE,ENGLISH_LOCALE);
    }

    public void setBanglaLocal(){
        userLocalDatabase.edit().putString(LOCALE,BANGLA_LOCAL).apply();
    }

    public void setEnglishLocal(){
        userLocalDatabase.edit().putString(LOCALE,ENGLISH_LOCALE).apply();
    }

    public String getCurrency(){
        String cur ="";

        if(getLocal().equals(ENGLISH_LOCALE)){
            cur = ENGLISH_CURRENCY;
        }else if(getLocal().equals(BANGLA_LOCAL)){
            cur = BANGLA_CURRENCY;
        }
        return cur;
    }
}
