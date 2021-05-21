package com.example.sunday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class dbhelper extends SQLiteOpenHelper {
    public static final int db_version = 1;
    public static final String data_id= "Id";
    public static final String data_year= "Year";
    public static final String data_month= "Month";
    public static final String data_date= "Date";
    public static final String data_hr= "Hr";
    public static final String data_min= "Min";
    public static final String data_emotion = "Emotion";
    public static final String Emotion_Table_name = "emotion_time";

    public dbhelper( Context context) {
        super(context, Emotion_Table_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLcreate_table_statement = "CREATE TABLE " + Emotion_Table_name + " ( "+ data_id +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                data_year + " INT, " +
                data_month + " INT, " +
                data_date + " INT, " +
                data_hr + " INT, " +
                data_min + " INT, " +
                data_emotion + " INT)";
        db.execSQL(SQLcreate_table_statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add_data(emotion_node node){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(data_year, node.getYear());
        cv.put(data_month, node.getMonth());
        cv.put(data_date, node.getDate());
        cv.put(data_hr, node.getHr());
        cv.put(data_min, node.getMin());
        cv.put(data_emotion, node.getEmotion_count());

        db.insert(Emotion_Table_name, null, cv );

        db.close();
    }

    public LinkedList<emotion_node> view_data(){
        Calendar current = Calendar.getInstance();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ Emotion_Table_name, null);
        LinkedList<emotion_node> emotion_nodeLinkedList = new LinkedList<emotion_node>();
        int today = current.get(Calendar.DATE);
        int this_month = current.get(Calendar.MONTH)+1;
        int this_year = current.get(Calendar.YEAR);

        if(c.moveToFirst()){
            do{
                if((this_year==c.getInt(1)&&(this_month==c.getInt(2)&&(today == c.getInt(3))))){
                    Calendar calendar= Calendar.getInstance();
                    calendar.set(Calendar.YEAR,c.getInt(1));
                    calendar.set(Calendar.MONTH, c.getInt(2)-1);
                    calendar.set(Calendar.DAY_OF_MONTH, c.getInt(3));
                    calendar.set(Calendar.HOUR_OF_DAY, c.getInt(4));
                    calendar.set(Calendar.MINUTE, c.getInt(5));
                    emotion_node tempnode= new emotion_node(calendar, c.getInt(6));
                    emotion_nodeLinkedList.addFirst(tempnode);
                }

            }while (c.moveToNext());


        }


        db.close();
        return emotion_nodeLinkedList;

    }

    public int get_date_unhappyemo(int year, int month, int date){
        month = month+1;
        int unhappy_emotion_count =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ Emotion_Table_name, null);


        if(c.moveToFirst()){
            do{
                if((year==c.getInt(1))&&(month==c.getInt(2))&&(date == c.getInt(3))&&(2==c.getInt(6))){
                   unhappy_emotion_count++;
                }
            }while (c.moveToNext());
        }
        db.close();
        return unhappy_emotion_count;}



    public int get_date_happyemo(int year, int month, int date){
        month = month+1;
        int happy_emotion_count =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ Emotion_Table_name, null);


        if(c.moveToFirst()){
            do{
                if((year==c.getInt(1))&&(month==c.getInt(2))&&(date == c.getInt(3))&&(1==c.getInt(6))){
                    happy_emotion_count++;
                }
            }while (c.moveToNext());
        }
        db.close();
        return happy_emotion_count;}

}
