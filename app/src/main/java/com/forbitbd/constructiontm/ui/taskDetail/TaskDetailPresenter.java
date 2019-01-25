package com.forbitbd.constructiontm.ui.taskDetail;

import android.util.Log;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.model.WorkDone;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private MyDatabaseRef myDatabaseRef;
    private TaskDetailContract.View mView;
    private Disposable workDoneDisposable;

    public TaskDetailPresenter(TaskDetailContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void destroy() {
        if (workDoneDisposable != null) {
            workDoneDisposable.dispose();
        }
    }

    @Override
    public void requestForWorkDone(String projectId, String taskId) {
        Log.d("WWWW","Main Task Id "+taskId);
        myDatabaseRef.getWorkDoneRef(projectId)
                .orderByChild("task_id")
                .equalTo(taskId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        List<WorkDone> workDoneList = new ArrayList<>();

                        for (DataSnapshot x: dataSnapshot.getChildren()){
                            WorkDone workDone = x.getValue(WorkDone.class);
                            Log.d("WWWW",MyUtil.getStringDate(new Date(workDone.getDate())));
                            Log.d("WWWW","GEt Task Id "+workDone.getTask_id());
                            workDoneList.add(workDone);
                        }

                        Collections.sort(workDoneList, new Comparator<WorkDone>() {
                            @Override
                            public int compare(WorkDone workDone, WorkDone t1) {
                                return (int) (workDone.getDate()/1000/60/60-t1.getDate()/1000/60/60);
                            }
                        });

                        mView.updateWorkDone(workDoneList);



                       /* Observable.fromIterable(workdoneList)
                                .groupBy(r-> MyUtil.getStringDate(new Date(r.getDate())))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<GroupedObservable<String, WorkDone>>() {
                                    @Override
                                    public void accept(GroupedObservable<String, WorkDone> stringWorkDoneGroupedObservable) throws Exception {

                                        Log.d("JJJJJJ",stringWorkDoneGroupedObservable.getKey());

                                    }
                                });*/
                        /*Observable<List<WorkDone>> workDoneOb = Observable.fromCallable(new Callable<List<WorkDone>>() {
                            @Override
                            public List<WorkDone> call() throws Exception {
                                return getWorkDoneList(dataSnapshot);
                            }
                        });

                        workDoneDisposable = workDoneOb.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<WorkDone>>() {
                                    @Override
                                    public void accept(List<WorkDone> workDoneList) throws Exception {
                                        mView.updateWorkDone(workDoneList);
                                    }
                                });*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private List<WorkDone> getWorkDoneList(DataSnapshot dataSnapshot){

        List<WorkDone> workDoneList = new ArrayList<>();

        for (DataSnapshot x: dataSnapshot.getChildren()){
            workDoneList.add(x.getValue(WorkDone.class));
        }

        Collections.sort(workDoneList, new Comparator<WorkDone>() {
            @Override
            public int compare(WorkDone workDone, WorkDone t1) {
                return (int) (workDone.getDate()/1000/60/60-t1.getDate()/1000/60/60);
            }
        });

        return workDoneList;

    }
}
