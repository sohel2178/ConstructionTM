package com.forbitbd.constructiontm.holder;

import android.os.Build;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Project;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by sohel on 5/29/2018.
 */

public class SharedProjectHolder extends RecyclerView.ViewHolder {
    private View mView;
    private MyDatabaseRef myDatabaseRef;

    private RelativeLayout ivDelete,ivViewProject;

    private LinearLayout mHideContainer;
    private LinearLayout mMainContainer;

    public SharedProjectHolder(View itemView) {
        super(itemView);
        mView = itemView;
        this.myDatabaseRef = new MyDatabaseRef();


    }

    public void bindProject(ProjectPermission sharedProject){

        final ImageView ivOwnerImage = itemView.findViewById(R.id.owner_image);
        final TextView tvName = itemView.findViewById(R.id.tv_name);
        final TextView tvLocation = itemView.findViewById(R.id.tv_location);
        final TextView tvOwnerName = itemView.findViewById(R.id.owner_name);

        ivDelete = itemView.findViewById(R.id.delete);
        ivViewProject = itemView.findViewById(R.id.view_project);
        mHideContainer = itemView.findViewById(R.id.hide_container);
        mMainContainer = itemView.findViewById(R.id.main_container);

        mMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideContainer();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mView.setTransitionName(sharedProject.getProjectId());

        }

        myDatabaseRef.getProjectRef().child(sharedProject.getProjectId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue()!=null){
                            Project project = dataSnapshot.getValue(Project.class);

                           /* if(!project.getImage_url().equals("")){
                                Picasso.with(mView.getContext())
                                        .load(project.getImage_url())
                                        .into(imageView);
                            }*/

                            tvName.setText(project.getProject_name());
                            tvLocation.setText(mView.getContext().getString(R.string.location).concat("\n").concat(project.getProject_location()));

                            myDatabaseRef.getUserRef().child(project.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue()!=null){
                                                User user = dataSnapshot.getValue(User.class);
                                                tvOwnerName.setText(user.getName());

                                                if(!user.getPhotoUri().equals("")){
                                                    Picasso.with(mView.getContext())
                                                            .load(user.getPhotoUri())
                                                            .into(ivOwnerImage);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    /*public View getRootView(){
        return mView;
    }*/


    public RelativeLayout getDeleteButton(){
        return ivDelete;
    }
    public RelativeLayout getViewProjectButton(){
        return ivViewProject;
    }

    private void hideContainer(){
        TransitionManager.beginDelayedTransition(mMainContainer);
        if(mHideContainer.getTag()==null){
            mHideContainer.setVisibility(View.VISIBLE);
            mHideContainer.setTag("TAG");

        }else{
            mHideContainer.setVisibility(View.GONE);
            mHideContainer.setTag(null);
        }
    }



}
