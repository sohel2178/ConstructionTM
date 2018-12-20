package com.forbitbd.constructiontm.ui.shareProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectShareActivity extends PrebaseActivity implements View.OnClickListener,ProjectShareContract.View {
    private String projectId;
    private User selectedUser;


    private CircleImageView ivImage;
    private TextView tvName,tvCompanyName;

    private CheckBox ckbActivityWrite;

    private Button btnShare;

    private ProjectSharePresenter mPresenter;

    private static final int REQUEST_CODE=5000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_share);
        mPresenter = new ProjectSharePresenter(this);
        //Initialize Ad
        new AdUtil(this);

        String projectName = getIntent().getStringExtra(Constant.PROJECT_NAME);
        projectId = getIntent().getStringExtra(Constant.PROJECT_ID);
        selectedUser = (User) getIntent().getSerializableExtra(Constant.USER);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(projectName);

        initView();



    }

    private void initView() {

        ivImage = findViewById(R.id.image);
        tvName = findViewById(R.id.name);
        tvCompanyName = findViewById(R.id.tv_company_name);


        ckbActivityWrite = findViewById(R.id.has_write_permission);

        btnShare = findViewById(R.id.share);
        btnShare.setOnClickListener(this);

        updateUI();
    }

    private void updateUI() {
        if(selectedUser!=null){
            tvName.setText(selectedUser.getName());

            if(!selectedUser.getCompanyName().equals("")){
                tvCompanyName.setText(selectedUser.getCompanyName());
            }else{
                tvCompanyName.setText(R.string.undefined);
            }


            if(selectedUser.getPhotoUri() != null && !selectedUser.getPhotoUri().equals("")){
                Picasso.with(getApplicationContext())
                        .load(selectedUser.getPhotoUri())
                        .into(ivImage);
            }

            ckbActivityWrite.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.share:
                ProjectPermission projectPermission = new ProjectPermission();
                projectPermission.setProjectId(projectId);
                projectPermission.setIsEnable(1);

                if(ckbActivityWrite.isChecked()){
                    projectPermission.setActivityWrite(1);
                }else{
                    projectPermission.setActivityWrite(0);
                }

                mPresenter.shareClick(projectPermission,selectedUser.getUid(),projectId);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            selectedUser = (User) data.getSerializableExtra(Constant.USER);
            updateUI();
        }
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }


    @Override
    public void complete() {
        hideProgressDialog();
        Toast.makeText(this, "Project Shared Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


}
