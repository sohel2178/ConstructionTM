package com.forbitbd.constructiontm.ui.taskDetail.progress;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.model.WorkDone;
import com.forbitbd.constructiontm.ui.util.DateAxisFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment implements ProgressContract.View {
    private LineChart mLineChart;

    private ProgressPresenter mPresenter;
    private double volOfWorks;


    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProgressPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        initView(view);
        return view;
    }

    private void initView(View view) {
        mLineChart = view.findViewById(R.id.line_chart);
    }

    public void update(List<WorkDone> workDoneList, double volofWorks){
        this.volOfWorks= volofWorks;
        Log.d("HHHH",volofWorks+"");
        mPresenter.processList(workDoneList);
    }

    @Override
    public void updateChart(List<ChartModel> chartModelList) {

        Log.d("KKKKKK",volOfWorks+"");
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setPinchZoom(true);
        // mBarChart.setOnChartValueSelectedListener(this);

        mLineChart.setDrawGridBackground(false);
        mLineChart.getLegend().setEnabled(false);
        //mLineChart.setViewPortOffsets(60, 0, 50, 60);
        mLineChart.setExtraOffsets(10, 10, 10, 60);

        List<String> xAxisLabels = new ArrayList<>();
        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (ChartModel x: chartModelList) {
            yVals1.add(new Entry(chartModelList.indexOf(x), (float) x.getAmount()));
            xAxisLabels.add(MyUtil.getStringDate(new Date(x.getDate())));
        }


        IAxisValueFormatter xAxisFormatter = new DateAxisFormatter(xAxisLabels);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90f);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mLineChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum((float) (volOfWorks+100));

        LimitLine ll = new LimitLine((float) volOfWorks, "Work Done Limit");
        ll.setLineColor(Color.GREEN);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLACK);
        ll.setTextSize(12f);
        leftAxis.addLimitLine(ll);

        // Disable Right Axis
        mLineChart.getAxisRight().setEnabled(false);



        LineDataSet dataSet = new LineDataSet(yVals1,"Progress Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2);

        /*ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);*/

        LineData data = new LineData(dataSet);
        data.setValueTextSize(10f);
        //data.setValueTypeface(mTfLight);
        mLineChart.setData(data);
        mLineChart.animateXY(1000,1000);
        mLineChart.invalidate();
    }
}
