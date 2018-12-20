package com.forbitbd.constructiontm.ui.projectAdd;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.BaseActivity;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.dialogFragment.DatePickerListener;
import com.forbitbd.constructiontm.dialogFragment.MyDateDialog;
import com.forbitbd.constructiontm.model.Project;

import java.util.Date;

public class AddProjectActivity extends BaseActivity implements View.OnClickListener,AddProjectContract.View{



    private EditText etProjectName,etProjectDesc,etProjectLocation,etStartDate,etCompletionDate;
    private TextInputLayout tProjectName,tProjectDesc,tProjectLocation,tStartDate,tCompletionDate;
    //private FloatingActionButton mivStartDate,mivCompletiondate;
    //private ImageView ivProject;
    private Button btnAdd;

    //private byte[] byteArray;

    private AddProjectContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_project);

        //Initialize Ad
        new AdUtil(this);


        etProjectName = findViewById(R.id.et_project_name);
        etProjectDesc = findViewById(R.id.et_project_description);
        etProjectLocation = findViewById(R.id.et_project_location);
        etStartDate = findViewById(R.id.et_start_date);
        etCompletionDate = findViewById(R.id.et_completion_date);

        tProjectName = findViewById(R.id.ti_project_name);
        tProjectLocation = findViewById(R.id.ti_project_location);
        tProjectDesc= findViewById(R.id.ti_project_description);
        tStartDate = findViewById(R.id.ti_start_date);
        tCompletionDate = findViewById(R.id.ti_completion_date);

        btnAdd = findViewById(R.id.add_project);

        btnAdd.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etCompletionDate.setOnClickListener(this);


        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.create_new_project));

        etStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    MyDateDialog myDateDialog = new MyDateDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TITLE,getString(R.string.select_start_date));
                    myDateDialog.setArguments(bundle);
                    myDateDialog.setDatePickerListener(new DatePickerListener() {
                        @Override
                        public void onDatePick(long time) {
                            etStartDate.setText(MyUtil.getStringDate(new Date(time)));
                        }
                    });
                    myDateDialog.show(getFragmentManager(),"FFFF");
                }
            }
        });

        etCompletionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    MyDateDialog myDateDialog = new MyDateDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TITLE,getString(R.string.select_completion_date));
                    myDateDialog.setArguments(bundle);
                    myDateDialog.setDatePickerListener(new DatePickerListener() {
                        @Override
                        public void onDatePick(long time) {
                            etCompletionDate.setText(MyUtil.getStringDate(new Date(time)));
                        }
                    });
                    myDateDialog.show(getFragmentManager(),"FFFF");
                }
            }
        });


        //initCircularReveal(getString(R.string.create_new_project),"transition_reveal_contact");

        mPresenter = new AddProjectPresenter(this);


    }


    @Override
    protected void onResume() {
        super.onResume();

        //setTitle("Add New Project");
    }


    @Override
    public void onClick(View v) {
        MyDateDialog myDateDialog = new MyDateDialog();
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.et_start_date:

                bundle.putString(Constant.TITLE,getString(R.string.select_start_date));
                myDateDialog.setArguments(bundle);
                myDateDialog.setDatePickerListener(new DatePickerListener() {
                    @Override
                    public void onDatePick(long time) {
                        etStartDate.setText(MyUtil.getStringDate(new Date(time)));
                    }
                });
                myDateDialog.show(getFragmentManager(),"FFFF");
                break;
            case R.id.et_completion_date:
                bundle.putString(Constant.TITLE,getString(R.string.select_completion_date));
                myDateDialog.setArguments(bundle);
                myDateDialog.setDatePickerListener(new DatePickerListener() {
                    @Override
                    public void onDatePick(long time) {
                        etCompletionDate.setText(MyUtil.getStringDate(new Date(time)));
                    }
                });
                myDateDialog.show(getFragmentManager(),"FFFF");
                break;
            case R.id.add_project:

                String name = etProjectName.getText().toString().trim();
                String location = etProjectLocation.getText().toString().trim();
                String description = etProjectDesc.getText().toString().trim();
                String startDate = etStartDate.getText().toString().trim();
                String completionDate = etCompletionDate.getText().toString().trim();

                Project project = new Project(getCurrentUser().getUid(),name,description,location,startDate,completionDate);

                boolean valid = mPresenter.validate(project);

                if(!valid){
                    return;
                }

                if(isOnline()){
                    mPresenter.addProjectToDatabase(project);
                }else{
                    Toast.makeText(getApplicationContext(), "Internet Connection is not Available!!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void showValidationError(String message, int fieldId) {
        switch (fieldId){
            case 1:
                etProjectName.requestFocus();
                tProjectName.setError(message);
                break;

            case 2:
                etProjectLocation.requestFocus();
                tProjectLocation.setError(message);
                break;

            case 3:
                etProjectDesc.requestFocus();
                tProjectDesc.setError(message);
                break;

            case 4:
                etStartDate.requestFocus();
                tStartDate.setError(message);
                break;

            case 5:
                etCompletionDate.requestFocus();
                tCompletionDate.setError(message);
                break;

        }


    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
        onBackPressed();
    }

    @Override
    public void clearPreError() {
        tProjectName.setErrorEnabled(false);
        tProjectLocation.setErrorEnabled(false);
        tProjectDesc.setErrorEnabled(false);
        tStartDate.setErrorEnabled(false);
        tCompletionDate.setErrorEnabled(false);
    }
}
