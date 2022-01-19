package com.example.listto.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskModel implements Serializable {
    private Integer id;
    private String topic;
    private String description;
    private Date schedule;
    private Integer color;
    private Integer status;
    public TaskModel(){}
    public TaskModel( String topic, String description, Date schedule, Integer color, Integer status) {
        this.topic = topic;
        this.description = description;
        this.schedule = schedule;
        this.color = color;
        this.status = status;
    }
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getTopic() {return topic;}
    public void setTopic(String topic) {this.topic = topic;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public Date getSchedule() {return schedule;}
    public void setSchedule(Date schedule) {this.schedule = schedule;}
    public Integer getColor() {return color;}
    public void setColor(Integer color) {this.color = color;}
    public Integer getStatus() {return status;}
    public void setStatus(Integer status) {this.status = status;}
    public String getDateFormat(){
        SimpleDateFormat format=new SimpleDateFormat("EEE,d MMM/h:mm a");
        return format.format(this.schedule);
    }
    public String getSimpleDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.schedule);
    }
    public Date convertStringToDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Calendar getCalendarFormat(){
        Calendar cal=Calendar.getInstance();
        cal.setTime(this.schedule);
        return cal;
    }

}
