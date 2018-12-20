package com.forbitbd.constructiontm.ui.project.task;


import com.forbitbd.constructiontm.model.Task;

import java.util.List;

public interface TaskContract {

    interface Presenter{
        void requestDataFromServer(String projectId);
        void buttonClick(int action);
        void requestToshowAd(int action);
        void calculateProgress(List<Task> taskList);
        void getUpdatedList(List<Task> taskList, Task task);
    }

    interface View{
        void updateUi(List<Task> taskList);
        void startNewTaskActivityAndGetResult();
        void startGanttChartActivity();
        void showAdvert(int action);
        void updateProgress(double fp, double pp);
    }
}
