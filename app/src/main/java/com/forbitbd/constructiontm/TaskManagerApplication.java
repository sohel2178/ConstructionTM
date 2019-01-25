package com.forbitbd.constructiontm;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TaskManagerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
        FirebaseDatabase.getInstance().getReference().keepSynced(false);
    }
}
