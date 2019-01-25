package com.forbitbd.constructiontm.ui.taskDetail.workdoneTable;

import com.forbitbd.constructiontm.model.DailyWorkdone;
import com.forbitbd.constructiontm.model.WorkDone;

import java.util.List;

public interface WorkDoneTableContract {

    interface Presenter{
        void processData(List<WorkDone> workDoneList);
    }

    interface View{
        void addItem(DailyWorkdone dailyWorkdone);
        void clearAdpter();
    }
}
