package com.forbitbd.constructiontm.ui.project;


import com.forbitbd.constructiontm.model.ProjectPermission;

public class ProjectPresenter implements ProjectContract.Presenter {

    private ProjectContract.View mView;

    public ProjectPresenter(ProjectContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadRightFragment(ProjectPermission projectPermission) {
        mView.loadActivityFragment();
        /*if(projectPermission==null){
            mView.loadActivityFragment();
        }else{
            if(projectPermission.getActivityRead()==1){

            }else if(projectPermission.getFinanceRead()==1){
                mView.loadFinanceFragment();
            }else if(projectPermission.getStoreRead()==1){
                mView.loadStoreFragment();
            }else if(projectPermission.getEmployeeRead()==1){
                mView.loadEmployeeFragment();
            }
        }*/
    }


}
