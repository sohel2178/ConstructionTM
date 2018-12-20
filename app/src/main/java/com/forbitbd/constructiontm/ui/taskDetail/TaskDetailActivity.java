package com.forbitbd.constructiontm.ui.taskDetail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;


import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.model.WorkDone;
import com.forbitbd.constructiontm.ui.taskDetail.progress.ProgressFragment;
import com.forbitbd.constructiontm.ui.taskDetail.workdoneChart.WorkDoneChartFragment;
import com.forbitbd.constructiontm.ui.taskDetail.workdoneTable.WorkDoneTableFragment;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends PrebaseActivity implements TaskDetailContract.View {

    private Task task;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<WorkDone> workDoneList;
    private ViewPagerAdapter adapter;
    private int activeFragment;
    private TaskDetailPresenter mPresenter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new TaskDetailPresenter(this);
        setContentView(R.layout.activity_task_detail);
        new AdUtil(this);
        workDoneList = new ArrayList<>();

        task = (Task) getIntent().getSerializableExtra(Constant.TASK);
        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(task.getTask_name());

        initView();
    }

    private void initView() {
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
                activeFragment = position;
                updateUI();

                Log.d("HHHH","Called");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       mPresenter.requestForWorkDone(task.getProject_id(),task.getTask_id());

    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }


    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
        }
        adapter.addFragment(new WorkDoneTableFragment(), "TABLE");
        adapter.addFragment(new WorkDoneChartFragment(), "CHART");
        adapter.addFragment(new ProgressFragment(), "PROGRESS");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void updateWorkDone(List<WorkDone> workDoneList) {
        this.workDoneList = workDoneList;
        updateUI();
    }

    private void updateUI(){
        if(activeFragment==0){
            WorkDoneTableFragment tf = (WorkDoneTableFragment) adapter.getItem(activeFragment);
            tf.setData(this.workDoneList,task.getUnit());
        }else if(activeFragment==1){
            WorkDoneChartFragment cf = (WorkDoneChartFragment) adapter.getItem(activeFragment);
            cf.update(this.workDoneList);
        }else if(activeFragment==2){
            ProgressFragment pf = (ProgressFragment) adapter.getItem(activeFragment);
            pf.update(this.workDoneList,task.getTask_volume_of_works());
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
