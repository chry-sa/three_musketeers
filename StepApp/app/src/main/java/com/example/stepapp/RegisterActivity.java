package com.example.stepapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {



    private AppBarConfiguration mAppBarConfiguration;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 45;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 46;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 47;

    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView btn=findViewById(R.id.alreadyacount);


        Spinner spinner = (Spinner) findViewById(R.id.Sex);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

            }
        });
        // add register functionality
        Button registerButton = findViewById(R.id.register);
        getWriteExternalStorage();
        getReadExternalStorage();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: register new user

                EditText user_name   = (EditText)findViewById(R.id.login_username);
                EditText email_   = (EditText)findViewById(R.id.input_email);
                EditText passw   = (EditText)findViewById(R.id.login_password);
                EditText age   = (EditText)findViewById(R.id.age);

                String s_user_name= user_name.getText().toString().trim();
                String s_email= email_.getText().toString().trim();
                String s_passw= passw.getText().toString().trim();
                String s_age= age.getText().toString().trim();


                if (s_user_name.equals("") || s_email.equals("") || s_passw.equals("") || s_age.equals("")){

                    Toast toast = Toast.makeText(v.getContext(), "Your information is incomplete", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{

                    StepAppOpenHelper databaseOpenHelper = new StepAppOpenHelper(RegisterActivity.this);
                    SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(StepAppOpenHelper.KEY_USER, s_user_name);
                    values.put(StepAppOpenHelper.KEY_AGE, s_age);
                    values.put(StepAppOpenHelper.KEY_EMAIL, s_email);
                    values.put(StepAppOpenHelper.KEY_PASS, s_passw);
                    database.insert(StepAppOpenHelper.TABLE_NAME_2, null, values);

                    Toast.makeText(RegisterActivity.this,"User created successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }



                // step 1: find email
                // step 2: find password
                // step 3: get confirm password
                // step 4: check email is valid
                // step 5: check password is equal to confirm password
                //
                // step 6: use email and password to create an account
           }
        });
    }

    private void getReadExternalStorage() {

        if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }

    }

    private void getWriteExternalStorage() {
        if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
    //https://developer.android.com/guide/topics/ui/controls/spinner
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}