package com.forbitbd.constructiontm.ui.main.rateUs;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.ui.main.MainActivity;


public class RateUsFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvNotNow,tvYes;


    public RateUsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_rate_us, null);
        initView(view);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialog).create();

        alertDialog.setView(view);
        return alertDialog;
    }

    private void initView(View view) {

        tvYes = view.findViewById(R.id.yes);
        tvNotNow = view.findViewById(R.id.cancel);

        tvYes.setOnClickListener(this);
        tvNotNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yes:

                dismiss();

                Uri marketUri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
                break;

            case R.id.cancel:
                dismiss();
                if(getActivity() instanceof MainActivity){
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.finish();
                }
                break;
        }
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_us, container, false);
    }*/

}
