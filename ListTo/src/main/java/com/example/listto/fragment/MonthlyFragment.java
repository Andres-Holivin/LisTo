package com.example.listto.fragment;

import static com.example.listto.util.CalenderUtil.getDateFormat;
import static com.example.listto.util.CalenderUtil.getDateTitle;
import static com.example.listto.util.CalenderUtil.getStringToDate;
import static com.example.listto.util.CalenderUtil.getStringToDateSql;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.ULocale;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listto.HomeActivity;
import com.example.listto.R;
import com.example.listto.adapter.DailyAdapter;
import com.example.listto.model.DatabaseHandler;
import com.example.listto.util.FragmentRefresh;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthlyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String month="0";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthlyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyFragment newInstance(String param1, String param2) {
        MonthlyFragment fragment = new MonthlyFragment();
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
    private TextView dateTitle;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_monthly, container, false);

        Calendar calendar=Calendar.getInstance();
        month=calendar.get(Calendar.YEAR)+"-"
                +(calendar.get(Calendar.MONTH)+1)+"-"
                +calendar.get(Calendar.DAY_OF_MONTH)+" 11:11";

        setRecyclerView();

        ImageButton calender=view.findViewById(R.id.calender_ib);
        dateTitle=view.findViewById(R.id.month_tv);
        Log.d("date", month);
        dateTitle.setText(getDateFormat(getStringToDateSql(month)));
        calender.setOnClickListener(v->{
            showDateDialog();
        });

        new FragmentRefresh().getInstance().setFragmentRefreshListener(new FragmentRefresh.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                dateTitle.setText(getDateFormat(getStringToDateSql(month)));
                setRecyclerView();
            }
        });

        return view;
    }
    public void showDateDialog(){
        Calendar calendar=Calendar.getInstance();
        MonthPickerDialog.Builder builder;
        builder = new MonthPickerDialog.Builder(getContext(),new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                month=selectedYear+"-"+(selectedMonth+1)+"-1"+" 11:11";
                dateTitle.setText(getDateFormat(getStringToDateSql(month)));
                setRecyclerView();
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        builder.setMinYear(calendar.get(Calendar.YEAR)-5).setMaxYear(calendar.get(Calendar.YEAR)+5).setTitle("Select Month And Year").build().show();
    }
    public void setRecyclerView(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        RecyclerView recyclerView=view.findViewById(R.id.activity_rv);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new DailyAdapter(getContext(),getStringToDateSql(month),"month"));
    }

}