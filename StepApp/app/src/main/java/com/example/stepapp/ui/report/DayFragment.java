package com.example.stepapp.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepapp.MainActivity;
import com.example.stepapp.R;
import com.example.stepapp.StepAppOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DayFragment extends Fragment {

    AnyChartView anyChartView;
    public Map<String, Integer> stepsByDay = null;
    public String user="";
    public int allSteps=0;

    TextView numStepsTextView;
    TextView numCaloriesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        user= MainActivity.logged_user;
        View root = inflater.inflate(R.layout.fragment_day, container, false);

        //Updating number of steps and calories
        stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext(),user);

        for (Map.Entry<String,Integer> entry : stepsByDay.entrySet())
            allSteps=allSteps+ entry.getValue().intValue();




        numStepsTextView = root.findViewById(R.id.numStepsTextView);
        numStepsTextView.setText(String.valueOf(allSteps));

        // Add the number of calories in text view
        numCaloriesTextView = root.findViewById(R.id.numCalories);

        //Variable where burned calories will be stored. Through research we discovered that a human burns  between 0.04 and 0.06 calories per step

        double Calories = allSteps*0.05;
        numCaloriesTextView.setText(String.format("%.2f" ,Calories));


        // Create column chart
        anyChartView = root.findViewById(R.id.dayBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar2));

        Cartesian cartesian = createColumnChart(user);
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

        return root;
    }

    public Cartesian createColumnChart(String user){
        //Read data from SQLiteDatabase
        stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext(),user);

        // Create and get the cartesian coordinate system for column chart
        Cartesian cartesian = AnyChart.column();

        // Create data entries for x and y axis of the graph
        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : stepsByDay.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        // Set the data for column chart and get the columns
        Column column = cartesian.column(data);

        //Modify the UI of the Column
        //Change the color of bar chart and its border
        //column.fill("#1EB980");
        //column.stroke("#1EB980");
        column.fill("#A86C14");
        column.stroke("#A86C14");

        // Add tooltip to the bar charts and modify its properties
        column.tooltip()
                .titleFormat("Day: {%X}")
                .position(Position.RIGHT_TOP)
                .anchor(Anchor.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5)
                .format("{%Value}{groupsSeparator: } Steps");


        // Modify the UI of the Cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.background().fill("#00000000");
        cartesian.yScale().minimum(0);
        cartesian.yAxis(0).title("Number of steps");
        cartesian.xAxis(0).title("Day");
        cartesian.animation(true);

        return cartesian;
    }
}