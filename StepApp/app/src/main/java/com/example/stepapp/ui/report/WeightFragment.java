package com.example.stepapp.ui.report;

        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;

        import android.os.SystemClock;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.anychart.AnyChart;
        import com.anychart.AnyChartView;
        import com.anychart.chart.common.dataentry.DataEntry;
        import com.anychart.chart.common.dataentry.ValueDataEntry;
        import com.anychart.charts.Cartesian;
        import com.anychart.core.cartesian.series.Column;
        import com.anychart.core.cartesian.series.Line;
        import com.anychart.enums.Anchor;
        import com.anychart.enums.HoverMode;
        import com.anychart.enums.Position;
        import com.anychart.enums.TooltipPositionMode;
        import com.example.stepapp.LoginActivity;
        import com.example.stepapp.MainActivity;
        import com.example.stepapp.R;
        import com.example.stepapp.RegisterActivity;
        import com.example.stepapp.StepAppOpenHelper;
        import android.view.View;
        import android.widget.AdapterView;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;
        import java.util.TimeZone;
        import java.util.TreeMap;
        import java.util.regex.Pattern;


public class WeightFragment extends Fragment {

    AnyChartView anyChartView;
    public Map<String, Double> weightByDay = null;
    public String user="";

    public String day="";
    public String timestamp="";
    public String hour="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weight, container, false);
        user= MainActivity.logged_user;

        // Create column chart
        anyChartView = root.findViewById(R.id.weightLineChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar2));

        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

        //add Weight button
        Button addWeight = root.findViewById(R.id.toggleweight);

        addWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registering a new weight

                EditText weight   = (EditText) root.findViewById(R.id.new_weight);

                String s_weight= weight.getText().toString().trim();


                if (s_weight.equals("") || isNumeric(s_weight)==false){

                    Toast toast = Toast.makeText(v.getContext(), "Introduce a valid value", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{

                    Toast toast = Toast.makeText(v.getContext(), "Weight Registered", Toast.LENGTH_SHORT);
                    toast.show();


                    StepAppOpenHelper databaseOpenHelper = new StepAppOpenHelper(getContext());
                    SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

                    long timeInMillis = System.currentTimeMillis() ;

                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                    String date = jdf.format(timeInMillis);


                    // Get the date, the day and the hour
                    timestamp = date;
                    day = date.substring(0,10);
                    hour = date.substring(11,13);

                    ContentValues values = new ContentValues();
                    values.put(StepAppOpenHelper.KEY_USER, user);
                    values.put(StepAppOpenHelper.KEY_WEIGHT, s_weight);
                    values.put(StepAppOpenHelper.KEY_DAY, day);
                    values.put(StepAppOpenHelper.KEY_HOUR, hour);
                    values.put(StepAppOpenHelper.KEY_TIMESTAMP, timestamp);
                    database.insert(StepAppOpenHelper.TABLE_NAME_3, null, values);

                }
            }
        });


        return root;
    }


    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public Cartesian createColumnChart(){
        //Read data from SQLiteDatabase
        weightByDay = StepAppOpenHelper.loadWeightByDay(getContext(),user);

        // Create and get the cartesian coordinate system for line chart
        Cartesian cartesian = AnyChart.line();

        // Create data entries for x and y axis of the graph
        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String,Double> entry : weightByDay.entrySet()) //TODO :Change Query to daily weight
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        // Set the data for column chart and get the columns
        Line line = cartesian.line(data);

        //Modify the UI of the Column
        //Change the color of bar chart and its border
        //column.fill("#1EB980");
        //column.stroke("#1EB980");
        //column.fill("#A86C14");
        //column.stroke("#A86C14");

        line.color("#A86C14");
        line.markers(true);

        // Add tooltip to the bar charts and modify its properties
        line.tooltip()
                .titleFormat("Day: {%X}")
                .position(Position.RIGHT_TOP)
                .anchor(Anchor.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5)
                .format("{%Value}{groupsSeparator: } Kg");


        // Modify the UI of the Cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.background().fill("#00000000");
        cartesian.yScale().minimum(0);
        cartesian.yAxis(0).title("Kgs");
        cartesian.xAxis(0).title("Day");
        cartesian.animation(true);

        return cartesian;
    }
}

