package com.forbitbd.constructiontm.utility;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sohel on 1/27/2018.
 */

public class MyUtil {
    private static final String DATE_FORMAT="dd-MMM-yyyy";
    private static final String DATE_FORMAT2="dd-MM-yy";

    private static char[] eng_arr = {'0','1','2','3','4','5','6','7','8','9'};
    private static char[] bangla_arr = {'০','১','২','৩','৪','৫','৬','৭','৮','৯'};


    public static String convertToBangla(String number){
        String values = "";
        char[] charArr = number.toCharArray();

        for(int i=0;i<charArr.length;i++){

            char c=0;

            for(int j=0;j<eng_arr.length;j++){
                if(charArr[i]==eng_arr[j]){
                    c = bangla_arr[j];
                    //values.concat(String.valueOf(bangla_arr[j]));
                    break;
                }else {
                    c=charArr[i];
                }
            }

            values= values.concat(String.valueOf(c));
        }

        return values;


    }

    public static String getStringDate(Date date){
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static Date getDate(String dateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.parse(dateStr);
    }

    public static String getStringDate2(Date date){
        DateFormat df = new SimpleDateFormat(DATE_FORMAT2);
        return df.format(date);
    }

    public static int getDuration(long fDate,long sDate){
        long diff = fDate-sDate;
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1);
    }

    public static long getBeginingTime(long time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);


        int year = calendar.get(Calendar.YEAR);
        int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(year,month,dayofmonth,0,0,0);


        return calendar.getTimeInMillis();
    }

    public static long getDayAfter(long date,int dayAfter){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        int year = calendar.get(Calendar.YEAR);
        int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(year,month,dayofmonth+dayAfter,0,0,0);


        return calendar.getTimeInMillis();
    }

    public static long getEndingTime(long time){

        long beginingTime = getBeginingTime(time);

        return beginingTime+(23*60*60+59*60+59)*1000;

    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public static String decodeString(String string) {
        return string.replace(",", ".");
    }


    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }


    public static byte[] getScaleByteArray (Context c, Uri uri, int width, int height) {
        byte[] byteArray = null;
        try {
            Bitmap nb = BitmapFactory.decodeStream (c.getContentResolver().openInputStream(uri));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(nb, width, height, false);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public static List<Integer> getScreenDimension(Context context){

        List<Integer> retList = new ArrayList<>();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        retList.add(width);
        retList.add(height);

        return retList;
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap, int width, int height){
        return Bitmap.createScaledBitmap(bitmap,width,height,false);

    }

    public static byte[] getByteArray(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);

        return stream.toByteArray();

    }






}
