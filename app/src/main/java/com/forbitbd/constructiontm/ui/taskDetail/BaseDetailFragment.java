package com.forbitbd.constructiontm.ui.taskDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.forbitbd.constructiontm.model.WorkDone;

import java.util.List;

public class BaseDetailFragment extends Fragment {

    List<WorkDone> workDoneList;

    private double volOfWorks;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof TaskDetailActivity){
            TaskDetailActivity ta = (TaskDetailActivity) getActivity();
            this.workDoneList = ta.getWorkDoneList();
            this.volOfWorks = ta.getVolumeOfWorks();
        }
    }

    public List<WorkDone> getWorkdoneList(){
        return workDoneList;
    }

    public double getVolumeofWorks(){
        return volOfWorks;
    }
}
