package com.example.listto.util;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.TaskModel;

import java.util.Calendar;
import java.util.Vector;

public class CreateSchedule {
    private static CreateSchedule instance=null;
    private static Context context;
    public static CreateSchedule getInstance(Context c){
        context=c;
        if(instance==null){
            instance=new CreateSchedule();
        }
        return instance;
    }
    public CreateSchedule() {

    }
    public void setSchedule(String id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        TaskModel taskModel=DatabaseHandler.getInstance(context).getTaskById(Integer.valueOf(id));
        Intent intent=new Intent(context, NotificationUtil.class).putExtra("Id",String.valueOf(taskModel.getId()));
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context,taskModel.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("currentTime", String.valueOf(Calendar.getInstance().getTime()));
        Log.e("currentTime", String.valueOf(taskModel.getCalendarFormat().getTimeInMillis()-100000L));
        alarmManager.set(AlarmManager.RTC_WAKEUP,(taskModel.getCalendarFormat().getTimeInMillis()-100000L),pendingIntent);
    }
    public void cancelNotification(String id) {
        TaskModel taskModel=DatabaseHandler.getInstance(context).getTaskById(Integer.valueOf(id));
        Intent intent=new Intent(context, NotificationUtil.class).putExtra("Id",String.valueOf(taskModel.getId()));
        PendingIntent pending = PendingIntent.getBroadcast(context, taskModel.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
        Log.d("Cancel Notification", "Success Cancel Notification");
    }
}
