package com.forbitbd.constructiontm.database;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by IMATPC-12 on 06-Mar-18.
 */

public class MyStorageRef {

    private static final String PROJECT_REF="Projects";
    private static final String EMPLOYEE_REF="Employees";
    private static final String STORE_REF="Stores";
    private static final String USER_REF="Users";

    private static MyStorageRef instance;

    private StorageReference mStorageRef;

    public MyStorageRef() {
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    public StorageReference getProjectStorageRef(){
        return mStorageRef.child(PROJECT_REF);
    }

    public StorageReference getEmployeeRef(){
        return getProjectStorageRef().child(EMPLOYEE_REF);
    }

    public StorageReference getStoreRef(String projectId){
        return getProjectStorageRef().child(STORE_REF).child(projectId);
    }

    public StorageReference getUserStoreRef(){
        return getProjectStorageRef().child(USER_REF);
    }
}
