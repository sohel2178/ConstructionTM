package com.forbitbd.constructiontm.ui.taskDetail.workdoneTable;

import android.util.Log;

import com.forbitbd.constructiontm.model.DailyWorkdone;
import com.forbitbd.constructiontm.model.WorkDone;
import com.forbitbd.constructiontm.utility.MyUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

public class WorkdoneTablePresenter implements WorkDoneTableContract.Presenter {

    private WorkDoneTableContract.View mvView;

    private List<DailyWorkdone> dailyWorkdoneList;

    public WorkdoneTablePresenter(WorkDoneTableContract.View mvView) {
        this.mvView = mvView;
        this.dailyWorkdoneList = new ArrayList<>();
    }

    @Override
    public void processData(List<WorkDone> workDoneList) {
        Log.d("SohelMAMA",workDoneList.size()+"");

        mvView.clearAdpter();

        dailyWorkdoneList.clear();

          Observable.fromIterable(workDoneList)
                    .groupBy(r-> MyUtil.getStringDate(new Date(r.getDate())))
                    .flatMapSingle(Observable::toList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(group-> processGroup(group)
                            ,err->err.printStackTrace()
                    ,()->sortData());
    }

    private void processGroup(List<WorkDone> workDoneList){
        DailyWorkdone dailyWorkdone = new DailyWorkdone(MyUtil.getStringDate(new Date(workDoneList.get(0).getDate())));

        for (WorkDone x: workDoneList){
            dailyWorkdone.add(x.getAmount());
        }

        dailyWorkdoneList.add(dailyWorkdone);

       //
    }

    private void sortData(){
        Log.d("HHHHHH","Complete Called");

        Collections.sort(dailyWorkdoneList, new Comparator<DailyWorkdone>() {
            @Override
            public int compare(DailyWorkdone dailyWorkdone, DailyWorkdone t1) {
                try {
                    return MyUtil.getDate(dailyWorkdone.getDate()).compareTo(MyUtil.getDate(t1.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        for (DailyWorkdone dailyWorkdone :dailyWorkdoneList){
            mvView.addItem(dailyWorkdone);
        }
    }
}
