package com.forbitbd.constructiontm.holder;

import android.content.Context;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.Project;


/**
 * Created by Sohel on 1/27/2018.
 */

public class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;
    ImageView ivEdit,ivShare,ivUser,ivDelete,ivViewProject;
    TextView textView,tvProjectLocation,tvDescription,tvAction;

    LinearLayout mActionContainer,mPseudoContainer;

    private LinearLayout mContainer;

    private RelativeLayout mHideContainer;

    public ProjectHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }


    public void bindProject(Project project){

        textView = mView.findViewById(R.id.tv_name);
        ivEdit = mView.findViewById(R.id.edit);
        ivShare = mView.findViewById(R.id.share);
        ivUser = mView.findViewById(R.id.user);
        ivDelete = mView.findViewById(R.id.delete);
        ivViewProject = mView.findViewById(R.id.view_project);
        mContainer = mView.findViewById(R.id.container);
        mHideContainer = mView.findViewById(R.id.hide_container);

        mActionContainer = mView.findViewById(R.id.action_container);
        mPseudoContainer = mView.findViewById(R.id.pseudo_container);

        tvAction = mView.findViewById(R.id.txt_action);
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transition slide = new Slide(Gravity.RIGHT);
                TransitionManager.beginDelayedTransition(mHideContainer,slide);

                TransitionManager.beginDelayedTransition(mHideContainer);
                if(mPseudoContainer.getVisibility()==View.VISIBLE){
                    mPseudoContainer.setVisibility(View.GONE);
                }

            }
        });

        mHideContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transition slide = new Slide(Gravity.RIGHT);
                TransitionManager.beginDelayedTransition(mHideContainer,slide);
                if(mPseudoContainer.getVisibility()==View.GONE){
                    mPseudoContainer.setVisibility(View.VISIBLE);
                }
            }
        });



        tvProjectLocation = mView.findViewById(R.id.tv_location);
        tvDescription = mView.findViewById(R.id.tv_description);
        textView.setText(project.getProject_name());
        tvDescription.setText(project.getProject_description());
        tvProjectLocation.setText(mContext.getString(R.string.location).concat("\n").concat(project.getProject_location()));

    }

    public View getView(){
       return mContainer;
    }

    public ImageView getIvEdit(){
        return ivEdit;
    }

    public ImageView getIvShare(){
        return ivShare;
    }

    public ImageView getIvUser(){
        return ivUser;
    }

    public ImageView getIvDelete(){
        return ivDelete;
    }

    public ImageView getIvViewProject(){
        return ivViewProject;
    }



    @Override
    public void onClick(View view) {

    }
}
