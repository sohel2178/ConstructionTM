package com.forbitbd.constructiontm.ui.project;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.ui.project.task.ActivityFragment;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProjectActivity extends PrebaseActivity implements ProjectContract.View{

    private Project project;
    private ProjectPermission projectPermission;

    private AppBarLayout mAppBarLayout;

    private TextView tvProjectLocation,tvProjectDescription,tvList;

    private ProjectPresenter mPresenter;

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


        //Set Enter and Exit Transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           /* getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());*/
        }

        //setupInterAd();



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
        mAppBarLayout = findViewById(R.id.appbar);

        tvProjectLocation = findViewById(R.id.project_location);
        tvProjectDescription = findViewById(R.id.project_desc);


        tvProjectLocation.setText(project.getProject_location());
        tvProjectDescription.setText(project.getProject_description());


        if(savedInstanceState==null){
            mPresenter.loadRightFragment(projectPermission);
        }



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
        if(!(getSupportFragmentManager().findFragmentById(R.id.project_container) instanceof ActivityFragment)){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.project_container,new ActivityFragment()).commit();

        }
    }

}
