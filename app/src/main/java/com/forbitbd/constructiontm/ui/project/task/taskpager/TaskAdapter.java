package com.forbitbd.constructiontm.ui.project.task.taskpager;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.model.ProjectPermission;
import com.forbitbd.constructiontm.model.Task;

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

        holder.tvName.setText(task.getTask_name());
        holder.tvStartDate.setText(MyUtil.getStringDate(new Date(task.getTask_start_date())));
        holder.tvCompletionDate.setText(MyUtil.getStringDate(new Date(task.getTask_finished_date())));
        holder.tvDuration.setText(String.valueOf(MyUtil.getDuration(task.getTask_finished_date(),task.getTask_start_date())).concat(" Days"));


        double pro = task.getTask_volume_of_work_done()/task.getTask_volume_of_works()*100;

        DecimalFormat df = new DecimalFormat("#.##");
        holder.tvProgress.setText(df.format(pro).concat(" %"));

        holder.tvVolumeofWorks.setText(df.format(task.getTask_volume_of_works())
                .concat(" ").concat(task.getUnit()));
        holder.getTvVolumeofWorkDone.setText(df.format(task.getTask_volume_of_work_done())
                .concat(" ").concat(task.getUnit()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mProgressBar.setProgress((int)(task.getTask_volume_of_work_done()/task.getTask_volume_of_works()*100),true);
        }else{
            holder.mProgressBar.setProgress((int)(task.getTask_volume_of_work_done()/task.getTask_volume_of_works()*100));
        }

        if(permission!=null){
            holder.ivEdit.setVisibility(View.GONE);
            holder.ivDelete.setVisibility(View.GONE);

            if(permission.getActivityWrite()==0){
                holder.ivAdd.setVisibility(View.GONE);
            }else{
                holder.ivAdd.setVisibility(View.VISIBLE);
            }
        }

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

   /* public void addTaskInPosition(Task task,int position){
        this.taskList.add(position,task);
        notifyItemInserted(position);
    }*/

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

        TextView tvName,tvStartDate,tvCompletionDate,tvDuration,tvProgress,tvVolumeofWorks,getTvVolumeofWorkDone;
        private ProgressBar mProgressBar;

        ImageView ivDelete,ivAdd,ivEdit,ivView;

        private RelativeLayout rlHide;

        public TaskHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvStartDate = itemView.findViewById(R.id.start_date);
            tvCompletionDate = itemView.findViewById(R.id.completion_date);
            tvDuration = itemView.findViewById(R.id.duration);
            tvProgress = itemView.findViewById(R.id.progress);
            tvVolumeofWorks = itemView.findViewById(R.id.vol_of_works);
            getTvVolumeofWorkDone = itemView.findViewById(R.id.vol_of_works_done);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            rlHide = itemView.findViewById(R.id.hide_container);

            ivDelete = itemView.findViewById(R.id.delete);
            ivAdd = itemView.findViewById(R.id.fab_add);
            ivEdit = itemView.findViewById(R.id.edit);
            ivView = itemView.findViewById(R.id.view_project);

            itemView.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
            ivAdd.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            ivView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            if(view==itemView){
                if(view.getTag()==null){
                    rlHide.setVisibility(View.VISIBLE);
                    view.setTag("Hell");
                }else{
                    rlHide.setVisibility(View.GONE);

                    view.setTag(null);
                }
                //listener.onItemClick(taskList.get(getAdapterPosition()));
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
