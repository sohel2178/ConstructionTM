package com.forbitbd.constructiontm.ui.project;


import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;

import java.util.List;

public interface ProjectContract {

    interface Presenter{
        void getAllTask(String projectId);
        void startAddTaskActivity();
        void startGanttChartActivity();
        void filterData(List<Task> taskList,int position);

        void calculateProgress(List<Task> taskList);

        void sortTaskListByStartDate(List<Task> taskList,Task task);
        void getRemoverdPosition(List<Task> taskList,Task task);
        void getUpdatePosition(List<Task> taskList,Task task);
    }

    interface View{
        void loadActivityFragment();

        void clearAdapter(int position);
        void addItem(Task task,int position);
        void updateSortedList(List<Task> taskList);

        void startAddTaskActivity();
        void startGanttChartActivity();
        void initializeViewPager(List<Task> taskList);

        void updateProgress(double fp, double pp,int taskCount);
        void removeTask(int position);
        void updateTask(Task task ,int position);
    }
}
