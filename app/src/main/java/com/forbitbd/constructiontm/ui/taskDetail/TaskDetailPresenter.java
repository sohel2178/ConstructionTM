package com.forbitbd.constructiontm.ui.taskDetail;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.WorkDone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
        myDatabaseRef.getWorkDoneRef(projectId)
                .orderByChild("task_id")
                .equalTo(taskId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        Observable<List<WorkDone>> workDoneOb = Observable.fromCallable(new Callable<List<WorkDone>>() {
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
                                });
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
