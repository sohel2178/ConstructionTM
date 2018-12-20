package com.forbitbd.constructiontm.ui.project.task.taskpager;

import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.model.WorkDone;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskPagerPresenter implements TaskPagerContract.Presenter {

    private TaskPagerContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public TaskPagerPresenter(TaskPagerContract.View mView) {
        this.mView = mView;
        myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void setupAd() {
        mView.setupAdCloseListener();
    }

    @Override
    public void callViewToStartTaskDetailActivity() {
        mView.startTaskDetailActivity();
    }

    @Override
    public void callViewToEditTask() {
        mView.startEditTask();
    }

    @Override
    public void callViewShowDialog() {
        mView.showDeleteDialog("Alert");
    }

    @Override
    public void showWorkDoneDialog(Task task) {
        mView.showWorkDoneDialog(task);
    }

    @Override
    public void deleteFromDatabase(Task task) {
        mView.showProgressDialog();
        myDatabaseRef.getTaskRef(task.getProject_id()).child(task.getTask_id())
                .setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mView.deleteComplete();
                    }
                });
    }

    @Override
    public void addWorkdoneToDatabase(final Task task, Date date, final double amount) {
        String key = myDatabaseRef.getWorkDoneRef(task.getProject_id()).push().getKey();

        WorkDone workDone = new WorkDone(task.getTask_id(),amount);
        workDone.setDate(date.getTime());
        workDone.setWork_done_id(key);

        mView.showProgressDialog();

        myDatabaseRef.getWorkDoneRef(task.getProject_id())
                .child(key).setValue(workDone, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                myDatabaseRef.getTaskRef(task.getProject_id())
                        .child(task.getTask_id()).child("task_volume_of_work_done")
                        .setValue(task.getTask_volume_of_work_done() + amount, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                mView.hideDialogAndUpdateItem(amount);

                            }
                        });
            }
        });



    }

    @Override
    public void startFiltering(List<Task> taskList, int fragmentNo) {
        Date date = new Date();
        List<Task> tempList = new ArrayList<>();
        switch (fragmentNo){
            case 0:
                mView.updateAdapter(taskList);
                break;

            case 1:
                for(Task x: taskList){
                    if(MyUtil.getBeginingTime(x.getTask_start_date()) <= date.getTime()
                            && MyUtil.getEndingTime(x.getTask_finished_date()) >= date.getTime()
                            && x.getTask_volume_of_work_done()!=x.getTask_volume_of_works()){
                        tempList.add(x);
                    }
                }
                mView.updateAdapter(tempList);

               break;

            case 2:

                for(Task x: taskList){
                    if(MyUtil.getEndingTime(x.getTask_finished_date())>date.getTime()
                            && x.getTask_volume_of_work_done()!=x.getTask_volume_of_works()){

                        tempList.add(x);
                    }
                }
                mView.updateAdapter(tempList);


                break;

            case 3:
                for(Task x: taskList){
                    if(x.getTask_volume_of_work_done()==x.getTask_volume_of_works()){
                        tempList.add(x);
                    }
                }
                mView.updateAdapter(tempList);
                break;

            case 4:
                for(Task x: taskList){
                    if(MyUtil.getEndingTime(x.getTask_finished_date())<date.getTime()
                            && x.getTask_volume_of_work_done()!=x.getTask_volume_of_works()){

                        tempList.add(x);
                    }
                }
                mView.updateAdapter(tempList);
                break;

        }
    }
}
