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

package com.forbitbd.constructiontm.ui.gantt;

import android.content.Context;
import android.util.Log;


import com.forbitbd.constructiontm.utility.MyUtil;
import com.forbitbd.constructiontm.model.Task;
import com.forbitbd.constructiontm.ui.gantt.model.Cell;
import com.forbitbd.constructiontm.ui.gantt.model.ColumnHeader;
import com.forbitbd.constructiontm.ui.gantt.model.RowHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class TableViewModel {

    // Columns indexes
    public static final int MOOD_COLUMN_INDEX = 3;
    public static final int GENDER_COLUMN_INDEX = 4;

    // Constant values for icons
    public static final int SAD = 1;
    public static final int HAPPY = 2;
    public static final int BOY = 1;
    public static final int GIRL = 2;

    // Constant size for dummy data sets
    private static final int COLUMN_SIZE = 500;
    private static final int ROW_SIZE = 500;

    // Drawables
    /*private final Drawable mBoyDrawable;
    private final Drawable mGirlDrawable;
    private final Drawable mHappyDrawable;
    private final Drawable mSadDrawable;*/

    private Context mContext;
    private List<Task> taskList;

    private int row_size, column_size;
    private long startTime,endTime;

    public TableViewModel(Context context, List<Task> taskList) {
        mContext = context;
        this.taskList = taskList;
        this.row_size = taskList.size();

        List<Long> timeLIne = getTimeLine();
        this.column_size = MyUtil.getDuration(timeLIne.get(1),timeLIne.get(0));
        this.startTime = timeLIne.get(0);
        this.endTime = timeLIne.get(1);

        Log.d("HHHH",column_size+" ,"+row_size);

    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            RowHeader header = new RowHeader(String.valueOf(i), taskList.get(i).getTask_name());
            list.add(header);
        }

        return list;
    }

    private List<Long> getTimeLine(){
        List<Long> timelines = new ArrayList<>();

        long initialDate=0;
        long finalDate=0;

        for (Task x: taskList){
            if(taskList.indexOf(x)==0){
                initialDate= x.getTask_start_date();
                finalDate = x.getTask_finished_date();
            }else {
                if(initialDate>=x.getTask_start_date()){
                    initialDate = x.getTask_start_date();
                }

                if(finalDate<=x.getTask_finished_date()){
                    finalDate = x.getTask_finished_date();
                }
            }
        }

        timelines.add(initialDate);
        timelines.add(finalDate);

        return timelines;

    }

    /**
     * This is a dummy model list test some cases.
     */
    public static List<RowHeader> getSimpleRowHeaderList(int startIndex) {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "row " + (startIndex + i));
            list.add(header);
        }

        return list;
    }


    private List<ColumnHeader> getSimpleColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();



        for (int i = 0; i< column_size; i++){
            ColumnHeader header = null;
            if(i==0){
                header = new ColumnHeader(String.valueOf(i), MyUtil.getStringDate(new Date(startTime)));
            }else {
                header = new ColumnHeader(String.valueOf(i), MyUtil.getStringDate(new Date(MyUtil.getDayAfter(startTime,i))));
            }
            list.add(header);

        }



      /*  for (int i = 0; i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            if (i % 6 == 2) {
                title = "large column " + i;
            } else if (i == MOOD_COLUMN_INDEX) {
                title = "mood";
            } else if (i == GENDER_COLUMN_INDEX) {
                title = "gender";
            }
            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }*/

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        List<Long> timeLIne = getTimeLine();
        int duration = MyUtil.getDuration(timeLIne.get(1),timeLIne.get(0));

        Log.d("HHHHH",duration+"");

        /*for (int i=0;i<duration;i++){
            String dateStr = MyUtil.getStringDate(new Date(MyUtil.getDayAfter(timeLIne.get(0),i)));
            Log.d("JJJJJJJJJJJJJJJJJJ",dateStr);
        }*/

        for (int i = 0; i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            int nRandom = new Random().nextInt();
            if (nRandom % 4 == 0 || nRandom % 3 == 0 || nRandom == i) {
                title = "large column " + i;
            }

            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }

        return list;
    }

    private List<List<Cell>> getSimpleCellList() {
        List<List<Cell>> list = new ArrayList<>();

        /*for (int i=0;i<row_size;i++){
            List<Cell> cellList = new ArrayList<>();

            for (int j = 0; j<column_size; j++){
                String id = j + "-" + i;

                Cell cell = new Cell(id, "pp");
                cellList.add(cell);
            }
            list.add(cellList);
        }*/
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();

            for (int j = 0; j < COLUMN_SIZE; j++) {
                String text = "cell " + j + " " + i;
                if (j % 4 == 0 && i % 5 == 0) {
                    text = "large cell " + j + " " + i + ".";
                }
                String id = j + "-" + i;

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<List<Cell>> getCellListForSortingTest() {
        List<List<Cell>> list = new ArrayList<>();

       /* for (int i=0;i<column_size;i++){
            List<Cell> cellList = new ArrayList<>();

            for (int j = 0; j< row_size; j++){
                String id = j + "-" + i;

                Object text = "cell " + j + " " + i;

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
            list.add(cellList);
        }*/
        for (int i = 0; i < row_size; i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < column_size; j++) {
                Object text = "cell " + j + " " + i;

                /*final int random = new Random().nextInt();
                if (j == 0) {
                    text = i;
                } else if (j == 1) {
                    text = random;
                } else if (j == MOOD_COLUMN_INDEX) {
                    text = random % 2 == 0 ? HAPPY : SAD;
                } else if (j == GENDER_COLUMN_INDEX) {
                    text = random % 2 == 0 ? BOY : GIRL;
                }*/

                // Create dummy id.
                String id = j + "-" + i;

                Cell cell;
                if (j == 3) {
                    cell = new Cell(id, text);
                } else if (j == 4) {
                    // NOTE female and male keywords for filter will have conflict since "female"
                    // contains "male"
                    cell = new Cell(id, text);
                } else {
                    cell = new Cell(id, text);
                }
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<List<Cell>> getRandomCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String text = "cell " + j + " " + i;
                int random = new Random().nextInt();
                if (random % 2 == 0 || random % 5 == 0 || random == j) {
                    text = "large cell  " + j + " " + i + getRandomString() + ".";
                }

                // Create dummy id.
                String id = j + "-" + i;

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    private List<List<Cell>> getEmptyCellList() {
        List<List<Cell>> list = new ArrayList<>();

        for (int i = 0; i < row_size; i++) {
            Task task = taskList.get(i);

            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);

            int offset = MyUtil.getDuration(task.getTask_start_date(),startTime)-1;
            int duration = MyUtil.getDuration(task.getTask_finished_date(),task.getTask_start_date());

            int off_plus_dur = offset+duration;

            double each_segment = 100/(duration);
            double progress = task.getTask_volume_of_work_done()*100/task.getTask_volume_of_works();



            for (int j = 0; j < column_size; j++) {

                String id = j + "-" + i;
                Cell cell=null;

                int firstValue,secondValue=0;



                if(j>=offset && j<off_plus_dur){
                    firstValue = 1;
                    //cell = new Cell(id, "1");
                    if(each_segment*(j+1-offset)<progress){
                        secondValue =1;
                    }else {

                        if((each_segment*(j+1-offset)-progress)<each_segment){
                            secondValue =2;
                        }else {
                            secondValue=0;
                        }
                    }
                }else {
                    firstValue =0;
                }



                cell = new Cell(id,firstValue+" "+secondValue+" "+progress);

                cellList.add(cell);
            }
        }

        return list;
    }

    private List<RowHeader> getEmptyRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "");
            list.add(header);
        }

        return list;
    }

    /**
     * This is a dummy model list test some cases.
     */
    public static List<List<Cell>> getRandomCellList(int startIndex) {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String text = "cell " + j + " " + (i + startIndex);
                int random = new Random().nextInt();
                if (random % 2 == 0 || random % 5 == 0 || random == j) {
                    text = "large cell  " + j + " " + (i + startIndex) + getRandomString() + ".";
                }

                String id = j + "-" + (i + startIndex);

                Cell cell = new Cell(id, text);
                cellList.add(cell);
            }
        }

        return list;
    }


    private static String getRandomString() {
        Random r = new Random();
        String str = " a ";
        for (int i = 0; i < r.nextInt(); i++) {
            str = str + " a ";
        }

        return str;
    }


    public List<List<Cell>> getCellList() {
        return getEmptyCellList();
    }

    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return getSimpleColumnHeaderList();
    }


}
