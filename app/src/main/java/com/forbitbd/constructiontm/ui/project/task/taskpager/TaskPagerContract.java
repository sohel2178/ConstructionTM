package com.forbitbd.constructiontm.ui.project.task.taskpager;


import com.forbitbd.constructiontm.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskPagerContract {

    interface Presenter {
        void setupAd();
        void callViewToStartTaskDetailActivity();
        void callViewToEditTask();
        void callViewShowDialog();
        void showWorkDoneDialog(Task task);
        void deleteFromDatabase(Task task);
        void addWorkdoneToDatabase(Task task, Date date, double amount);
        void startFiltering(List<Task> taskList, int fragmentNo);
    }

    interface View{
        void setupAdCloseListener();
        void startTaskDetailActivity();
        void startEditTask();
        void deleteComplete();
        void showDeleteDialog(String title);

        void showProgressDialog();
        void showWorkDoneDialog(Task task);
        void hideDialogAndUpdateItem(double amount);
        void updateAdapter(List<Task> taskList);
    }
}
