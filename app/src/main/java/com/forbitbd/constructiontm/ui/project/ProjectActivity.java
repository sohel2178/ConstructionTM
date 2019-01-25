package com.forbitbd.constructiontm.ui.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.ui.gantt.GanttActivity;
import com.forbitbd.constructiontm.ui.project.task.taskpager.TaskPagerFragment;
import com.forbitbd.constructiontm.ui.taskAdd.AddTaskActivity;
import com.forbitbd.constructiontm.ui.taskUpdate.UpdateTaskActivity;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends PrebaseActivity implements ProjectContract.View,View.OnClickListener {
    public static final int REQUEST_CODE=5000;
    public static final int EDIT_REQUEST_CODE=10000;

    private Project project;
    private ProjectPermission projectPermission;




    private ProjectPresenter mPresenter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private FloatingActionButton fabAdd,fabGantt;

    private List<Task> taskList;
    private int currentPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        mPresenter = new ProjectPresenter(this);


        if(getIntent().getExtras()!=null){
            project = (Project) getIntent().getExtras().getSerializable(Constant.PROJECT);
            projectPermission = (ProjectPermission) getIntent().getSerializableExtra(Constant.PROJECT_PERMISSION);
        }

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(project.getProject_name());




        //SEtup Add
        new AdUtil(this);


        initView(savedInstanceState);

        subscribeToProject();


    }

    private void subscribeToProject() {
        FirebaseMessaging.getInstance().subscribeToTopic(project.getId());
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(Bundle savedInstanceState) {


        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(taskList!=null){
                    currentPos = position;
                    mPresenter.filterData(taskList,currentPos);
                    mPresenter.calculateProgress(taskList);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fabAdd = findViewById(R.id.fab_add);
        fabGantt = findViewById(R.id.fab_gantt_chart);
        fabAdd.setOnClickListener(this);
        fabGantt.setOnClickListener(this);

        if(projectPermission!=null){
            if(projectPermission.getActivityWrite()==0){
                fabAdd.setVisibility(View.GONE);
            }
        }

        mPresenter.getAllTask(project.getId());

    }

    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
        }
        adapter.addFragment(new TaskPagerFragment(), "ALL");
        adapter.addFragment(new TaskPagerFragment(), "TODAY");
        adapter.addFragment(new TaskPagerFragment(), "RUNNING");
        adapter.addFragment(new TaskPagerFragment(), "COMPLETED");
        adapter.addFragment(new TaskPagerFragment(), "EXPIRED");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }


    public ProjectPermission getProjectPermission(){
        return this.projectPermission;
    }




    public Project getProject(){
        return project;
    }



    @Override
    public void loadActivityFragment() {
    }

    @Override
    public void clearAdapter(int position) {
        TaskPagerFragment taskPagerFragment = (TaskPagerFragment) adapter.getItem(position);
        taskPagerFragment.clearAdapter();
    }

    @Override
    public void addItem(Task task,int position) {
        TaskPagerFragment taskPagerFragment = (TaskPagerFragment) adapter.getItem(position);
        taskPagerFragment.addTask(task);
    }

    @Override
    public void updateSortedList(List<Task> taskList) {
        this.taskList =taskList;
        mPresenter.filterData(taskList,currentPos);
        mPresenter.calculateProgress(taskList);
    }

    @Override
    public void startAddTaskActivity() {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        intent.putExtra(Constant.PROJECT_ID,project.getId());

        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void startGanttChartActivity() {
        Intent intent = new Intent(getApplicationContext(), GanttActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, (Serializable) taskList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void initializeViewPager(final List<Task> taskList) {
        this.taskList =taskList;
        mPresenter.filterData(taskList,currentPos);
        mPresenter.calculateProgress(taskList);

    }

    @Override
    public void updateProgress(double fp, double pp,int taskCount) {
        TaskPagerFragment taskPagerFragment = (TaskPagerFragment) adapter.getItem(currentPos);
        taskPagerFragment.updateUI(fp,pp,taskCount);
    }

    @Override
    public void removeTask(int position) {
        taskList.remove(position);
        mPresenter.filterData(taskList,currentPos);
        mPresenter.calculateProgress(taskList);
    }

    @Override
    public void updateTask(Task task, int position) {
        taskList.set(position,task);
        mPresenter.filterData(taskList,currentPos);
        mPresenter.calculateProgress(taskList);
        //unfoldItem(position);
    }

    private void unfoldItem(int position){
        TaskPagerFragment taskPagerFragment = (TaskPagerFragment) adapter.getItem(currentPos);
        taskPagerFragment.unfoldItem(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                mPresenter.startAddTaskActivity();
                break;

            case R.id.fab_gantt_chart:
                mPresenter.startGanttChartActivity();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode==EDIT_REQUEST_CODE && resultCode==ProjectActivity.RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            mPresenter.getUpdatePosition(taskList,task);
        }

        if(requestCode==REQUEST_CODE && resultCode==ProjectActivity.RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            mPresenter.sortTaskListByStartDate(taskList,task);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void deleteTask(Task task){
        mPresenter.getRemoverdPosition(taskList,task);
    }


    public void startUpdateTask(Task task){
        Intent intent = new Intent(getApplicationContext(), UpdateTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TASK,task);
        intent.putExtras(bundle);
        startActivityForResult(intent,EDIT_REQUEST_CODE);
    }

    public void requestToUpdateTask(Task updatedTask){
        mPresenter.getUpdatePosition(taskList,updatedTask);
    }

}
