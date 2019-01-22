package com.forbitbd.constructiontm.ui.project.task.taskpager;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.dialogFragment.AddWorkDoneFragment;
import com.forbitbd.constructiontm.dialogFragment.WorkDoneAmountListener;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.ui.project.ProjectActivity;
import com.forbitbd.constructiontm.ui.project.task.ActivityFragment;
import com.forbitbd.constructiontm.ui.taskDetail.TaskDetailActivity;
import com.forbitbd.constructiontm.ui.taskUpdate.UpdateTaskActivity;
import com.forbitbd.constructiontm.ui.util.AdFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskPagerFragment extends AdFragment implements TaskClickListener ,TaskPagerContract.View{

    private static final int VIEW_CLICK =0;
    private static final int EDIT_CLICK =1;
    private static final int ADD_CLICK =2;
    private static final int DELETE_CLICK =3;

    private static final int REQUEST_CODE=10000;

    private RecyclerView rvTask;

    private TaskAdapter adapter;

    //int fragnumber;
    //private ProjectPermission mProjectPermission;


    private int action;
    private Task selectedTask;


    private TaskPagerPresenter mPresenter;

    private FrameLayout touchInterceptor;

    private ProjectPermission permission;

    private int counter;


    public TaskPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        touchInterceptor = new FrameLayout(getContext());
        touchInterceptor.setClickable(true);

        mPresenter = new TaskPagerPresenter(this);
        mPresenter.setupAd();

        //fragnumber = getArguments().getInt(Constant.FRAG_NO);
        //permission = (ProjectPermission) getArguments().getSerializable(Constant.PROJECT_PERMISSION);

        if(getActivity() instanceof ProjectActivity){
            ProjectActivity pa = (ProjectActivity) getActivity();
            permission = pa.getProjectPermission();
            adapter = new TaskAdapter(getContext(),pa.getProjectPermission(),this);
        }


    }

    public void clearAdapter(){
        adapter.clear();
    }

    public void addTask(Task task){
        adapter.addTask(task);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ViewGroup) getView().findViewById(R.id.root)).removeView(touchInterceptor);
        //listener.onLoad(fragnumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_pager, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onPause() {
        if (touchInterceptor.getParent() == null) {
            ((ViewGroup) getView().findViewById(R.id.root)).addView(touchInterceptor);
        }
        super.onPause();
    }


    private void initView(View view) {
        rvTask = view.findViewById(R.id.rv_task);
        rvTask.setLayoutManager(new LinearLayoutManager(getContext()));
        //rvTask.setNestedScrollingEnabled(false);
        rvTask.setAdapter(adapter);
    }

    public void update(List<Task> taskList){
       // mPresenter.startFiltering(taskList,fragnumber);
    }



    @Override
    public void onItemClick(Task task) {
        this.action=VIEW_CLICK;
        this.selectedTask = task;

        counter++;



        startTaskDetailActivity();
        //showAd();
    }


    @Override
    public void onAddClick(Task task) {
        this.action=ADD_CLICK;
        this.selectedTask = task;

        counter++;


        if(counter%3==0){
            if(getmInterstitialAd().isLoaded()){
                showAd();
            }else{
                mPresenter.showWorkDoneDialog(task);
            }
        }else{
            mPresenter.showWorkDoneDialog(task);
        }

      /*  if(getmInterstitialAd().isLoaded()){
            showAd();
        }else{
            mPresenter.showWorkDoneDialog();
        }*/

    }

    @Override
    public void onEditClick(Task task) {
        this.action=EDIT_CLICK;
        this.selectedTask = task;

        counter++;

        if(counter%3==0){
            if(getmInterstitialAd().isLoaded()){
                showAd();
            }else{
                mPresenter.callViewToEditTask();
            }
        }else{
            mPresenter.callViewToEditTask();
        }

    }

    @Override
    public void onDeleteClick(Task task) {
        this.action=DELETE_CLICK;
        this.selectedTask = task;

        counter++;

        if(counter%3==0){
            if(getmInterstitialAd().isLoaded()){
                showAd();
            }else{
                mPresenter.callViewShowDialog();
            }
        }else{
            mPresenter.callViewShowDialog();
        }

    }

    @Override
    public void setupAdCloseListener() {

        getmInterstitialAd().setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                getmInterstitialAd().loadAd(new AdRequest.Builder().build());

                switch (action){
                    case VIEW_CLICK:
                        mPresenter.callViewToStartTaskDetailActivity();
                        break;

                    case EDIT_CLICK:
                        mPresenter.callViewToEditTask();
                        break;

                    case DELETE_CLICK:
                        mPresenter.callViewShowDialog();
                        break;

                    case ADD_CLICK:
                        mPresenter.showWorkDoneDialog(selectedTask);
                        break;
                }
            }
        });
    }

    @Override
    public void startTaskDetailActivity() {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TASK,selectedTask);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startEditTask() {
        Intent intent = new Intent(getContext(), UpdateTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TASK,selectedTask);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode==ProjectActivity.RESULT_OK){
            Task task = (Task) data.getSerializableExtra(Constant.TASK);

            if(getParentFragment() instanceof ActivityFragment && task!=null){
                ActivityFragment af = (ActivityFragment) getParentFragment();
                af.updateTask(task);
            }
        }
    }

    @Override
    public void deleteComplete() {
        if(getActivity() instanceof ProjectActivity){
            ProjectActivity pa = (ProjectActivity) getActivity();
            pa.hideProgressDialog();
        }
        Toast.makeText(getContext(), "Task Remove Successfully", Toast.LENGTH_SHORT).show();
        //adapter.removeTask(selectedTask);

        if(getParentFragment() instanceof ActivityFragment){
            ActivityFragment af = (ActivityFragment) getParentFragment();
            af.deleteTask(selectedTask);
        }
    }

    @Override
    public void showDeleteDialog(String title) {

        rvTask.setClickable(false);

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);
        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext(),R.style.CustomDialogTheme).create();
        alertDialog.setTitle(ssBuilder);
        alertDialog.setMessage("Do you Really want to Delete this Task??");
        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.delete);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(R.color.red));
        }
        alertDialog.setIcon(drawable);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.indicator_4));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.indicator_4));
            }
        });



        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CALL","Call");
                dialogInterface.dismiss();
                mPresenter.deleteFromDatabase(selectedTask);

            }
        });

        alertDialog.show();
    }

    @Override
    public void showProgressDialog() {
        if(getActivity() instanceof ProjectActivity){
            ProjectActivity pa = (ProjectActivity) getActivity();
            pa.showProgressDialog();
        }
    }

    @Override
    public void showWorkDoneDialog(Task task) {
        AddWorkDoneFragment df = new AddWorkDoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TASK,task.getTask_name());
        df.setArguments(bundle);
        df.setListener(new WorkDoneAmountListener() {
            @Override
            public void getAmount(final double amount, Date selectedDate) {
                if(selectedTask.getTask_volume_of_work_done()+amount>selectedTask.getTask_volume_of_works()){
                    Toast.makeText(getContext(), "Work Done Exceed total Amount of Works", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.addWorkdoneToDatabase(selectedTask,selectedDate,amount);
            }
        });

        df.show(getActivity().getFragmentManager(),"ADD_WORKDONE_FRAGMENT");
    }

    @Override
    public void hideDialogAndUpdateItem(double amount) {
        if(getActivity() instanceof ProjectActivity){
            ProjectActivity pa = (ProjectActivity) getActivity();
            pa.hideProgressDialog();
        }
        selectedTask.setTask_volume_of_work_done(selectedTask.getTask_volume_of_work_done()+amount);
        //adapter.updateItem(selectedTask.getTask_id(),(selectedTask.getTask_volume_of_work_done()+amount));

        if(getParentFragment() instanceof ActivityFragment){
            ActivityFragment af = (ActivityFragment) getParentFragment();
            af.updateTask(selectedTask);
        }
    }

    @Override
    public void updateAdapter(List<Task> taskList) {
        adapter.setData(taskList);
    }
}
