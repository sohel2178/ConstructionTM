package com.forbitbd.constructiontm.ui.taskDetail.workdoneTable;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.DailyWorkdone;
import com.forbitbd.constructiontm.model.WorkDone;
import com.forbitbd.constructiontm.ui.taskDetail.BaseDetailFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkDoneTableFragment extends BaseDetailFragment implements WorkDoneTableContract.View {

    private RecyclerView rvWorkdone;
    private WorkDoneAdapter adapter;

    private WorkdoneTablePresenter mPresenter;


    public WorkDoneTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new WorkdoneTablePresenter(this);

       /* List<WorkDone> workDoneList = (List<WorkDone>) getArguments().getSerializable(Constant.WORK_DONE_LIST);
        String unit = getArguments().getString(Constant.UNIT);*/

        adapter = new WorkDoneAdapter(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_done_table, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvWorkdone = view.findViewById(R.id.rv_workdone);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvWorkdone.setLayoutManager(manager);
        rvWorkdone.addItemDecoration(new DividerItemDecoration(getContext(),manager.getOrientation()));
        rvWorkdone.setAdapter(adapter);

        mPresenter.processData(getWorkdoneList());
    }


    @Override
    public void addItem(DailyWorkdone dailyWorkdone) {
        adapter.addItem(dailyWorkdone);
    }

    @Override
    public void clearAdpter() {
        adapter.clear();
    }
}
