package com.forbitbd.constructiontm.ui.taskDetail.workdoneTable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.DailyWorkdone;
import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.model.WorkDone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sohel on 20-04-18.
 */

public class WorkDoneAdapter extends RecyclerView.Adapter<WorkDoneAdapter.WorkDoneHolder>{

    private List<DailyWorkdone> dailyWorkdoneList;
    private LayoutInflater inflater;
    private String unit;

    public WorkDoneAdapter(Context context) {
        this.dailyWorkdoneList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public WorkDoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_work_done,parent,false);

        WorkDoneHolder holder = new WorkDoneHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WorkDoneHolder holder, int position) {
        DailyWorkdone dailyWorkdone = dailyWorkdoneList.get(position);

        holder.bind(dailyWorkdone);

       /* holder.tvDate.setText(MyUtil.getStringDate(new Date(workDone.getDate())));
        holder.tvWorkDone.setText(String.valueOf(workDone.getAmount()).concat(" ").concat(unit));*/

    }

    public void addItem(DailyWorkdone dailyWorkdone){
        dailyWorkdoneList.add(dailyWorkdone);

        int pos = dailyWorkdoneList.indexOf(dailyWorkdone);
        notifyItemInserted(pos);
    }

    public void clear(){
        this.dailyWorkdoneList.clear();
        notifyDataSetChanged();
    }

   /* public void setData(List<WorkDone> workDoneList, String unit){
        this.workDoneList = workDoneList;
        this.unit = unit;
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return dailyWorkdoneList.size();
    }

    public class WorkDoneHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvWorkDone;

        public WorkDoneHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.date);
            tvWorkDone = itemView.findViewById(R.id.work_done);
        }


        public void bind(DailyWorkdone dailyWorkdone){
            tvDate.setText(dailyWorkdone.getDate());
            tvWorkDone.setText(dailyWorkdone.getAmount()+"");
        }
    }
}
