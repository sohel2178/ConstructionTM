package com.forbitbd.constructiontm.ui.project.task;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskPresenter implements TaskContract.Presenter {
    private TaskContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public TaskPresenter(TaskContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void requestDataFromServer(String projectId) {
        myDatabaseRef.getTaskRef(projectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               processSnapShot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void buttonClick(int action) {
        switch (action){
            case 1:
                mView.startNewTaskActivityAndGetResult();
                break;

            case 2:
                mView.startGanttChartActivity();
                break;
        }

    }

    @Override
    public void requestToshowAd(int action) {
        mView.showAdvert(action);
    }

    @Override
    public void calculateProgress(List<Task> taskList) {
        double totalWorkdone=0;
        double workTobeDone =0;
        double ph=0;

        for(Task task:taskList){
            workTobeDone = workTobeDone+task.getTask_volume_of_works()*task.getTask_unit_rate();
            totalWorkdone = totalWorkdone+task.getTask_volume_of_work_done()*task.getTask_unit_rate();
            ph = ph+(task.getTask_volume_of_work_done()/task.getTask_volume_of_works());
        }

        double financialProgress = totalWorkdone/workTobeDone*100;
        double physicalProgress = ph/taskList.size()*100;

        mView.updateProgress(financialProgress,physicalProgress);
    }

    @Override
    public void getUpdatedList(List<Task> taskList, Task task) {
        int pos = -1;

        for (Task x:taskList){
            if(x.getTask_id().equals(task.getTask_id())){
                pos = taskList.indexOf(x);
                break;
            }
        }

        taskList.remove(pos);
        taskList.add(pos,task);

        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return (int) (t1.getTask_start_date()-t2.getTask_start_date());
            }
        });

        mView.updateUi(taskList);
    }

    private void processSnapShot(DataSnapshot dataSnapshot){
        List<Task> taskList = new ArrayList<>();

        for (DataSnapshot x: dataSnapshot.getChildren()){
            taskList.add(x.getValue(Task.class));
        }

        // Sorting List By Start Date
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
               // Log.d("TTTT",(t1.getTask_start_date()-t2.getTask_start_date())/1000/60/60/24+"");
                return (int) (t1.getTask_start_date()-t2.getTask_start_date());
            }
        });

        mView.updateUi(taskList);
    }
}
