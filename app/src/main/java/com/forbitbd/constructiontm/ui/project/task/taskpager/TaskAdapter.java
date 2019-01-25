package com.forbitbd.constructiontm.ui.project.task.taskpager;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private Context context;
    private List<Task> taskList;
    private LayoutInflater inflater;
    private ProjectPermission permission;
    private TaskClickListener listener;


    public TaskAdapter(Context context, ProjectPermission permission, TaskClickListener listener) {
        this.context = context;
        this.permission = permission;
        this.taskList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_tast_new,parent,false);
        return  new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {

        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void clear(){
        this.taskList.clear();
        notifyDataSetChanged();
    }


    public void addTask(Task task){
        this.taskList.add(task);
        int position = taskList.indexOf(task);
        notifyItemInserted(position);
    }

    public void setData(List<Task> taskList){
        clear();
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void removeTask(Task task){
        int pos = taskList.indexOf(task);
        taskList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void updateItem(String id, double newWorkdone){
        for (Task x: taskList){
            if(x.getTask_id().equals(id)){
                x.setTask_volume_of_work_done(newWorkdone);
                break;
            }
        }
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        FoldingCell mFoldingCell;

        TextView tvName,tvRemaining,tvNameTwo,tvStartDate,tvFinishedDate,tvDuration,tvVolumeofWorks,getTvVolumeofWorkDone;
        private CircleProgressBar mProgressBar;

        private ImageView ivIndicator;

        ImageView ivDelete,ivAdd,ivEdit,ivView;

        public TaskHolder(View itemView) {
            super(itemView);

            mFoldingCell = itemView.findViewById(R.id.folding_cell);

            tvName = itemView.findViewById(R.id.name);
            tvRemaining = itemView.findViewById(R.id.remaining);
            ivIndicator = itemView.findViewById(R.id.indicator);

            tvNameTwo = itemView.findViewById(R.id.name_two);
            mProgressBar = itemView.findViewById(R.id.progressBar);

           // tvProgress = itemView.findViewById(R.id.progress);
            tvStartDate = itemView.findViewById(R.id.start_date);
            tvFinishedDate = itemView.findViewById(R.id.finished_date);
            tvDuration = itemView.findViewById(R.id.duration);

            tvVolumeofWorks = itemView.findViewById(R.id.vol_of_works);
            getTvVolumeofWorkDone = itemView.findViewById(R.id.vol_of_work_done);

            mFoldingCell.setOnClickListener(this);

            //rlHide = itemView.findViewById(R.id.hide_container);

            ivDelete = itemView.findViewById(R.id.delete);
            ivAdd = itemView.findViewById(R.id.fab_add);
            ivEdit = itemView.findViewById(R.id.edit);
            ivView = itemView.findViewById(R.id.view_project);

            ivDelete.setOnClickListener(this);
            ivAdd.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            ivView.setOnClickListener(this);

        }

        public void bind(Task task){
            tvName.setText(task.getTask_name());

            tvNameTwo.setText(task.getTask_name());
            tvStartDate.setText(MyUtil.getStringDate(new Date(task.getTask_start_date())));
            tvFinishedDate.setText(MyUtil.getStringDate(new Date(task.getTask_finished_date())));
            tvDuration.setText(String.valueOf(MyUtil.getDuration(task.getTask_finished_date(),task.getTask_start_date())).concat(" Days"));

            double pro = task.getTask_volume_of_work_done()/task.getTask_volume_of_works()*100;

            DecimalFormat df = new DecimalFormat("#.##");
            //tvProgress.setText(df.format(pro).concat(" %"));

            tvVolumeofWorks.setText(df.format(task.getTask_volume_of_works())
                    .concat(" ").concat(task.getUnit()));
            getTvVolumeofWorkDone.setText(df.format(task.getTask_volume_of_work_done())
                    .concat(" ").concat(task.getUnit()));

            mProgressBar.setProgress((int)(task.getTask_volume_of_work_done()/task.getTask_volume_of_works()*100));
           /* mProgressBar.setSecondaryProgress(100);
            mProgressBar.setMax(100);*/

            switch (task.getState()){
                case 1:
                    ivIndicator.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    tvRemaining.setText(task.getRemainingDays()+" Days Remaining");
                    break;

                case 2:
                    ivIndicator.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                    tvRemaining.setText("Task Completed");
                    break;

                case 3:
                    ivIndicator.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    tvRemaining.setText("Task Expired");
                    break;
            }

             if(permission!=null) {
                 ivEdit.setVisibility(View.GONE);
                 ivDelete.setVisibility(View.GONE);

                 if (permission.getActivityWrite() == 0) {
                     ivAdd.setVisibility(View.GONE);
                 } else {
                     ivAdd.setVisibility(View.VISIBLE);
                 }
             }


        }


        @Override
        public void onClick(View view) {

            if(view==mFoldingCell){
                mFoldingCell.toggle(false);
            }else if(view==ivAdd){
                listener.onAddClick(taskList.get(getAdapterPosition()));
            }else if(view==ivEdit){
                listener.onEditClick(taskList.get(getAdapterPosition()));
            }else if(view==ivDelete){
                listener.onDeleteClick(taskList.get(getAdapterPosition()));
            }else if(view ==ivView){
                listener.onItemClick(taskList.get(getAdapterPosition()));
            }

        }
    }
}
