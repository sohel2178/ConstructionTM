package com.forbitbd.constructiontm.ui.taskUpdate;


import com.forbitbd.constructiontm.model.Task;

public interface UpdateTaskContract {
    interface Presenter{
        void requestForUnit(String projectId);
        boolean validate(Task task, long start, long finished, String volOfWork, String unitRate);
        void updateTask(Task task, String projectId, String unit);

    }

    interface View{
        void addUnitToAdapter(String unit);
        void showErrorMessage(String message, int field);
        void showDialog();
        void complete(Task task);
        void clearPreError();

    }
}
