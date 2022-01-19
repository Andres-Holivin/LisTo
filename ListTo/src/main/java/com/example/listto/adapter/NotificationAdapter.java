package com.example.listto.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listto.R;
import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.TaskModel;

import java.util.Date;
import java.util.Vector;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Vector<TaskModel> data = new Vector<>();
    private Context context;
    public NotificationAdapter(Context context) {
        DatabaseHandler db= new DatabaseHandler(context);
        TaskModel taskModel=new TaskModel();
        data=db.getNotification();
        this.context=context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.notification_item,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.titleTv.setText(data.get(position).getTopic());
        holder.dateTv.setText(data.get(position).getDateFormat());
        holder.descTv.setText(data.get(position).getDescription());
        if(data.get(position).getStatus()==1){
            holder.actionIv.setImageResource(R.drawable.ic_baseline_check_circle_24);
//            holder.actionIv.setColorFilter(holder.actionIv.getResources().getColor(R.color.correct_green));
        }else if(data.get(position).getStatus()==0){
            holder.actionIv.setImageResource(R.drawable.ic_baseline_pending_24);
//            holder.actionIv.setColorFilter(holder.actionIv.getResources().getColor(R.color.pending_yellow));
        }
    }

    @Override
    public int getItemCount() {
        return (data!=null)?data.size():0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv,dateTv,descTv;
        private ImageView actionIv;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv=itemView.findViewById(R.id.title_tv);
            dateTv=itemView.findViewById(R.id.date_tv);
            actionIv=itemView.findViewById(R.id.action_image);
            descTv=itemView.findViewById(R.id.description_tv);
        }
    }
}
