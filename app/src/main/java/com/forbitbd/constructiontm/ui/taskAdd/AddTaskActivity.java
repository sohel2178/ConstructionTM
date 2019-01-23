package com.forbitbd.constructiontm.ui.taskAdd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.AdUtil;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTaskActivity extends PrebaseActivity implements View.OnClickListener,AddTaskContract.View {


    // Main View
    private EditText etName,etVolofWork,etUnitRate,etStartDate,etFinishedDate;
    private TextInputLayout tName,tVolofWork,tUnitRate,tStartDate,tFinishedDate,tUnit;
    private AutoCompleteTextView etUnit;
    private Button btnAdd;

    private long startDateTime=-1,finishedDateTime =-1;
    private String projectId;

    private ArrayAdapter<String> unitAdapter;


    private AddTaskPresenter mPresenter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.mPresenter = new AddTaskPresenter(this);

        //Initialize Ad
        new AdUtil(this);



        if(getIntent().getStringExtra(Constant.PROJECT_ID)!=null){
            projectId = getIntent().getStringExtra(Constant.PROJECT_ID);
        }
        unitAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.add_new_task));


        initView();

        mPresenter.requestForUnit(projectId);

    }

    private void initView() {


        // Main View
        etName = findViewById(R.id.et_task_name);
        etVolofWork = findViewById(R.id.et_task_vol_of_work);
        etUnitRate = findViewById(R.id.et_unit_rate);
        etStartDate = findViewById(R.id.et_start_date);
        etFinishedDate = findViewById(R.id.et_finished_date);
        etUnit = findViewById(R.id.unit);
        etUnit.setThreshold(1);
        etUnit.setAdapter(unitAdapter);

        tName = findViewById(R.id.ti_task_name);
        tVolofWork = findViewById(R.id.ti_task_vol_of_work);
        tUnitRate = findViewById(R.id.ti_unit_rate);
        tUnit = findViewById(R.id.ti_unit);
        tStartDate = findViewById(R.id.ti_start_date);
        tFinishedDate = findViewById(R.id.ti_finished_date);


        btnAdd = findViewById(R.id.btn_add);

        etStartDate.setOnClickListener(this);
        etFinishedDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);


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



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_start_date:
                final DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        startDateTime = calendar.get(0).getTimeInMillis();
                        etStartDate.setText(MyUtil.getStringDate(new Date(startDateTime)));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                DatePicker datePicker = builder.build();
                datePicker.show();
                break;

            case R.id.et_finished_date:
                DatePickerBuilder builder1 = new DatePickerBuilder(this, new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        finishedDateTime = calendar.get(0).getTimeInMillis();
                        etFinishedDate.setText(MyUtil.getStringDate(new Date(finishedDateTime)));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                DatePicker datePicker2 = builder1.build();
                datePicker2.show();
                break;

            case R.id.btn_add:
                hideKey(view);
                String name = etName.getText().toString().trim();
                String volOfWork = etVolofWork.getText().toString().trim();
                String unitRate = etUnitRate.getText().toString().trim();
                final String unit = etUnit.getText().toString().trim();

                Task task = new Task();
                task.setProject_id(projectId);
                task.setTask_name(name);
                task.setUnit(unit);

                boolean valid = mPresenter.validate(task,startDateTime,finishedDateTime,volOfWork,unitRate);

                if(!valid){
                    return;
                }

                if(!isOnline()){
                    Toast.makeText(this, "Please Turn on Your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                task.setTask_volume_of_works(Double.parseDouble(volOfWork));
                task.setTask_unit_rate(Double.parseDouble(unitRate));
                task.setTask_start_date(startDateTime);
                task.setTask_finished_date(finishedDateTime);

                mPresenter.saveTask(task,projectId,unit);

                break;
        }
    }

    @Override
    public void addUnitToAdapter(String unit) {
        unitAdapter.add(unit);
    }

    @Override
    public void showErrorMessage(String message, int field) {
        switch (field){
            case 1:
                etName.requestFocus();
                tName.setError(message);
                break;

            case 2:
                etVolofWork.requestFocus();
                tVolofWork.setError(message);
                break;

            case 3:
                etUnitRate.requestFocus();
                tUnitRate.setError(message);
                break;

            case 4:
                etUnit.requestFocus();
                tUnit.setError(message);
                break;

            case 5:
                tStartDate.setError(message);
                break;

            case 6:
                tFinishedDate.setError(message);
                break;
        }

        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void complete(Task task) {
        hideProgressDialog();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TASK,task);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

        finish();
    }

    @Override
    public void clearPreError() {
        tName.setErrorEnabled(false);
        tVolofWork.setErrorEnabled(false);
        tUnitRate.setErrorEnabled(false);
        tUnit.setErrorEnabled(false);
        tStartDate.setErrorEnabled(false);
        tFinishedDate.setErrorEnabled(false);
    }
}
