package com.forbitbd.constructiontm.ui.project;


import android.util.Log;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class ProjectPresenter implements ProjectContract.Presenter {

    private ProjectContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public ProjectPresenter(ProjectContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void loadRightFragment(ProjectPermission projectPermission) {
        mView.loadActivityFragment();
        /*if(projectPermission==null){
            mView.loadActivityFragment();
        }else{
            if(projectPermission.getActivityRead()==1){

            }else if(projectPermission.getFinanceRead()==1){
                mView.loadFinanceFragment();
            }else if(projectPermission.getStoreRead()==1){
                mView.loadStoreFragment();
            }else if(projectPermission.getEmployeeRead()==1){
                mView.loadEmployeeFragment();
            }
        }*/
    }

    @Override
    public void getAllTask(String projectId) {
        myDatabaseRef.getTaskRef(projectId)
                .orderByChild("task_start_date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Task> taskList = new ArrayList<>();
                for (DataSnapshot x: dataSnapshot.getChildren()){
                    Task task = x.getValue(Task.class);
                    taskList.add(task);
                    //Log.d("HHHHH",MyUtil.getStringDate(new Date(task.getTask_start_date())));
                }
                mView.initializeViewPager(taskList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void startAddTaskActivity() {
        mView.startAddTaskActivity();
    }

    @Override
    public void filterData(List<Task> taskList, final int position) {
        mView.clearAdapter(position);
        final Date date = new Date();
        Observable.fromIterable(taskList)
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        switch (position){
                            case 0:
                                return true;
                            case 1:
                                boolean value = MyUtil.getBeginingTime(task.getTask_start_date()) <= date.getTime()
                                        && MyUtil.getEndingTime(task.getTask_finished_date()) >= date.getTime()
                                        && task.getTask_volume_of_work_done()!=task.getTask_volume_of_works();
                                return value;

                            case 2:
                                return MyUtil.getEndingTime(task.getTask_finished_date())>date.getTime()
                                        && task.getTask_volume_of_work_done()!=task.getTask_volume_of_works();

                            case 3:
                                return task.getTask_volume_of_work_done()==task.getTask_volume_of_works();

                            case 4:
                                return MyUtil.getEndingTime(task.getTask_finished_date())<date.getTime()
                                        && task.getTask_volume_of_work_done()!=task.getTask_volume_of_works();

                        }
                        return false;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Task>() {
                    @Override
                    public void accept(Task task) throws Exception {
                        mView.addItem(task,position);
                    }
                });
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

        mView.updateProgress(financialProgress,physicalProgress,taskList.size());
    }

    @Override
    public void sortTaskListByStartDate(List<Task> taskList, Task task) {
        taskList.add(task);
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return (int) (t1.getTask_start_date()-t2.getTask_start_date());
            }
        });
        mView.updateSortedList(taskList);

    }

    @Override
    public void getRemoverdPosition(List<Task> taskList, Task task) {
        for (Task x: taskList){
            if(x.getTask_id().equals(task.getTask_id())){
                mView.removeTask(taskList.indexOf(x));
                break;
            }
        }
    }

    @Override
    public void getUpdatePosition(List<Task> taskList, Task task) {
        for (Task x: taskList){
            if(x.getTask_id().equals(task.getTask_id())){
                mView.updateTask(task,taskList.indexOf(x));
                break;
            }
        }
    }


}
