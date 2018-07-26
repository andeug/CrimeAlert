package com.example.andrewsimiyu.crimealert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    Button LogOut;
    ImageButton Witness, Vehicle, Suspect, Evidence, Missing,Emergency;

    TextView EmailShow;
    String EmailHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Witness= findViewById(R.id.ibtnWitness);
        Vehicle= findViewById(R.id.ibtnVehicle);
       Suspect= findViewById(R.id.ibtnSuspect);
       Evidence= findViewById(R.id.ibtnUpload);
       Missing= findViewById(R.id.ibtnMissing);
       Emergency=findViewById(R.id.ibtnEmergency);

        LogOut = findViewById(R.id.button);
        EmailShow = findViewById(R.id.EmailShow);

        Missing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this, MissingPerson.class);
                startActivity(intent);
            }
        });

        Witness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Witness.class);

                startActivity(intent);

    }
        });
        Vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Vehicle.class);

                startActivity(intent);

            }
        });

        Suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Suspect.class);

                startActivity(intent);

            }
        });




        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(UserLoginActivity.UserEmail);
        EmailShow.setText(EmailHolder);

        Evidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, UploadFile.class);

                startActivity(intent);
            }
        });

       LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                Intent intent = new Intent(DashboardActivity.this, UserLoginActivity.class);

                startActivity(intent);

                Toast.makeText(DashboardActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();


            }
        });
    }
}