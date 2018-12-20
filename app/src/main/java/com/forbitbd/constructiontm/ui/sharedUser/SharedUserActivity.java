package com.forbitbd.constructiontm.ui.sharedUser;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.utility.PrebaseActivity;
import com.forbitbd.constructiontm.model.SharedUser;


public class SharedUserActivity extends PrebaseActivity implements SharedUserContract.View,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private String projectId;

    private SharedUserAdapter adapter;
    private RecyclerView rvSharedUser;

    private SharedUserPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_user);
        mPresenter = new SharedUserPresenter(this);

        //Initialize Ad
       // new AdUtil(this);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.shared_user_list);

        projectId = getIntent().getStringExtra(Constant.PROJECT_ID);

        adapter = new SharedUserAdapter(getApplicationContext(),projectId);

        initView();

        mPresenter.requestForAllUsers(projectId);

    }

    private void initView() {
        rvSharedUser = findViewById(R.id.rv_shared_user);
        rvSharedUser.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvSharedUser.setItemAnimator(new DefaultItemAnimator());
        rvSharedUser.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvSharedUser.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSharedUser);
    }


    @Override
    public void addSharedUser(SharedUser sharedUser) {
        adapter.addSharedUser(sharedUser);
    }

    @Override
    public void userDeleted(int position) {

        adapter.removeItem(position);


        Snackbar snackbar = Snackbar
                .make(rvSharedUser, "Deleted Successfully", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        SharedUser sharedUser = adapter.getSharedUser(position);
        mPresenter.deleteSharedUser(sharedUser,projectId,position);
    }
}
