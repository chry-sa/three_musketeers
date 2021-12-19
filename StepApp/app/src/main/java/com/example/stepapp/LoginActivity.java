package com.example.stepapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;


public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 45;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 46;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 47;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public String user="";

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

        user= StepAppOpenHelper.loadMainUser(getApplicationContext());
        Log.d("Logged", "user: " + String.valueOf(user));


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

                EditText user_name   = (EditText)findViewById(R.id.new_goal);
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
                        i.putExtra("userLogin",value);
                        startActivity(i);
                    }
                }

            }
        });


        //https://developer.android.com/training/sign-in/biometric-auth
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                //---------------
                //Authentication Test-----Test Login
                //Intent i = new Intent(LoginActivity.this, MainActivity.class);
                //i.putExtra("userLogin","guest");
                //startActivity(i);

                //---------------
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);



                String user ="" ;
                user= StepAppOpenHelper.loadMainUser(getApplicationContext());

                if (user ==null||user.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "You need to register as the principal user!", Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(getApplicationContext(),
                            "Authentication succeeded!", Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("userLogin",user);
                    startActivity(i);
                }


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for HappyFeet!")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        Button biometricLoginButton = findViewById(R.id.biobutton);//TODO
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
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