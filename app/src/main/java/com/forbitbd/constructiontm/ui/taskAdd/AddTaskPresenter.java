package com.forbitbd.constructiontm.ui.taskAdd;

import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddTaskPresenter implements AddTaskContract.Presenter {

    private AddTaskContract.View mView;
    private MyDatabaseRef myDatabaseRef;

    public AddTaskPresenter(AddTaskContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public void requestForUnit(String projectId) {
        myDatabaseRef.getUnitRef(projectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()){
                    //unitList.add(x.getValue(String.class));
                    mView.addUnitToAdapter(x.getValue(String.class));
                }
               // unitAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean validate(Task task, long start, long finished, String volOfWork, String unitRate) {
        mView.clearPreError();

        if(task.getTask_name().equals("")){
            mView.showErrorMessage("Empty Field Not Allowed",1);
            return false;
        }

        if(volOfWork.equals("")){
            mView.showErrorMessage("Empty Field Not Allowed",2);
            return false;
        }

        if(unitRate.equals("")){
            mView.showErrorMessage("Empty Field Not Allowed",3);
            return false;
        }

        if(task.getUnit().equals("")){
            mView.showErrorMessage("Empty Field Not Allowed",4);
            return false;
        }

        if(start==-1){
            mView.showErrorMessage("Please Select Start Date",5);
            return false;
        }

        if(finished==-1){
            mView.showErrorMessage("Please Select Start Date",6);
            return false;
        }

        if(start>finished){
            mView.showErrorMessage("Finished Date Must be Equal to or Greater than Start Date",6);
            return false;
        }
        return true;
    }

    @Override
    public void saveTask(final Task task, final String projectId, final String unit) {
        String id =myDatabaseRef.getTaskRef(projectId).push().getKey();
        task.setTask_id(id);

        myDatabaseRef.getTaskRef(projectId).child(id).setValue(task, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                myDatabaseRef.getUnitRef(projectId).child(unit).setValue(unit);
                mView.complete(task);
            }
        });

    }
}
