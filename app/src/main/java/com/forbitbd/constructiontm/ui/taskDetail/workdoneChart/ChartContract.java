package com.forbitbd.constructiontm.ui.taskDetail.workdoneChart;


import com.forbitbd.constructiontm.model.WorkDone;

import java.util.List;

public interface ChartContract {
    interface Presenter{
       void processWorkDoneList(List<WorkDone> workDoneList);
       void destroy();
    }

    interface View{
        void showChart(List<WorkDone> workDoneList);
    }
}
