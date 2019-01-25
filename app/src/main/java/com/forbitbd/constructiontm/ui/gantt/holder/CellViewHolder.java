/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.forbitbd.constructiontm.ui.gantt.holder;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.ui.gantt.model.Cell;

/**
 * Created by evrencoskun on 23/10/2017.
 */

public class CellViewHolder extends AbstractViewHolder {

    public final TextView cell_textview,tvProgress;
    public final LinearLayout cell_container;
    private Cell cell;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
        tvProgress = (TextView) itemView.findViewById(R.id.progress);
        cell_container = (LinearLayout) itemView.findViewById(R.id.cell_container);
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        //cell_textview.setText(String.valueOf(cell.getData()));


        String data = String.valueOf(cell.getData());
        String[] arr = data.split(" ");

        String barController = arr[0];
        String progressController = arr[1];
        String progress = arr[2];


        if(barController.equals("1")) {
            cell_textview.setBackground(ContextCompat.getDrawable(cell_textview.getContext(), R.drawable.gantt_back));
            //tvProgress.setBackground(ContextCompat.getDrawable(tvProgress.getContext(), R.drawable.my_layer));
        }
        else {
            cell_textview.setBackgroundColor(ContextCompat.getColor(cell_textview.getContext(),R.color.white));
            //tvProgress.setBackgroundColor(ContextCompat.getColor(cell_textview.getContext(),R.color.white));
        }

        if(progressController.equals("1")){
            tvProgress.setBackground(ContextCompat.getDrawable(tvProgress.getContext(), R.drawable.full_progress));
        }else if(progressController.equals("2")){
            tvProgress.setBackground(ContextCompat.getDrawable(tvProgress.getContext(), R.drawable.my_layer));
            if(progress!=null){
                tvProgress.setText(progress.concat(" %"));
            }
        }
        else{
            tvProgress.setBackgroundColor(ContextCompat.getColor(cell_textview.getContext(),R.color.white));
        }


        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();
    }
}