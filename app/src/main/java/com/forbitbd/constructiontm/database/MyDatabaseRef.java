package com.forbitbd.constructiontm.database;

import com.forbitbd.constructiontm.utility.MyUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by sohel on 07-02-18.
 */

public class MyDatabaseRef {
    private static final String USER_REF="Users";
    private static final String PROJECT_REF="Projects";
    private static final String SLIDER_REF="Slider";
    private static final String TASK_REF="Tasks";
    private static final String UNIT_REF="Units";
    private static final String ACCOUNT_REF="Accounts";
    private static final String TRANSACTION_REF="Transaction";
    private static final String TRANSACTION_IMAGE_REF="TransactionImages";
    private static final String EMPLOYEE_REF="Employees";
    private static final String WORK_DONE_REF="WorkDone";
    private static final String STORE_REF="Stores";
    private static final String STORE_ITEM_REF="StoreItems";
    private static final String SHARED_PROJECT_REF="SharedProject";
    private static final String SHARED_USER_REF="SharedUser";
    private static final String SUPPLIER_REF="SupplierRef";
    private static final String SUPER_ADMIN="SuperAdmin";

    private static MyDatabaseRef instance;



    private FirebaseDatabase database;

    public MyDatabaseRef() {
        this.database  = FirebaseDatabase.getInstance();
    }


    public DatabaseReference getUserRef(){
        return database.getReference(USER_REF);
    }

    public DatabaseReference getProjectRef(){
        return database.getReference(PROJECT_REF);
    }
    public DatabaseReference getTaskRef(String projectId){
        database.getReference(TASK_REF).child(projectId).keepSynced(false);
        return database.getReference(TASK_REF).child(projectId);
    }


    public DatabaseReference getUnitRef(String projectId){
        return database.getReference(UNIT_REF).child(projectId);
    }

    public DatabaseReference getAccountRef(String projectId){
        return database.getReference(ACCOUNT_REF).child(projectId);
    }

    public DatabaseReference getTransactionRef(String projectId){
        return database.getReference(TRANSACTION_REF).child(projectId);
    }

    public DatabaseReference getEmployeeRef(String projectId){
        return database.getReference(EMPLOYEE_REF).child(projectId);
    }

    public DatabaseReference getTransactionImageRef(String projectId, String transactionId){
        return database.getReference(TRANSACTION_IMAGE_REF).child(projectId).child(transactionId);
    }

    public DatabaseReference getSliderRef(){
        return database.getReference(SLIDER_REF);
    }

    public Query getDailyTransactionQuery(String projectId, long dateTime){
        long beginningTime = MyUtil.getBeginingTime(dateTime);
        long endingTime = MyUtil.getEndingTime(dateTime);
        return getTransactionRef(projectId).orderByChild("date_time").startAt(beginningTime).endAt(endingTime);
    }

    public DatabaseReference getWorkDoneRef(String projectId){
        return database.getReference(WORK_DONE_REF).child(projectId);
    }

    public DatabaseReference getStoreRef(String projectId){
        return database.getReference(STORE_REF).child(projectId);
    }

    public DatabaseReference getStoreItemRef(String projectId){
        return database.getReference(STORE_ITEM_REF).child(projectId);
    }

    public DatabaseReference getSharedProjectRef(String userUID){
        return database.getReference(SHARED_PROJECT_REF).child(userUID);
    }

    public DatabaseReference getSharedUserRef(String projectId){
        return database.getReference(SHARED_USER_REF).child(projectId);
    }

    public DatabaseReference getSupplierRef(String projectId){
        return database.getReference(SUPPLIER_REF).child(projectId);
    }

    public DatabaseReference getSuperAdminRef(){
        return database.getReference(SUPER_ADMIN);
    }
}
