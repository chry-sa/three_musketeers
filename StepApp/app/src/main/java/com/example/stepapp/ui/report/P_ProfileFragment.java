package com.example.stepapp.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.example.stepapp.MainActivity;
import com.example.stepapp.R;
import com.example.stepapp.StepAppOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class P_ProfileFragment extends Fragment {

    public String user="";
    public TextView userTextview;
    public TextView ageTextview;
    public TextView heightTextview;
    public TextView weightTextview;

    public ArrayList<String> ageHeight = new ArrayList<String> ();
    public double lastWeight =0;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_personal_profile, container, false);

        user = MainActivity.logged_user;
        userTextview = (TextView) root.findViewById(R.id.txt);
        userTextview.setText(String.valueOf(user));

        //Loading age and height for profile section

        ageHeight = StepAppOpenHelper.loadAgeHeight(getContext(),user);



        ageTextview = (TextView) root.findViewById(R.id.age);
        ageTextview.setText("Age: "+String.valueOf(ageHeight.get(0)));

        heightTextview = (TextView) root.findViewById(R.id.height);
        heightTextview.setText("Height: "+String.valueOf(ageHeight.get(1)));

        lastWeight = StepAppOpenHelper.loadSingleWeight(getContext(),user);
        weightTextview = (TextView) root.findViewById(R.id.weight);
        weightTextview.setText("Weight: "+Double.toString(lastWeight));

        //Loading age and height for profile section


        return root;
    }




}
