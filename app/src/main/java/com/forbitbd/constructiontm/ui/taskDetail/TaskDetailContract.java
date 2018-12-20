package com.forbitbd.constructiontm.ui.taskDetail;


import com.forbitbd.constructiontm.model.WorkDone;

import java.util.List;

public interface TaskDetailContract {
    interface Presenter{
        void destroy();
        void requestForWorkDone(String projectId, String taskId);
    }

    interface View{
        void updateWorkDone(List<WorkDone> workDoneList);
    }
}
