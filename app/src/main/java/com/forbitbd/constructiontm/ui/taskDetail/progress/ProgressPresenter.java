package com.forbitbd.constructiontm.ui.taskDetail.progress;


import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.model.WorkDone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgressPresenter implements ProgressContract.Presenter {
    private ProgressContract.View mView;

    public ProgressPresenter(ProgressContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void processList(List<WorkDone> workDoneList) {

        if(workDoneList.size()>0){
            long firstDate = MyUtil.getBeginingTime(workDoneList.get(0).getDate());
            long lastDate =MyUtil.getBeginingTime(workDoneList.get(workDoneList.size()-1).getDate());

            if(firstDate==lastDate){
                return;
            }

            List<ChartModel> chartModelList = new ArrayList<>();

            for (int i=0;i<MyUtil.getDuration(lastDate,firstDate)+1;i++){
                if(i==0){
                    ChartModel chartModel = new ChartModel(firstDate);
                    chartModelList.add(chartModel);
                }else{
                    //long day = 24*60*60*1000*i;
                    ChartModel chartModel = new ChartModel(MyUtil.getDayAfter(firstDate,i));
                    chartModelList.add(chartModel);
                }
            }

            for (ChartModel x: chartModelList){

                for (WorkDone y: workDoneList){
                    if(MyUtil.getStringDate(new Date(x.getDate())).equals(MyUtil.getStringDate(new Date(y.getDate())))){
                        x.addAmount(y.getAmount());
                    }
                }
            }

            //Cum Sum
            for (int i=0;i<chartModelList.size();i++){

                if(i!=0){
                    double currentAmount = chartModelList.get(i).getAmount();
                    chartModelList.get(i).setAmount(currentAmount+chartModelList.get(i-1).getAmount());
                }
            }

            mView.updateChart(chartModelList);



        }

    }


}
