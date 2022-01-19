package com.example.listto.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.listto.HomeActivity;
import com.example.listto.R;
import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.TaskModel;
import com.example.listto.util.FragmentRefresh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyFragment extends Fragment  {

    private Date currentDate=null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DailyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyFragment newInstance(String param1, String param2) {
        DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily,container,false);
        FragmentActivity activity=getActivity();

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setCalendarDayLayout(R.layout.calender_view_item);

        DatabaseHandler db=DatabaseHandler.getInstance(getContext());
        calendarView.setEvents(db.getAllEvent());

        currentDate=new Date();
        initFragment(currentDate);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                currentDate = eventDay.getCalendar().getTime();
                initFragment(currentDate);
            }
        });
        FragmentRefresh fragmentRefresh=new FragmentRefresh().getInstance();
        fragmentRefresh.setFragmentRefreshListener(new FragmentRefresh.FragmentRefreshListener(){
            @Override
            public void onRefresh() {
                DatabaseHandler db=DatabaseHandler.getInstance(getContext());
                calendarView.setEvents(db.getAllEvent());
                initFragment(currentDate);
            }
        });
        Log.d("Fragment", String.valueOf(fragmentRefresh.getFragmentRefreshListener()));
        return view;
    }
    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
    public void initFragment(Date date){
        Bundle bundle =new Bundle();
        bundle.putString("date",getFormattedDate(date));
        DailyActivityFragment fragment =new DailyActivityFragment();
        fragment.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_fl,fragment)
                .commit();
    }
}