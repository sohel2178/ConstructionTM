package com.forbitbd.constructiontm.ui.project;


import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;

import java.util.List;

public interface ProjectContract {

    interface Presenter{
        void loadRightFragment(ProjectPermission projectPermission);

        void getAllTask(String projectId);
        void startAddTaskActivity();
        void filterData(List<Task> taskList,int position);
    }

    interface View{
        void showErrorToast(String message);
        void loadActivityFragment();

        void clearAdapter(int position);
        void addItem(Task task,int position);

        void startAddTaskActivity();
        void initializeViewPager(List<Task> taskList);
    }
}
