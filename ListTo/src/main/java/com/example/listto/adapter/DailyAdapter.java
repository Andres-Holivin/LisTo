package com.example.listto.adapter;

import static com.example.listto.util.CalenderUtil.calculateSuffix;
import static com.example.listto.util.CalenderUtil.getCalenderDateSqlFormat;
import static com.example.listto.util.CalenderUtil.getMonthFromCalender;
import static com.example.listto.util.CalenderUtil.getStringToDateSql;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listto.HomeActivity;
import com.example.listto.R;
import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.TaskModel;
import com.example.listto.util.CreateSchedule;
import com.example.listto.util.FragmentRefresh;

import java.util.Calendar;
import java.util.Vector;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {

    private Vector<TaskModel> data = new Vector<>();
    private Context context;
    private DatabaseHandler db;
    private PopupWindow popupWindow;
    private TextView topic,desc,date,time,status;
    private ImageView statusIcon,colorIcon;
    private Button delete;
    private DailyAdapter adapter;

    public DailyAdapter(Context context, Calendar date, String type) {
        db = DatabaseHandler.getInstance(context);
        if(type.equals("day")){
            data=db.getTaskByDate(String.valueOf(new java.sql.Date(date.getTimeInMillis())));
        }else if(type.equals("month")){
            data=db.getTaskByMonth(String.valueOf(new java.sql.Date(date.getTimeInMillis())));
        }
        this.context=context;
        this.adapter=this;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.day_item,parent,false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        holder.titleTv.setText(data.get(position).getTopic());
        holder.dateTv.setText(data.get(position).getDateFormat());
        if(data.get(position).getStatus()==1){
            holder.actionIv.setImageResource(R.drawable.ic_baseline_check_circle_24);
//            holder.actionIv.setColorFilter(holder.actionIv.getResources().getColor(R.color.correct_green));
        }else if(data.get(position).getStatus()==0){
            holder.actionIv.setImageResource(R.drawable.ic_baseline_pending_24);
//            holder.actionIv.setColorFilter(holder.actionIv.getResources().getColor(R.color.pending_yellow));
        }
        holder.deleteIcon.setOnClickListener(view -> {
            Toast.makeText(context, "Activity Delete", Toast.LENGTH_SHORT).show();
            CreateSchedule.getInstance(context).cancelNotification(String.valueOf(data.get(position).getId()));
            db.deleteTask(data.get(position));
            if(new FragmentRefresh().getInstance().getFragmentRefreshListener()!=null){
                new FragmentRefresh().getInstance().getFragmentRefreshListener().onRefresh();
            }
        });
        holder.detailView.setOnClickListener(view -> {
            View viewPopup=LayoutInflater.from(context).inflate(R.layout.event_popup,null);
            popupWindow= new PopupWindow(context);
            initPopUp(viewPopup);
            setText(viewPopup,data.get(position));
            delete.setOnClickListener(v -> {
                Toast.makeText(context, "Activity Delete", Toast.LENGTH_SHORT).show();
                CreateSchedule.getInstance(context).cancelNotification(String.valueOf(data.get(position).getId()));
                db.deleteTask(data.get(position));
                if(new FragmentRefresh().getInstance().getFragmentRefreshListener()!=null){
                    new FragmentRefresh().getInstance().getFragmentRefreshListener().onRefresh();
                }
                popupWindow.dismiss();
            });
            ImageButton close=viewPopup.findViewById(R.id.exit_ib);
            close.setOnClickListener(view1 -> {
                popupWindow.dismiss();
            });
        });
    }

    @Override
    public int getItemCount() {
        return (data!=null)?data.size():0;
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv,dateTv;
        private ImageView actionIv;
        private Button detailView;
        private ImageButton deleteIcon;
        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv=itemView.findViewById(R.id.title_tv);
            dateTv=itemView.findViewById(R.id.date_tv);
            actionIv=itemView.findViewById(R.id.action_image);
            detailView=itemView.findViewById(R.id.view_detail_btn);
            deleteIcon=itemView.findViewById(R.id.delete_icon_btn);
        }
    }
    private void initPopUp(View viewPopup){
        popupWindow.setContentView(viewPopup);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(viewPopup.getResources().getColor(R.color.black_30)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(viewPopup, Gravity.CENTER, 1, 1);
    }
    private void setText(View viewPopup,TaskModel task){
        topic=viewPopup.findViewById(R.id.title_tv);
        desc=viewPopup.findViewById(R.id.description_tv);
        date=viewPopup.findViewById(R.id.date_tv);
        time=viewPopup.findViewById(R.id.time_tv);
        status=viewPopup.findViewById(R.id.status_tv);
        statusIcon=viewPopup.findViewById(R.id.status_icon_iv);
        delete=viewPopup.findViewById(R.id.btn_delete);
        colorIcon=viewPopup.findViewById(R.id.icon_color_iv);

        Drawable icon=context.getDrawable(R.drawable.ic_baseline_circle_24).mutate();
        icon.setTint(task.getColor());
        colorIcon.setImageDrawable(icon);

        topic.setText(task.getTopic());
        desc.setText(task.getDescription());
        Calendar cal = getStringToDateSql(task.getSimpleDateFormat());
        date.setText(cal.get(Calendar.DAY_OF_MONTH)+calculateSuffix(cal)+" "+getMonthFromCalender(cal));
        time.setText(cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE));
        if(task.getStatus()==1){
            status.setText("Complete");
            statusIcon.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }else if(task.getStatus()==0){
            status.setText("Pending");
            statusIcon.setImageResource(R.drawable.ic_baseline_pending_24);
        }
    }
}
