package com.forbitbd.constructiontm.ui.project.task;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.ui.gantt.GanttActivity;
import com.forbitbd.constructiontm.ui.project.ProjectActivity;
import com.forbitbd.constructiontm.ui.project.task.taskpager.FragmentLoadListener;
import com.forbitbd.constructiontm.ui.project.task.taskpager.TaskPagerFragment;
import com.forbitbd.constructiontm.ui.taskAdd.AddTaskActivity;
import com.forbitbd.constructiontm.ui.util.AdFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends AdFragment implements View.OnClickListener,FragmentLoadListener,TaskContract.View{


    public static final int REQUEST_CODE=5000;

    //private ProjectFragmentListener listener;

    private List<Task> taskList;
    private LinearLayout mHide;
    private ImageView ivIndicator;
    private int indicatorHeight,layoutHeight,hideContainerHeight;
    private boolean isOpen =false;

    private CircleProgress mPhysical,mFinancial;
    private FloatingActionButton fabAdd,fabGantt;
    private BottomNavigationView mNavigationView;
    private TaskPresenter mPresenter;

    private ProjectPermission permission;

    private int action;

    private int counter;

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.listener = (ProjectFragmentListener) getActivity();
        ProjectActivity activity = (ProjectActivity) getActivity();
        permission = activity.getProjectPermission();

        indicatorHeight = (int) getResources().getDimension(R.dimen.icon_height);
        taskList = new ArrayList<>();
        //projectPermission = activity.getProjectPermission();
        mPresenter = new TaskPresenter(this);


        mPresenter.requestDataFromServer(activity.getProject().getId());
        getmInterstitialAd().setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getmInterstitialAd().loadAd(new AdRequest.Builder().build());
                mPresenter.buttonClick(action);
            }
        });


    }



    private void updateUI(){
        if(getChildFragmentManager().findFragmentById(R.id.task_container)==null){
            loadFragment(0);
        }else{
            TaskPagerFragment tf = (TaskPagerFragment) getChildFragmentManager().findFragmentById(R.id.task_container);
            tf.update(taskList);
        }
    }

    private void calculateProgress() {
        mPresenter.calculateProgress(taskList);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        initView(view);
        return view;
    }

    private void initView(final View view) {

        fabAdd = view.findViewById(R.id.fab_add);
        fabGantt = view.findViewById(R.id.fab_gantt_chart);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabAdd.setTransitionName("add_task");
        }*/
        fabAdd.setOnClickListener(this);
        fabGantt.setOnClickListener(this);

        if(permission!=null){
            if(permission.getActivityWrite()==0){
                fabAdd.setVisibility(View.GONE);
            }

        }


        mHide = view.findViewById(R.id.hide_container);
        ivIndicator = view.findViewById(R.id.indicator);
        ivIndicator.setOnClickListener(this);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                layoutHeight = view.getHeight();
                hideContainerHeight = mHide.getHeight();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHide.setY(layoutHeight-indicatorHeight);
            }
        });

        mFinancial = view.findViewById(R.id.circle_progress_fi);
        mPhysical = view.findViewById(R.id.circle_progress_ph);

        mNavigationView = view.findViewById(R.id.navigation);



        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = new TaskPagerFragment();
                switch (item.getItemId()){
                    case R.id.action_all:
                       loadFragment(0);
                        break;

                    case R.id.action_today:
                        loadFragment(1);
                        break;

                    case R.id.action_running:
                        loadFragment(2);
                        break;

                    case R.id.action_complete:
                        loadFragment(3);
                        break;

                    case R.id.action_expired:
                        loadFragment(4);
                        break;
                }

                return true;
            }

        });
    }

    private void loadFragment(int value){
        Fragment fragment = new TaskPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.FRAG_NO,value);
        bundle.putSerializable(Constant.PROJECT_PERMISSION,permission);
        fragment.setArguments(bundle);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.task_container,fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode==ProjectActivity.RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);
            taskList.add(task);

            Collections.sort(taskList, new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    return (int) (task.getTask_start_date()-t1.getTask_start_date());
                }
            });
            updateUI();
            calculateProgress();
        }
    }

    public void updateTask(Task task){
        mPresenter.getUpdatedList(taskList,task);
    }

    public void deleteTask(Task task){
        taskList.remove(task);
        updateUi(taskList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.indicator:
                animate();
                break;

            case R.id.fab_add:
                //setClick(null,ADD_NEW_TASK);
                mPresenter.requestToshowAd(1);
                break;

            case R.id.fab_gantt_chart:
                //setClick(null,ADD_NEW_TASK);
                //mPresenter.requestToshowAd();
                mPresenter.requestToshowAd(2);
                break;
        }

    }

    private void animate() {
        final float animatedHeight = hideContainerHeight-indicatorHeight;
        ValueAnimator animator = ValueAnimator.ofFloat(0,animatedHeight);
        animator.setDuration(getResources().getInteger(R.integer.anim_duration_long));

        final int currentY = (int) mHide.getY();
        final float currentRotation =ivIndicator.getRotation();

        if(!isOpen){
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ivIndicator.setRotation(currentRotation-animatedValue*180/animatedHeight);
                    mHide.setY(currentY-animatedValue);
                    mHide.requestLayout();
                }
            });
            isOpen=true;



        }else{
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();

                    ivIndicator.setRotation(currentRotation+animatedValue*180/animatedHeight);
                    mHide.setY(currentY+animatedValue);
                    mHide.requestLayout();

                }
            });

            isOpen=false;

        }



        animator.start();
    }

    @Override
    public void onLoad(int fragmentNumber) {
        // Code Goes Here
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.task_container);

        if(fragment instanceof TaskPagerFragment){
           TaskPagerFragment tf = (TaskPagerFragment) fragment;
           tf.update(taskList);
        }

    }

    @Override
    public void updateUi(List<Task> taskList) {
        this.taskList = taskList;
        calculateProgress();
        updateUI();
    }

    @Override
    public void startNewTaskActivityAndGetResult() {
        ProjectActivity activity = (ProjectActivity) getActivity();

        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra(Constant.PROJECT_ID,activity.getProject().getId());

        startActivityForResult(intent,REQUEST_CODE);


    }

    @Override
    public void startGanttChartActivity() {
        Intent intent = new Intent(getContext(), GanttActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, (Serializable) taskList);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void showAdvert(int action) {
        counter++;
        this.action = action;

        if(counter%2==0){
            if(getmInterstitialAd().isLoaded()){
                showAd();
            }else{
                mPresenter.buttonClick(action);
            }
        }else{
            mPresenter.buttonClick(action);
        }



    }

    @Override
    public void updateProgress(double fp, double pp) {
        mPhysical.setProgress((int) pp);
        mFinancial.setProgress((int) fp);
    }
}
