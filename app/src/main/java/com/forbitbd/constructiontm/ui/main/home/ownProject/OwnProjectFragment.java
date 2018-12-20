package com.forbitbd.constructiontm.ui.main.home.ownProject;


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
import com.forbitbd.constructiontm.holder.ProjectHolder;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.ui.projectUpdate.UpdateProjectActivity;
import com.forbitbd.constructiontm.ui.project.ProjectActivity;
import com.forbitbd.constructiontm.ui.searchUser.SearchUserActivity;
import com.forbitbd.constructiontm.ui.sharedUser.SharedUserActivity;
import com.forbitbd.constructiontm.ui.util.AdFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnProjectFragment extends AdFragment implements OwnProjectContract.View {

    private RecyclerView rvProject;



    FirebaseRecyclerAdapter<Project,ProjectHolder> adapter;


    private ProjectHolder viewHolder;
    private Project project;

    private int action;
    private OwnProjectPresenter mPresenter;

    AlertDialog alertDialog;

    private int counter;




    public OwnProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAd();
        mPresenter = new OwnProjectPresenter(this);
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
        View view = inflater.inflate(R.layout.fragment_own_project, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvProject = view.findViewById(R.id.rv_project);
        rvProject.setNestedScrollingEnabled(false);
        rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProject.setAdapter(adapter);

    }


    @Override
    public void startProjectActivity(Project project) {
        Intent intent = new Intent(getContext(), ProjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        intent.putExtras(bundle);

        startActivity(intent);


    }

    @Override
    public void startEditProjectActivity() {
        Intent intent = new Intent(getContext(), UpdateProjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    @Override
    public void deleteProject(Project project){
        mPresenter.showDialog(project);
    }

    @Override
    public void startShareProjectActivity() {
        Intent intent = new Intent(getContext(),SearchUserActivity.class);
        intent.putExtra(Constant.PROJECT_ID,project.getId());
        intent.putExtra(Constant.PROJECT_NAME,project.getProject_name());

        startActivity(intent);
    }

    @Override
    public void startShareUserActivity() {
        Intent intent = new Intent(getContext(), SharedUserActivity.class);
        intent.putExtra(Constant.PROJECT_ID,project.getId());

        startActivity(intent);
    }

    @Override
    public void showDeleteDialog(final Project project) {
        String titleText ="Alert";

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);

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
        alertDialog.setMessage("Do you Really want to Delete this Project??");
        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.delete);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(R.color.red));
        }
        alertDialog.setIcon(drawable);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.alertNegativeClick();
                //dialogInterface.dismiss();

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
                mPresenter.alertPositiveClick(project);

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
        }else {
            Toast.makeText(getContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        /*if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setupConfig();

        }*/
    }






    @Override
    public void setupAdapter(Query query) {
        adapter =
                new FirebaseRecyclerAdapter<Project, ProjectHolder>(Project.class, R.layout.single_project_new, ProjectHolder.class,
                        query) {

                    @Override
                    protected void populateViewHolder(final ProjectHolder viewHolder, final Project model, int position) {
                        viewHolder.bindProject(model);

                        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPresenter.actionClick(viewHolder,model,0);
                            }
                        });



                        viewHolder.getIvViewProject().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPresenter.actionClick(viewHolder,model,0);
                            }
                        });

                        viewHolder.getIvEdit().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPresenter.actionClick(null,model,1);

                            }
                        });

                        viewHolder.getIvShare().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPresenter.actionClick(null,model,3);
                            }
                        });

                        viewHolder.getIvUser().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPresenter.actionClick(null,model,4);
                            }
                        });

                        viewHolder.getIvDelete().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPresenter.actionClick(null,model,2);

                            }
                        });

                    }

                };
    }

    @Override
    public void showInterAd(ProjectHolder holder, Project project, int action) {
        this.action = action;
        this.viewHolder = holder;
        this.project = project;

        counter++;

        if(counter%2==0){
            if(getmInterstitialAd().isLoaded()){
                showAd();
            }else {
                getmInterstitialAd().loadAd(new AdRequest.Builder().build());
                mPresenter.performTaskAsPerAction(this.action);
            }
        }else{
            getmInterstitialAd().loadAd(new AdRequest.Builder().build());
            mPresenter.performTaskAsPerAction(this.action);
        }



    }




}
