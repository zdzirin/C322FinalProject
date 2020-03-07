package com.zdzirinc323.finalprojectsandbox;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String USER_PREFS = "USER";

    SharedPreferences sharePref;
    DatabaseHelper dataBase;

    EditText nameText, emailText;
    Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        permissionRequest();

        sharePref = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        logIn();

        //Setting up fields
        dataBase = new DatabaseHelper(this);
        nameText = (EditText) findViewById(R.id.nameTextInput);
        emailText = (EditText) findViewById(R.id.emailTextInput);
        logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, msg;
                name = nameText.getText().toString();
                email = emailText.getText().toString();

                boolean added = dataBase.addUser(email, name);

                if (!added) {
                    msg = "Could not add user...";
                    Log.i("DATABASE", "Name: " + name + ". Email: " + email);
                } else {
                    msg = "Added user: " + name + "!";
                    SharedPreferences.Editor editor = sharePref.edit();
                    int userId = dataBase.getUserID(email, name);
                    Log.i("CHECK", "USER ID: " + userId);
                    editor.putInt("USER_ID", userId);
                    editor.commit();
                }
                Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
                logIn();
            }});



    }

    public void logIn() {
        int userID = sharePref.getInt("USER_ID", 0);
        Log.i("IdCheck", "Log in ID: " + userID);
        //If already logged in send to main screen, if not
        if (userID != 0) {
            Intent intent = new Intent(MainActivity.this, MainScreen.class);
            intent.putExtra("USER", userID);
            startActivity(intent);
        }
    }

    public void permissionRequest() {

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);

        }



    }




}
