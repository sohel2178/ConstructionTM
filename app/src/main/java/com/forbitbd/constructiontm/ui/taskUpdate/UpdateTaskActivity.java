package com.forbitbd.constructiontm.ui.taskUpdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateTaskActivity extends PrebaseActivity implements View.OnClickListener,UpdateTaskContract.View {

    private TextView tvTitle;

    private Task task;

    private EditText etName,etVolofWork,etUnitRate,etStartDate,etFinishedDate;
    private TextInputLayout tName,tVolofWork,tUnitRate,tStartDate,tFinishedDate,tUnit;
    private AutoCompleteTextView etUnit;
    private Button btnAdd;

    private String projectId;

    private ArrayAdapter<String> unitAdapter;

    private UpdateTaskPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_update_task);

        mPresenter = new UpdateTaskPresenter(this);


        new AdUtil(this);


        task = (Task) getIntent().getSerializableExtra(Constant.TASK);

        if(task!=null){
            projectId = task.getProject_id();
            setupToolbar();
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(task.getTask_name());
            initView();

        }

        unitAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        mPresenter.requestForUnit(projectId);
    }

    private void initView() {
        tvTitle =findViewById(R.id.title);
        tvTitle.setText(R.string.task_update_form);

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



        bindView();
    }

    private void bindView() {
        etName.setText(task.getTask_name());
        etVolofWork.setText(String.valueOf(task.getTask_volume_of_works()));
        etUnitRate.setText(String.valueOf(task.getTask_unit_rate()));
        etStartDate.setText(MyUtil.getStringDate(new Date(task.getTask_start_date())));
        etFinishedDate.setText(MyUtil.getStringDate(new Date(task.getTask_finished_date())));
        etUnit.setText(task.getUnit());
        btnAdd.setText(getString(R.string.update_task));
    }

    @Override
    protected void onResume() {
        super.onResume();
       // hideSoftKeyboard();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.et_start_date:
                DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        etStartDate.setText(MyUtil.getStringDate(new Date(calendar.get(0).getTimeInMillis())));

                    }
                }).pickerType(CalendarView.ONE_DAY_PICKER).date(Calendar.getInstance());

                DatePicker datePicker = builder.build();
                datePicker.show();
                break;

            case R.id.et_finished_date:
                DatePickerBuilder builder1 = new DatePickerBuilder(this, new OnSelectDateListener() {
                    @Override
                    public void onSelect(List<Calendar> calendar) {
                        etFinishedDate.setText(MyUtil.getStringDate(new Date(calendar.get(0).getTimeInMillis())));

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

                task.setTask_name(name);
                task.setUnit(unit);

                long startDateTime = -1;
                long finishedDateTime = -1;

                try {
                    startDateTime =MyUtil.getDate(etStartDate.getText().toString().trim()).getTime();
                    finishedDateTime =MyUtil.getDate(etFinishedDate.getText().toString().trim()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                boolean valid =mPresenter.validate(task,startDateTime,finishedDateTime,volOfWork,unitRate);

                if(!valid){
                    return;
                }

                task.setTask_unit_rate(Double.parseDouble(unitRate));
                task.setTask_volume_of_works(Double.parseDouble(volOfWork));
                task.setTask_start_date(startDateTime);
                task.setTask_finished_date(finishedDateTime);

                mPresenter.updateTask(task,projectId,unit);


                break;
        }

    }

    @Override
    public void addUnitToAdapter(String unit) {
        unitAdapter.add(unit);
    }

    @Override
    public void showErrorMessage(String message, int field) {
        switch (field) {
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
        hideProgressDialog();

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
