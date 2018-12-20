package com.forbitbd.constructiontm.dialogFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.forbitbd.constructiontm.utility.Constant;

public class MyDateDialog extends MyDatePickerFragment {

    private String titleText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.titleText = getArguments().getString(Constant.TITLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog alertDialog = super.onCreateDialog(savedInstanceState);

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
        alertDialog.setTitle(ssBuilder);
        return alertDialog;
    }
}
