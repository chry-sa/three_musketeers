package com.example.stepapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StepAppOpenHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "stepapp";

    public static final String TABLE_NAME = "num_steps";
    public static final String KEY_ID = "id";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_DAY = "day";

    public static final String TABLE_NAME_2= "Users";
    public static final String KEY_USER= "user";
    public static final String KEY_MAIN_USER= "main_user";
    public static final String KEY_AGE= "age";
    public static final String KEY_HEIGHT= "height";
    public static final String KEY_SEX= "sex";
    public static final String KEY_EMAIL= "email";
    public static final String KEY_PASS= "password";
    public static final String KEY_GOAL= "goal";


    public static final String TABLE_NAME_3 = "wei_table";
    public static final String KEY_WEIGHT= "weight";







    // Default SQL for creating a table in a database
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +KEY_USER +" TEXT, "+ KEY_DAY + " TEXT, " + KEY_HOUR + " TEXT, "
            + KEY_TIMESTAMP + " TEXT);";


    public static final String CREATE_USER_TABLE_SQL = "CREATE TABLE " + TABLE_NAME_2 + " (" +
            KEY_USER + " TEXT PRIMARY KEY," +KEY_MAIN_USER +" INTEGER,"+KEY_SEX+" TEXT, " +KEY_HEIGHT+" REAL, " +KEY_AGE  +" INTEGER, "  + KEY_EMAIL + " TEXT, " + KEY_PASS + " TEXT, "+KEY_GOAL  +" INTEGER );";


    public static final String CREATE_WEIGHT_SQL = "CREATE TABLE " + TABLE_NAME_3 + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +KEY_USER +" TEXT, "+ KEY_WEIGHT +" REAL, "+ KEY_DAY + " TEXT, " + KEY_HOUR + " TEXT, "
            + KEY_TIMESTAMP + " TEXT);";

    // The constructor
    public StepAppOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_USER_TABLE_SQL);
        db.execSQL(CREATE_WEIGHT_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // nothing to do here
    }

    /**
     * Utility function to load all records in the database
     *
     * @param context: application context
     */
    public static void loadRecords(Context context){
        List<String> dates = new LinkedList<String>();
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String [] columns = new String [] {StepAppOpenHelper.KEY_TIMESTAMP};
        Cursor cursor = database.query(StepAppOpenHelper.TABLE_NAME, columns, null, null, StepAppOpenHelper.KEY_TIMESTAMP,
                null, null );

        // iterate over returned elements
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            dates.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        Log.d("STORED TIMESTAMPS: ", String.valueOf(dates));
    }

    /**
     * Utility function to delete all records from the data base
     *
     * @param context: application context
     */
    public static void deleteRecords(Context context){
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int numberDeletedRecords =0;

        numberDeletedRecords = database.delete(StepAppOpenHelper.TABLE_NAME, null, null);
        database.close();

        // display the number of deleted records with a Toast message
        Toast.makeText(context,"Deleted " + String.valueOf(numberDeletedRecords) + " steps",Toast.LENGTH_LONG).show();
    }

    /**
     * Utility function to load records from a single day
     *
     * @param context: application context
     * @param date: today's date
     * @return numSteps: an integer value with the number of records in the database
     */
    //
    public static Integer loadSingleRecord(Context context, String date,String user){
        List<String> steps = new LinkedList<String>();
        // Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String where = StepAppOpenHelper.KEY_DAY + " = ? and "+StepAppOpenHelper.KEY_USER + "= ?";
        String [] whereArgs = { date , user };

        Cursor cursor = database.query(StepAppOpenHelper.TABLE_NAME, null, where, whereArgs, null,
                null, null );

        // iterate over returned elements
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            steps.add(cursor.getString(0));
            cursor.moveToNext();
        }
        database.close();
        cursor.close();

        Integer numSteps = steps.size();
        Log.d("STORED STEPS TODAY: ", String.valueOf(numSteps));
        return numSteps;
    }



    /**
     * Utility function to get the number of steps by hour for current date
     *
     * @param context: application context
     * @param date: today's date
     * @return map: map with key-value pairs hour->number of steps
     */
    //
    public static Map<Integer, Integer> loadStepsByHour(Context context, String date,String user ){
        // 1. Define a map to store the hour and number of steps as key-value pairs
        Map<Integer, Integer>  map = new HashMap<> ();

        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // 3. Define the query to get the data
        Cursor cursor = database.rawQuery("SELECT hour, COUNT(*)  FROM num_steps " +
                "WHERE day = ? and user = ?  GROUP BY hour ORDER BY  hour ASC ", new String [] {date,user});

        // 4. Iterate over returned elements on the cursor
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            Integer tmpKey = Integer.parseInt(cursor.getString(0));
            Integer tmpValue = Integer.parseInt(cursor.getString(1));

            //2. Put the data from the database into the map
            map.put(tmpKey, tmpValue);


            cursor.moveToNext();
        }

        // 5. Close the cursor and database
        cursor.close();
        database.close();

        // 6. Return the map with hours and number of steps
        return map;
    }

    public static Integer user_login(Context context, String usr, String pass){
        // 1. Define a map to store the hour and number of steps as key-value pairs
        ArrayList<String> quser = new ArrayList<>();

        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // 3. Define the query to get the data
        Cursor cursor = database.rawQuery("SELECT user  FROM Users " +
                "WHERE user = ? and password = ? ", new String[]{usr,pass});

        // 4. Iterate over returned elements on the cursor
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            String tmpKey = cursor.getString(0);

            //2. Put the data from the database into the map
            quser.add(tmpKey);


            cursor.moveToNext();
        }

        // 5. Close the cursor and database
        cursor.close();
        database.close();

        if (quser.size()>0){
            return 1;
        }
        else{
            return 0;
        }

    }

    /**
     * Utility function to get the number of steps by day
     *
     * @param context: application context
     * @return map: map with key-value pairs hour->number of steps
     */
    //
    public static Map<String, Integer> loadStepsByDay(Context context,String user){
        // 1. Define a map to store the hour and number of steps as key-value pairs
        Map<String, Integer>  map = new TreeMap<>();

        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // 3. Define the query to get the data
        Cursor cursor = database.rawQuery("SELECT day, COUNT(*)  FROM num_steps " +"WHERE user = ?" +
                "GROUP BY day ORDER BY day ASC ", new String [] {user});

        // 4. Iterate over returned elements on the cursor
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            String tmpKey = cursor.getString(0);
            Integer tmpValue = Integer.parseInt(cursor.getString(1));

            // Put the data from the database into the map
            map.put(tmpKey, tmpValue);
            cursor.moveToNext();
        }

        // 5. Close the cursor and database
        cursor.close();
        database.close();

        // 6. Return the map with hours and number of steps
        return map;
    }

    public static ArrayList<String>  loadAgeHeight(Context context, String user){

        ArrayList<String>   list_ageheight = new ArrayList<String>() ;



        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // 3. Define the query to get the data
        Cursor cursor = database.rawQuery("SELECT age,height FROM Users " +"WHERE user = ?", new String [] {user});

        // 4. Iterate over returned elements on the cursor


        cursor.moveToFirst();
        Log.d("Penguin Profile", "Age: " + String.valueOf(cursor.getString(0)));
        Log.d("Penguin Profile", "Height: " + String.valueOf(cursor.getString(1)));


        list_ageheight.add(String.valueOf(cursor.getString(0)));
        list_ageheight.add(String.valueOf(cursor.getString(1)));

        // 5. Close the cursor and database
        cursor.close();
        database.close();

        // 6. Return the map with hours and number of steps
        return list_ageheight;
    }

    public static String  loadMainUser(Context context){

        String mainUser = "" ;



        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // 3. Define the query to get the data
        Cursor cursor = database.rawQuery("SELECT user FROM Users WHERE main_user = 1",new String[] {});

        // 4. Iterate over returned elements on the cursor

        if ((cursor != null) && (cursor.getCount() > 0)) {

            cursor.moveToFirst();
            Log.d("Main_USER", "user: " + String.valueOf(cursor.getString(0)));

            mainUser  =  String.valueOf(cursor.getString(0));
        }
        else{

            Log.d("Main_USER", "user: No main user");


        }
        // 5. Close the cursor and database
        cursor.close();
        database.close();


        // 6. Return the map with hours and number of steps
        return mainUser;
    }

    //Load weight by a single record
    public static double loadSingleWeight(Context context,String user){
        String last_weight = "";
        double lw=0.0;
        // Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String where = StepAppOpenHelper.KEY_USER + "= ?";
        String [] whereArgs = {  user };

        Cursor cursor = database.query(StepAppOpenHelper.TABLE_NAME_3, null, where, whereArgs, null,
                null, StepAppOpenHelper.KEY_DAY +" DESC, "+StepAppOpenHelper.KEY_HOUR+" DESC ,"+StepAppOpenHelper.KEY_TIMESTAMP );

        if ((cursor != null) && (cursor.getCount() > 0)) {

            cursor.moveToFirst();


            last_weight= cursor.getString(2);
            Log.d("weight: Lastweight", String.valueOf(last_weight));


            lw = Double.parseDouble(last_weight);
        }
        else{

            Log.d("Main_USER", "user: No main user");
            lw=0.0;


        }

        // iterate over returned elements





        database.close();
        cursor.close();



        //if (last_weight.equals("")  ){

        //    lw=0.0;

        //}
        //else
        //{
        //    lw = Double.parseDouble(last_weight);

        //}



        Log.d("weight: ", String.valueOf(lw));
        return lw;
    }
    //Loading average weight by day
    public static Map<String, Double> loadWeightByDay(Context context,String user){
        // 1. Define a map to store the hour and number of steps as key-value pairs
        Map<String, Double>  map = new TreeMap<>();

        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // 3. Define the query to get the data
        Cursor cursor = database.rawQuery("SELECT day, avg(weight)  FROM wei_table " +"WHERE user = ?" +
                "GROUP BY day ORDER BY day ASC ", new String [] {user});

        // 4. Iterate over returned elements on the cursor
        cursor.moveToFirst();
        for (int index=0; index < cursor.getCount(); index++){
            String tmpKey = cursor.getString(0);
            Double tmpValue = Double.parseDouble(cursor.getString(1));

            // Put the data from the database into the map
            map.put(tmpKey, tmpValue);
            cursor.moveToNext();
        }

        // 5. Close the cursor and database
        cursor.close();
        database.close();

        // 6. Return the map with hours and number of steps
        return map;
    }

    public static Integer loadGoal(Context context,String user){
        String goal = "";
        // Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String where = StepAppOpenHelper.KEY_USER + "= ?";
        String [] whereArgs = {  user };
        String [] columns = {  StepAppOpenHelper.KEY_GOAL };

        Cursor cursor = database.query(StepAppOpenHelper.TABLE_NAME_2, columns , where, whereArgs, null,
                null, StepAppOpenHelper.KEY_USER );

        // iterate over returned elements
        cursor.moveToFirst();
        goal= cursor.getString(0);

        database.close();
        cursor.close();


        Integer gl = Integer.parseInt(goal);
        Log.d("goal: ", String.valueOf(gl));
        return gl;
    }


    public static void updateGoal(Context context,String user,Integer newGoal){
        // 1. Define a map to store the hour and number of steps as key-value pairs

        // 2. Get the readable database
        StepAppOpenHelper databaseHelper = new StepAppOpenHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String where = StepAppOpenHelper.KEY_USER + "= ?";

        ContentValues cv = new ContentValues();
        cv.put("goal",String.valueOf(newGoal)); //These Fields should be your String values of actual column names

        // Update goal
        database.update(StepAppOpenHelper.TABLE_NAME_2,cv, where, new String [] {user});

        //  and database
        database.close();

    }

}









