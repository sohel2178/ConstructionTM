package com.forbitbd.constructiontm.ui.taskDetail.progress;


import com.forbitbd.constructiontm.model.WorkDone;

import java.util.List;

public interface ProgressContract {

    interface Presenter{
        void processList(List<WorkDone> workDoneList);
    }

    interface View{
        //updateChart(List<>)

       void updateChart(List<ChartModel> chartModelList);
    }
}
