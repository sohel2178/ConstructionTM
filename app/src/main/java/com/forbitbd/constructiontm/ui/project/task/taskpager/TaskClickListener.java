package com.forbitbd.constructiontm.ui.project.task.taskpager;


import com.forbitbd.constructiontm.model.Task;

/**
 * Created by sohel on 4/19/2018.
 */

public interface TaskClickListener {

    void onItemClick(Task task);
    void onAddClick(Task task);
    void onEditClick(Task task);
    void onDeleteClick(Task task);


}
