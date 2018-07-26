package com.example.andrewsimiyu.crimealert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;


public class Witness extends AppCompatActivity {

    ImageView ivpic;
    Button Done;
    EditText F_name, S_name, Age, Gender, Tel_no, Description;
    String F_name_Holder, Age_Holder, Gender_Holder, Description_Holder, S_name_Holder, Tel_no_holder;
    String finalResult;
    String HttpURL;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    LocationManager locationManager;
    //Bitmap mCameraImage;
    String longitude;
    String latitude;
    // String mLongitude;
    // String mLatitude;
    // Request code that we will use in onActivityResult() to show that we have
// got a result from our image capture intent
    //private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public Witness() {
        HttpURL = "https://andrewsimiyu.000webhostapp.com/CrimeAlert/SuspectForm.php";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witness);

        //Assign Id'S
        F_name = findViewById(R.id.etFname);
        S_name = findViewById(R.id.etSname);
        Age = findViewById(R.id.etAge);
        Gender = findViewById(R.id.etGender);
        Tel_no = findViewById(R.id.etTelno);
        Description = findViewById(R.id.etDescription);


//        btnUpload = findViewById(R.id.btnUpload);
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                requestTakePictureWithCamera();
//            }
//        });

      ivpic=findViewById(R.id.ivAttachment);
        Done = findViewById(R.id.btnDone);

        //Adding Click Listener on button.
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking whether EditText is Empty or Not
                if (CheckEditTextIsEmptyOrNot()) {
                    getLocation();

                } else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(Witness.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void getLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
            TODO: Consider calling
            ActivityCompat#requestPermissions
            here to request the missing permissions, and then overriding
            public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults)
            to handle the case where the user grants the permission. See the documentation
            for ActivityCompat#requestPermissions for more details.
            */
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    longitude = location.getLongitude() + "";
                    latitude = location.getLatitude() + "";

                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    FormSubmitFunction(F_name_Holder, Age_Holder, Gender_Holder, Description_Holder, S_name_Holder, Tel_no_holder, longitude, latitude);
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
                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    FormSubmitFunction(F_name_Holder, Age_Holder, Gender_Holder, Description_Holder, S_name_Holder, Tel_no_holder, longitude, latitude);

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
//      //Allow the user to take a picture using the device camera
//
//    protected void requestTakePictureWithCamera() {
//// create Intent to take a picture and return control to the calling
//// application
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//// start the image capture Intent
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            /*
//Image captured and saved to default location
//TODO Check that data.getData() does NOT return null
//Toast.makeText(this, "Image saved to:\n" + data.getData(),
//Toast.LENGTH_LONG).show();
//Toast.makeText(getApplicationContext(),
//cameraPic.getPixel(1, 1) + "", Toast.LENGTH_LONG)
//.show();
//*/
//            if (resultCode == Activity.RESULT_OK) {
//                mCameraImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//// User cancelled the image capture
//                System.out.println("User cancelled the image capture");
//            } else {
//// Image capture failed, advise user
//                Toast.makeText(getApplicationContext(),
//                        "Image NOT captured successfully",
//                        Toast.LENGTH_LONG)
//                        .show();
//            }
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//// Inflate the menu; this adds items to the action bar if it is present.
//// getMenuInflater().inflate(R.menu.take_picture, menu);
//        return true;
//    }


    public boolean CheckEditTextIsEmptyOrNot(){

        F_name_Holder = F_name.getText().toString();
        S_name_Holder=S_name.getText().toString();
        Age_Holder = Age.getText().toString();
        Gender_Holder =Gender.getText().toString();
        Tel_no_holder=Tel_no.getText().toString().trim();
        Description_Holder = Description.getText().toString();


        return !TextUtils.isEmpty(F_name_Holder) && !TextUtils.isEmpty(Age_Holder) && !TextUtils.isEmpty(Gender_Holder) && !TextUtils.isEmpty(Description_Holder);

    }

    public void FormSubmitFunction(final String F_name, final String S_name, final String Age, final String Gender ,final String Tel_no, final String Description, final String longitude,
                                   final String latitude){

        @SuppressLint("StaticFieldLeak")
        class FormSubmitFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Toast.makeText(Suspect.this,finalResult,Toast.LENGTH_LONG).show();
                progressDialog = ProgressDialog.show(Witness.this,"Loading Data",null,true,true);

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Log.e("**********", "onPostExecute: "+finalResult);
                Toast.makeText(Witness.this, httpResponseMsg, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("F_name", params[0]);

                hashMap.put("S_name", params[4]);

                hashMap.put("Age", params[1]);

                hashMap.put("Gender", params[2]);

                hashMap.put("Tel_no", params[5]);

                hashMap.put("Description", params[3]);

                hashMap.put("longitude" , params[6]);

                hashMap.put("latitude", params[7]);

                //hashMap.put("mCameraImage"), params[8];



                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;

            }
        }

        FormSubmitFunctionClass FormSubmitFunctionClass = new FormSubmitFunctionClass();

        FormSubmitFunctionClass.execute(F_name, S_name, Age , Gender, Tel_no, Description,longitude,latitude);
    }

}