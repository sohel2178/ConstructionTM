package com.forbitbd.constructiontm.ui.main.home.sharedProject;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.Constant;
import com.forbitbd.constructiontm.holder.SharedProjectHolder;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.ui.project.ProjectActivity;
import com.forbitbd.constructiontm.ui.util.AdFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class SharedProjectFragment extends AdFragment implements ShareProjectContract.View {


    private RecyclerView rvProject;

    private Query query;

    FirebaseRecyclerAdapter<ProjectPermission,SharedProjectHolder> adapter;


    private ShareProjectPresenter mPresenter;
    private ProjectPermission projectPermission;
    private int action;

    AlertDialog alertDialog;




    public SharedProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAd();
        mPresenter = new ShareProjectPresenter(this);
        mPresenter.initializeFirebaseAdapter();

    }

    private void setupAd() {
        getmInterstitialAd().setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getmInterstitialAd().loadAd(new AdRequest.Builder().build());
                mPresenter.performTaskAsPerAction(action);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shared_project, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        rvProject = view.findViewById(R.id.rv_project);
        rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProject.setAdapter(adapter);
    }

    @Override
    public void setupAdapter(Query query) {

        adapter = new FirebaseRecyclerAdapter<ProjectPermission, SharedProjectHolder>(ProjectPermission.class,
                R.layout.single_shared_project_new,SharedProjectHolder.class,query) {
            @Override
            protected void populateViewHolder(SharedProjectHolder viewHolder, final ProjectPermission model, int position) {
                viewHolder.bindProject(model);

                viewHolder.getViewProjectButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.actionClick(model,1);
                    }
                });


                viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.actionClick(model,2);
                    }
                });
            }
        };

    }

    @Override
    public void showInterAd(ProjectPermission projectPermission, int action) {
        this.action=action;
        this.projectPermission=projectPermission;

        if(getmInterstitialAd().isLoaded()){
            showAd();
        }else {
            getmInterstitialAd().loadAd(new AdRequest.Builder().build());
            mPresenter.performTaskAsPerAction(this.action);
        }


    }

    @Override
    public void startProjectActivity() {
        mPresenter.retrieveProject(projectPermission.getProjectId());
    }

    @Override
    public void startProjectActivity(Project project) {
        Intent intent = new Intent(getContext(), ProjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        bundle.putSerializable(Constant.PROJECT_PERMISSION,projectPermission);
        intent.putExtras(bundle);

        startActivity(intent);

       /* if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.transitActivity(intent);
        }*/
    }

    @Override
    public void deleteProject() {
        mPresenter.showDialog();
    }

    @Override
    public void showDeleteDialog() {
        String titleText ="Alert";

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        alertDialog = new AlertDialog.Builder(getContext(),R.style.CustomDialogTheme).create();
        alertDialog.setTitle(ssBuilder);
        alertDialog.setMessage("Do you Really want to Remove this Project??");
        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.delete);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(R.color.red));
        }
        alertDialog.setIcon(drawable);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.alertNegativeClick();
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
                mPresenter.alertPositiveClick(projectPermission);

            }
        });

        alertDialog.show();
    }

    @Override
    public void dismissAlertDialog(String message) {
        if(alertDialog!=null &&alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        if(message!=null){
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
