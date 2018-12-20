package com.forbitbd.constructiontm.ui.projectUpdate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Project;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.List;


public class UpdateProjectActivity extends PrebaseActivity implements View.OnClickListener {

    private Project project;

    private EditText etProjectName,etProjectDesc,etProjectLocation,etStartDate,etCompletionDate;
    private Button btnAdd;


    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);

        //Initialize Ad
        //new AdUtil(this);


        project = (Project) getIntent().getSerializableExtra(Constant.PROJECT);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.update_project));

        initView();
    }

    private void initView() {
        etProjectName = findViewById(R.id.et_project_name);
        etProjectDesc = findViewById(R.id.et_project_description);
        etProjectLocation = findViewById(R.id.et_project_location);
        etStartDate = findViewById(R.id.et_start_date);
        etCompletionDate = findViewById(R.id.et_completion_date);

        btnAdd = findViewById(R.id.add_project);

        btnAdd.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etCompletionDate.setOnClickListener(this);

        tvTitle = findViewById(R.id.title);


        if(project!=null){
            bindProject();
        }
    }

    private void bindProject() {
        etProjectName.setText(project.getProject_name());
        etProjectDesc.setText(project.getProject_description());
        etProjectLocation.setText(project.getProject_location());
        etStartDate.setText(project.getStart_date());
        etCompletionDate.setText(project.getCompletion_date());


        btnAdd.setText(getString(R.string.update));
        tvTitle.setText(getString(R.string.project_update_form));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_start_date:
                DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        etStartDate.setText(MyUtil.getStringDate(calendar.get(0).getTime()));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                DatePicker datePicker = builder.build();
                datePicker.show();
                break;
            case R.id.et_completion_date:
                DatePickerBuilder builder1 = new DatePickerBuilder(this, new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        etCompletionDate.setText(MyUtil.getStringDate(calendar.get(0).getTime()));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                DatePicker datePicker2 = builder1.build();
                datePicker2.show();
                break;
            case R.id.add_project:

                String name = etProjectName.getText().toString().trim();
                String location = etProjectLocation.getText().toString().trim();
                String description = etProjectDesc.getText().toString().trim();
                String startDate = etStartDate.getText().toString().trim();
                String completionDate = etCompletionDate.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    etProjectName.requestFocus();
                    Toast.makeText(this, "Please Fill up Project Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(description)){
                    etProjectDesc.requestFocus();
                    Toast.makeText(this, "Please Fill up Project Description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(location)){
                    etProjectLocation.requestFocus();
                    Toast.makeText(this, "Please Fill up Project Location", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*if(TextUtils.isEmpty(startDate)){
                    mivStartDate.requestFocus();
                    Toast.makeText(this, "Please Select Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(completionDate)){
                    mivCompletiondate.requestFocus();
                    Toast.makeText(this, "Please Select Completion Date", Toast.LENGTH_SHORT).show();
                    return;
                }*/

               /* if(imageUri==null){
                    mivSelectImage.requestFocus();
                    Toast.makeText(this, "Please Select Project Image", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                project.setProject_name(name);
                project.setProject_location(location);
                project.setProject_description(description);
                project.setStart_date(startDate);
                project.setCompletion_date(completionDate);

                showProgressDialog();

                MyDatabaseRef myDatabaseRef = new MyDatabaseRef();

                myDatabaseRef.getProjectRef().child(project.getId()).setValue(project, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                        hideProgressDialog();
                        onBackPressed();
                    }
                });


                break;
        }
    }

}
