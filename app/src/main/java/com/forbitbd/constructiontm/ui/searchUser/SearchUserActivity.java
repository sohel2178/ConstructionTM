package com.forbitbd.constructiontm.ui.searchUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;


import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.model.User;
import com.forbitbd.constructiontm.ui.shareProject.ProjectShareActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity implements SearchUserContract.View{

    private AppCompatEditText etEmail;
    private RecyclerView rvUsers;

    private SearchUserPresenter mPresenter;
    private int before,after;

    private List<User> userList;
    private UserAdapter adapter;

    private String projectId;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        this.projectId = getIntent().getStringExtra(Constant.PROJECT_ID);
        this.projectName = getIntent().getStringExtra(Constant.PROJECT_NAME);

        userList = new ArrayList<>();

        mPresenter = new SearchUserPresenter(this);

        adapter = new UserAdapter(this);

        initView();
    }

    private void initView() {
        etEmail = findViewById(R.id.et_email);
        rvUsers = findViewById(R.id.rv_user);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                before = charSequence.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                after = charSequence.toString().length();
                String val = charSequence.toString();
                mPresenter.requestForData(val,before,after);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void updateData(List<User> userList, String val) {
        this.userList = userList;
        updateAdapter(val);
    }

    @Override
    public void updateAdapter(String value) {
        if (value.equals("")){
            adapter.setData(userList);
        }else {
            List<User> tempList = new ArrayList<>();
            for(int i=0;i<userList.size();i++){
                if(userList.get(i).getEmail().toLowerCase().startsWith(value.toLowerCase())){
                    tempList.add(userList.get(i));
                }
            }

            adapter.setData(tempList);
        }
    }

    @Override
    public void userSelect(User user) {
        Intent intent = new Intent(getApplicationContext(), ProjectShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER,user);
        bundle.putString(Constant.PROJECT_ID,projectId);
        bundle.putString(Constant.PROJECT_NAME,projectName);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
