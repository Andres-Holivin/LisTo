package com.example.listto.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;

import com.example.listto.R;


public class ActionBarCustom {
    public void getMainActionBar(ActionBar actionBar, Activity activity){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.hader_bar);
        actionBar.setElevation(1);
        View viewActionBar = actionBar.getCustomView();
    }
}
