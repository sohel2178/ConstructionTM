package com.forbitbd.constructiontm.ui.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.ui.project.task.taskpager.TaskPagerFragment;
import com.forbitbd.constructiontm.ui.taskAdd.AddTaskActivity;
import com.forbitbd.constructiontm.ui.taskDetail.TaskDetailActivity;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.ui.project.task.ActivityFragment;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectActivity extends PrebaseActivity implements ProjectContract.View,View.OnClickListener {
    public static final int REQUEST_CODE=5000;

    private Project project;
    private ProjectPermission projectPermission;

    private AppBarLayout mAppBarLayout;

    private TextView tvProjectLocation,tvProjectDescription,tvList;

    private ProjectPresenter mPresenter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private FloatingActionButton fabAdd,fabGantt;

    private List<Task> taskList;
    private int currentPos;

    private FoldingCell mFoldingCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
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

        Log.d("HHHHH",project.getId());

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home){
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(Bundle savedInstanceState) {
        mFoldingCell = findViewById(R.id.folding_cell);

        mFoldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFoldingCell.toggle(false);
            }
        });


        mAppBarLayout = findViewById(R.id.appbar);

        tvProjectLocation = findViewById(R.id.project_location);
        tvProjectDescription = findViewById(R.id.project_desc);


        tvProjectLocation.setText(project.getProject_location());
        tvProjectDescription.setText(project.getProject_description());

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
                }

//                activeFragment = position;
//                updateUI();

                Log.d("HHHH","Called");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });






        if(savedInstanceState==null){
            //mPresenter.loadRightFragment(projectPermission);
        }


        fabAdd = findViewById(R.id.fab_add);
        fabGantt = findViewById(R.id.fab_gantt_chart);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabAdd.setTransitionName("add_task");
        }*/
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
        adapter.addFragment(new TaskPagerFragment(), "COMPLETE");
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
    public void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadActivityFragment() {
        /*if(!(getSupportFragmentManager().findFragmentById(R.id.project_container) instanceof ActivityFragment)){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.project_container,new ActivityFragment()).commit();

        }*/
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
    public void startAddTaskActivity() {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        intent.putExtra(Constant.PROJECT_ID,project.getId());

        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void initializeViewPager(final List<Task> taskList) {
        this.taskList =taskList;
        mPresenter.filterData(taskList,currentPos);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                mPresenter.startAddTaskActivity();
                break;

            case R.id.fab_gantt_chart:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode==ProjectActivity.RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            //taskList.add(task);

            /*Collections.sort(taskList, new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    return (int) (task.getTask_start_date()-t1.getTask_start_date());
                }
            });*/
            //updateUI();
            //calculateProgress();
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

}
