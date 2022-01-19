package com.example.listto.model;

import static com.example.listto.util.CalenderUtil.getStringToDateSql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applandeo.materialcalendarview.EventDay;
import com.example.listto.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Queue;
import java.util.Vector;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="list_to";
    private static final String TABLE_NAME = "task";

    // column tables
    private static final String ID = "id";
    private static final String TOPIC = "topic";
    private static final String DESCRIPTION = "description";
    private static final String SCHEDULE = "schedule";
    private static final String COLOR = "color";
    private static final String STATUS = "status";
    private static Context c;

    private static DatabaseHandler instance;
    public static synchronized DatabaseHandler getInstance(Context context){
        c=context;
        if(instance==null){
            instance=new DatabaseHandler(context.getApplicationContext());
        }
        return instance;
    }
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE=
                "CREATE TABLE " +TABLE_NAME+" ( "
                  +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                  +TOPIC+" TEXT ,"
                  +DESCRIPTION+" TEXT ,"
                  +SCHEDULE+" DATETIME ,"
                  +COLOR+" INTEGER ,"
                  +STATUS+" INTEGER );";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Long addNewTask(TaskModel taskModel){
        SQLiteDatabase db=getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values= new ContentValues();
            values.put(TOPIC,taskModel.getTopic());
            values.put(DESCRIPTION,taskModel.getDescription());
            values.put(SCHEDULE,taskModel.getSimpleDateFormat());
            values.put(COLOR,taskModel.getColor());
            values.put(STATUS,taskModel.getStatus());
            long id=db.insertOrThrow(TABLE_NAME,null,values);
            db.setTransactionSuccessful();
            db.endTransaction();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            return -1L;
        }
    }
    public void deleteTask(TaskModel taskModel){
        SQLiteDatabase db=this.getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete(TABLE_NAME,ID+"=?",
                    new String[]{String.valueOf(taskModel.getId())});
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }
    public TaskModel getTaskById(Integer id){
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = "+id+" Limit 1";
        return getSingleTask(query);
    }
    public Vector<TaskModel> getTaskByDate(String date){
        String query="SELECT * FROM "+TABLE_NAME+" WHERE DATE("+SCHEDULE+") = '"+date+"' order by "+SCHEDULE;
        return getMultipleTask(query);
    }
    public Vector<TaskModel> getTaskByMonth(String date){
        String query="Select * from "+TABLE_NAME+ " WHERE strftime('%Y-%m',"+SCHEDULE+") = strftime('%Y-%m','"+date+"') order by "+SCHEDULE;
        return getMultipleTask(query);
    }
    public Vector<TaskModel> getNotification(){
        String query="Select * from "+TABLE_NAME+ " WHERE "+STATUS+" = 1 LIMIT 50";
        return getMultipleTask(query);
    }
    public boolean updateStatusActivity(String Id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues cv=new ContentValues();
            cv.put(STATUS,1);
            db.update(TABLE_NAME,cv,ID+" = ?",new String[]{String.valueOf(Id)});
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            db.endTransaction();
            return true;
        }
    }
    public Vector<EventDay> getAllEvent(){
        String query="SELECT schedule,color "
                +" FROM "+TABLE_NAME+" "
                +" where id in"
                +" (select id"
                +" from "+TABLE_NAME+""
                +" where Date(schedule)>= Date()"
                +" limit 50)"
                +" or id in"
                +" (select id"
                +" from "+TABLE_NAME+""
                +" where Date(schedule)< Date()"
                +" limit 50);";
        return getMultipleEvent(query);
    }
    public Vector<TaskModel> getAllTask(){
        String query="SELECT * from "+TABLE_NAME;
        return getMultipleTask(query);
    }
    private TaskModel getSingleTask(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query,null);
        TaskModel taskModel = new TaskModel();
        try {
            if (cursor!=null)
                cursor.moveToNext();
                taskModel=parseTask(cursor);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null&&!cursor.isClosed()){
                cursor.close();
            }
        }
        return taskModel;
    }
    private Vector<TaskModel> getMultipleTask(String query){
        Vector<TaskModel> tasks=new Vector<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query,null);
        try{
            if(cursor.moveToFirst()){
                do{
                    tasks.add(parseTask(cursor));
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null&&!cursor.isClosed()){
                cursor.close();
            }
        }
        return tasks;
    }
    private Vector<EventDay> getMultipleEvent(String query){
        Vector<EventDay> events=new Vector<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query,null);
        try{
            if(cursor.moveToFirst()){
                do{
                    events.add(parseEvent(cursor));
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null&&!cursor.isClosed()){
                cursor.close();
            }
        }
        return events;
    }
    private TaskModel parseTask(Cursor cursor){
        TaskModel taskModel=new TaskModel();
        taskModel.setId(Integer.parseInt(cursor.getString(0)));
        taskModel.setTopic(cursor.getString(1));
        taskModel.setDescription(cursor.getString(2));
        taskModel.setSchedule(taskModel.convertStringToDate(cursor.getString(3)));
        taskModel.setColor(Integer.parseInt(cursor.getString(4)));
        taskModel.setStatus(Integer.parseInt(cursor.getString(5)));
        return taskModel;
    }
    private EventDay parseEvent(Cursor cursor){
        Drawable icon=c.getDrawable(R.drawable.ic_baseline_circle_24).mutate();
        icon.setTint(Integer.parseInt(cursor.getString(1)));

        EventDay eventDay=
                new EventDay(
                        getStringToDateSql(cursor.getString(0)),
                        icon
                );
        return eventDay;
    }
}
