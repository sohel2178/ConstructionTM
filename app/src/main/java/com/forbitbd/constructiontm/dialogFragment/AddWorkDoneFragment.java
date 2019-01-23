package com.forbitbd.constructiontm.dialogFragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.MyUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddWorkDoneFragment extends DialogFragment {

    private WorkDoneAmountListener listener;

    public void setListener(WorkDoneAmountListener listener){
        this.listener= listener;
    }

    private Date selectedDate;

    private String taskName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskName = getArguments().getString(Constant.TASK);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        selectedDate = new Date();


        String titleText = this.taskName.concat(" Work Done");

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(),R.style.CustomDialogTheme).create();
        alertDialog.setTitle(ssBuilder);
        alertDialog.setCancelable(false);

        View v = getActivity().getLayoutInflater().inflate(R.layout.work_done, null);

        final EditText etDate = v.findViewById(R.id.et_date);
        final EditText etAmonut = v.findViewById(R.id.et_amount);
        final Button btnSelect = v.findViewById(R.id.select);

        etDate.setText(MyUtil.getStringDate(selectedDate));

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerBuilder builder1 = new DatePickerBuilder(getActivity(), new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        selectedDate = calendar.get(0).getTime();
                        etDate.setText(MyUtil.getStringDate(selectedDate));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                com.applandeo.materialcalendarview.DatePicker datePicker2 = builder1.build();
                datePicker2.show();
            }
        });

        //final DatePicker datePicker = v.findViewById(R.id.dialog_date_datePicker);

        alertDialog.setView(v);

        Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(R.color.indicator_4));
        }
        alertDialog.setIcon(drawable);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.indicator_4));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.indicator_4));
            }
        });



        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(etAmonut.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(), "Amount is Empty", Toast.LENGTH_SHORT).show();
                    etAmonut.requestFocus();
                    dialogInterface.cancel();
                    return;
                }

                final double amount = Double.parseDouble(etAmonut.getText().toString().trim());

                if(listener!=null){
                    listener.getAmount(amount,selectedDate);
                }


            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.indicator_4));
                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.indicator_4));
            }
        });

        return alertDialog;
    }

}
