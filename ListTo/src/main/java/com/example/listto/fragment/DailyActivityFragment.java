package com.example.listto.fragment;

import static com.example.listto.util.CalenderUtil.calculateSuffix;
import static com.example.listto.util.CalenderUtil.getMonthFromCalender;
import static com.example.listto.util.CalenderUtil.getStringToDate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.listto.R;
import com.example.listto.adapter.DailyAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DailyActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyActivityFragment newInstance(String param1, String param2) {
        DailyActivityFragment fragment = new DailyActivityFragment();
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

        View view = inflater.inflate(R.layout.fragment_daily_activity,container,false);
        TextView title=view.findViewById(R.id.title_daily_activity_tv);
        Bundle bundle=this.getArguments();
        Calendar param=null;
        if(bundle!=null){
            param=getStringToDate(bundle.getString("date"));
        }
        Log.e("TAG",String.valueOf(param.getTime()) );
        title.setText(param.get(Calendar.DAY_OF_MONTH)+calculateSuffix(param)+" "+getMonthFromCalender(param));

        FragmentActivity activity=getActivity();
        LinearLayoutManager layoutManager=new LinearLayoutManager(activity);
        RecyclerView recyclerView=view.findViewById(R.id.daily_rv);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new DailyAdapter(getContext(),param,"day"));

        return view;
    }
}