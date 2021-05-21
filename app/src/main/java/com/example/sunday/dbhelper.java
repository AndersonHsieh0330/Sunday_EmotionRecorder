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
        cv.put(data_month, node.getMonth());
        cv.put(data_date, node.getDate());
        cv.put(data_hr, node.getHr());
        cv.put(data_min, node.getMin());
        cv.put(data_emotion, node.getEmotion_count());

        db.insert(Emotion_Table_name, null, cv );

        db.close();
    }

    public LinkedList<emotion_node> view_data(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ Emotion_Table_name, null);
        LinkedList<emotion_node> emotion_nodeLinkedList = new LinkedList<emotion_node>();
        int today = Calendar.getInstance().get(Calendar.DATE);

        if(c.moveToFirst()){
            do{
                if(today == c.getInt(2)){
                    Calendar calendar= Calendar.getInstance();
                    calendar.set(Calendar.MONTH, c.getInt(1)-1);
                    calendar.set(Calendar.DAY_OF_MONTH, c.getInt(2));
                    calendar.set(Calendar.HOUR_OF_DAY, c.getInt(3));
                    calendar.set(Calendar.MINUTE, c.getInt(4));
                    emotion_node tempnode= new emotion_node(calendar, c.getInt(5));
                    emotion_nodeLinkedList.addFirst(tempnode);
                }

            }while (c.moveToNext());


        }


        db.close();
        return emotion_nodeLinkedList;

    }



}
