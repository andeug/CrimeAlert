package com.example.andrewsimiyu.crimealert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Emergency extends AppCompatActivity {
String Emergency;
LocationManager locationManager;
String longitude;
String latitude;
Button Submit;
String finalResult;
String HttpURL;
ProgressDialog progressDialog;
HashMap<String, String> hashMap = new HashMap<>();
HttpParse httpParse = new HttpParse();
    public Emergency() {
        HttpURL = "https://andrewsimiyu.000webhostapp.com/CrimeAlert/Emergency.php";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);
        Submit = findViewById(R.id.btnDone);
        //Adding Click Listener on button.
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking whether EditText is Empty or Not

                    getLocation();

            }
        });

    final Spinner spinner = findViewById(R.id.spinner);


    // Initializing a String Array
    String[] emergency = new String[]{
            "Panic",
            "Terror Attack",
            "Fire",
            "Traffic",
            "Domestic"
    };

    final List<String> crimeList = new ArrayList<>(Arrays.asList(emergency));

    // Initializing an ArrayAdapter
    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
            this, R.layout.spinner_item, crimeList){
        @Override
        public boolean isEnabled(int position){
            return position != 0;
        }
        @Override
        public View getDropDownView(int position, View convertView,
                                    @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if(position == 0){
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            }
            else {
                tv.setTextColor(Color.RED);
            }
            return view;
        }
    };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(position > 0){
                // Notify the selected item text
                Emergency = (String) parent.getItemAtPosition(position);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });
}
public void getLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude() + "";
                    latitude = location.getLatitude() + "";

                    FormSubmitFunction(Emergency,longitude, latitude);
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    longitude = location.getLongitude() + "";

                    latitude = location.getLatitude() + "";

                    FormSubmitFunction(Emergency,longitude, latitude);
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Intent intent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });

        }


    }

    public void FormSubmitFunction(final String Emergency, final String longitude,
                                   final String latitude){

        @SuppressLint("StaticFieldLeak")
        class FormSubmitFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Toast.makeText(Suspect.this,finalResult,Toast.LENGTH_LONG).show();
                progressDialog = ProgressDialog.show(Emergency.this,"Loading Data",null,true,true);

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Log.e("**********", "onPostExecute: "+finalResult);
                Toast.makeText(Emergency.this, httpResponseMsg, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Emergency.this, Navigation.class);

                intent.putExtra("UserEmail","email");

                startActivity(intent);

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("Emergency", params[8]);

                hashMap.put("longitude" , params[6]);

                hashMap.put("latitude", params[7]);



                //hashMap.put("mCameraImage"), params[8];



                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;

            }
        }

        FormSubmitFunctionClass FormSubmitFunctionClass = new FormSubmitFunctionClass();

        FormSubmitFunctionClass.execute(Emergency,longitude,latitude);
    }




}
