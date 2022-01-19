package com.example.listto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.listto.fragment.CalenderFragment;
import com.example.listto.fragment.NotificationFragment;
import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.TaskModel;
import com.example.listto.util.ActionBarCustom;
import com.example.listto.util.CreateSchedule;
import com.example.listto.util.FragmentRefresh;
import com.example.listto.util.NotificationUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {
    private int year=-1,month=-1,day=-1,hour=-1,minute=-1;
    private String topic="",desc="";
    private Date datetime=null;
    private int color=0;
    private Calendar calendar=Calendar.getInstance();
    private EditText dateEt,timeEt;
    private ImageButton colorPicker;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DatabaseHandler db=DatabaseHandler.getInstance(this);
        Vector<TaskModel> tasks = db.getAllTask();
        for (TaskModel task : tasks) {
            String log = task.getId()+" "+task.getTopic()+" "+task.getSchedule();
            Log.d("Name: ", log);
        }

        ActionBar actionBar = getSupportActionBar();
        new ActionBarCustom().getMainActionBar(actionBar,this);

        FloatingActionButton addButton = findViewById(R.id.fab);

        addButton.setOnClickListener(v -> {
            View viewPopup= LayoutInflater.from(v.getContext()).inflate(R.layout.add_new_popup,null);
            PopupWindow popupWindow= new PopupWindow(v.getContext());
            popupWindow.setContentView(viewPopup);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.popup_window_animation);
            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_30)));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(viewPopup, Gravity.CENTER, 0, 0);

            ImageButton close=viewPopup.findViewById(R.id.exit_ib);
            close.setOnClickListener(view1 ->popupWindow.dismiss());

            dateEt= viewPopup.findViewById(R.id.date_et);
            dateEt.setOnClickListener(view -> showDatePiker(view));

            timeEt=viewPopup.findViewById(R.id.time_et);
            timeEt.setOnClickListener(view -> showTimePiker(view));

            color= R.color.Light_Green;
            colorPicker=viewPopup.findViewById(R.id.color_picker_ib);
            Drawable icon=getDrawable(R.drawable.ic_baseline_circle_24).mutate();
            icon.setTint(Color.BLACK);
            colorPicker.setImageDrawable(icon);
            colorPicker.setOnClickListener(view -> showColorPiker(v));

            EditText topicEt=viewPopup.findViewById(R.id.topic_et);
            EditText descEt=viewPopup.findViewById(R.id.description_et);
            Button addBtn= viewPopup.findViewById(R.id.add_btn);
            addBtn.setOnClickListener(view -> {
                datetime=setDateTime();
                topic= String.valueOf(topicEt.getText());
                desc= String.valueOf(descEt.getText());
                if(topic.equals("")||desc.equals("")||day==-1||hour==-1){
                    Toast.makeText(this, "Value can't empty", Toast.LENGTH_SHORT).show();
                }else{
                    boolean cek=saveData(this);
                    if(!cek){
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }else{
                        if(new FragmentRefresh().getInstance().getFragmentRefreshListener()!=null){
                            new FragmentRefresh().getInstance().getFragmentRefreshListener().onRefresh();
                        }
                        Toast.makeText(this, "Success Save", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                }
            });
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.list_item);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        currentFragment=new CalenderFragment();
        loadFragment(currentFragment);
    }
    private boolean saveData(Context context){
        DatabaseHandler db=DatabaseHandler.getInstance(context);
        TaskModel task=new TaskModel(topic,desc,datetime,color,0);
        long id= db.addNewTask(task);
        CreateSchedule schedule= CreateSchedule.getInstance(HomeActivity.this);
        schedule.setSchedule(String.valueOf(id));
        if(id==-1)return false;
        else return true;
    }
    private Date setDateTime(){
        String date=year+"-"+(month+1)+"-"+day+" "+hour+":"+minute;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.list_item:
                    currentFragment=new CalenderFragment();
                    loadFragment(currentFragment);
                    break;
                case R.id.notification_item:
                    currentFragment=new NotificationFragment();
                    loadFragment(currentFragment);
                    break;
            }
            return true;
        }
    };
    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_home_fl,fragment);
        fragmentTransaction.commit();
    }
    private void showDatePiker(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int y,int monthOfYear, int dayOfMonth) {
                        day=dayOfMonth;
                        month=monthOfYear;
                        year=y;
                        dateEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + y);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showTimePiker(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int m) {
                        hour=hourOfDay;
                        minute=m;
                        timeEt.setText(hourOfDay + ":" + m);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }
    private void showColorPiker(View view){
        new ColorPickerDialog.Builder(view.getContext())
                .setTitle("Select Color")
                .setPositiveButton("Select",
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                Drawable icon=getDrawable(R.drawable.ic_baseline_circle_24).mutate();
                                icon.setTint(envelope.getColor());
                                colorPicker.setImageDrawable(icon);
                                color=envelope.getColor();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .show();
    }
}