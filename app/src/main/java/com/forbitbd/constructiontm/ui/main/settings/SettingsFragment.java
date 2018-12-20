package com.forbitbd.constructiontm.ui.main.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.UserLocalStore;
import com.forbitbd.constructiontm.ui.main.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements SettingContract.View,View.OnClickListener {

    private RadioButton mREnglish,mRBangla;
    private Button btnUpdate;

    private SettingPresenter mPresenter;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLocalStore userLocalStore = new UserLocalStore(getContext());

        mPresenter = new SettingPresenter(this,userLocalStore);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mREnglish = view.findViewById(R.id.radioEnglish);
        mRBangla = view.findViewById(R.id.radioBangla);
        btnUpdate = view.findViewById(R.id.update);
        btnUpdate.setOnClickListener(this);

        mPresenter.check();
    }

    @Override
    public void checkEnglish() {
        mREnglish.setChecked(true);
    }

    @Override
    public void checkBangla() {
        mRBangla.setChecked(true);
    }


    @Override
    public void complete() {
        if(getActivity() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.recreate();
        }
    }

    @Override
    public void onClick(View view) {
        int langVal=0;
        int currencyValue=0;
        if(mREnglish.isChecked()){
            langVal=0;
        }else{
            langVal=1;
        }


        mPresenter.saveCheckedLang(langVal);
    }
}
