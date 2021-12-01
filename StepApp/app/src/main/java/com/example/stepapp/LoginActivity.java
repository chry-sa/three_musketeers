package com.example.stepapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 45;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 46;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 47;

    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        TextView btn=findViewById(R.id.btnToSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });
        // add login functionality
        Button loginButton = findViewById(R.id.login_btn);


        getWriteExternalStorage();
        getReadExternalStorage();

        if (runningQOrLater) {
            getActivity();
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: login new user
                Integer user_c=0;

                EditText user_name   = (EditText)findViewById(R.id.login_username);
                EditText passw   = (EditText)findViewById(R.id.login_password);

                if ( new String("").equals(user_name.getText().toString()) || new String("").equals(passw.getText().toString()) ){

                    Toast toast=Toast.makeText(v.getContext(),"No User or Password",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {

                    user_c = StepAppOpenHelper.user_login(getApplicationContext(),
                            user_name.getText().toString(),
                            passw.getText().toString());

                    if (user_c == 0) {

                        Toast toast = Toast.makeText(v.getContext(), "Invalid User or Password", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {

                        String value= user_name.getText().toString();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("logged_user",value);
                        startActivity(i);
                    }
                }

            }
        });

    }

    private void getActivity() {

        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_ACTIVITY_RECOGNITION_PERMISSION);
        }
    }

    private void getReadExternalStorage() {

        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }

    }

    private void getWriteExternalStorage() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }


}