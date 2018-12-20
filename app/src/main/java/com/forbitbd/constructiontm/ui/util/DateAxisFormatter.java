package com.forbitbd.constructiontm.ui.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by sohel on 20-04-18.
 */

public class DateAxisFormatter implements IAxisValueFormatter {

    private List<String> dateList;

    public DateAxisFormatter(List<String> dateList) {
        this.dateList = dateList;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        return dateList.get(index);
    }
}
