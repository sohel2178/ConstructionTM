package com.forbitbd.constructiontm.ui.gantt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.model.Task;

import java.util.List;

public class GanttActivity extends AppCompatActivity {

    TableView tableView;
    private AbstractTableAdapter mTableViewAdapter;
    private TableViewModel mTableViewModel;
    private RelativeLayout mHideContainer;

    private List<Task> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gantt);

        taskList = (List<Task>) getIntent().getSerializableExtra(Constant.DATA);

        Log.d("HHHHHHH",taskList.size()+"");

        initView();
    }

    private void initView() {
        tableView = findViewById(R.id.tableview);
        mHideContainer = findViewById(R.id.hide_container);

        if(taskList!=null && taskList.size()>0){
            initializeTableView(taskList);
        }else {
            mHideContainer.setVisibility(View.VISIBLE);
        }

    }

    private void initializeTableView(List<Task> taskList) {
        // Create TableView View model class  to group view models of TableView
        mTableViewModel = new TableViewModel(getApplicationContext(),taskList);

        // Create TableView Adapter
        mTableViewAdapter = new TableViewAdapter(getApplicationContext(), mTableViewModel);

        tableView.setAdapter(mTableViewAdapter);
        tableView.setTableViewListener(new TableViewListener(tableView));

        // Create an instance of a Filter and pass the TableView.
        //mTableFilter = new Filter(mTableView);

        // Load the dummy data to the TableView
        mTableViewAdapter.setAllItems(mTableViewModel.getColumnHeaderList(), mTableViewModel
                .getRowHeaderList(), mTableViewModel.getCellList());


    }
}
