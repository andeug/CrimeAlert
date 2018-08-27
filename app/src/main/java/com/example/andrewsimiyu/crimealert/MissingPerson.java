package com.example.andrewsimiyu.crimealert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;


public class MissingPerson extends AppCompatActivity {

    Button Done;
    EditText F_name, S_name, Age, Tel_no, Description;
    String F_name_Holder, Age_Holder, Gender_Holder, Description_Holder, S_name_Holder, Tel_no_holder;
    String finalResult;
    String HttpURL;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    LocationManager locationManager;
    String longitude;
    String latitude;
    String Gender;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    //protected static final String TAG = "TakePictureActivity";
    Bitmap  mCameraImage;
    ImageView ivpic;
    // Request code that we will use in onActivityResult() to show that we have
// got a result from our image capture intent
    //private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public MissingPerson() {
        HttpURL = "https://andrewsimiyu.000webhostapp.com/CrimeAlert/MissingPerson.php";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_person);

        //Assign Id'S
        F_name = findViewById(R.id.etFname);
        S_name = findViewById(R.id.etSname);
        Age = findViewById(R.id.etAge);
        Tel_no = findViewById(R.id.etTelno);
        Description = findViewById(R.id.etDescription);


        RadioGroup radioGroup = findViewById(R.id.myRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                if(checkedId == R.id.Male) {

                    Gender= String.valueOf(checkedId);

                } else if(checkedId == R.id.Female) {

                    Gender= String.valueOf(checkedId);
                }
            }
        });

        ivpic=findViewById(R.id.ivAttachment);
        ivpic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(arg0== ivpic){
                    //on attachment icon click
                    requestTakePictureWithCamera();
                }else{
                    Toast.makeText(MissingPerson.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
                }
            }});
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
                    Toast.makeText(MissingPerson.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

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
            Toast.makeText(MissingPerson.this,"inside gps",Toast.LENGTH_LONG).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    longitude = location.getLongitude() + "";

                    latitude = location.getLatitude() + "";
                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    FormSubmitFunction(F_name_Holder, Age_Holder, Gender_Holder, Description_Holder, S_name_Holder, Tel_no_holder, longitude, latitude);
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

    //Allow the user to take a picture using the device camera

    protected void requestTakePictureWithCamera() {
// create Intent to take a picture and return control to the calling
// application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            /*
Image captured and saved to default location
TODO Check that data.getData() does NOT return null
Toast.makeText(this, "Image saved to:\n" + data.getData(),
Toast.LENGTH_LONG).show();
Toast.makeText(getApplicationContext(),
cameraPic.getPixel(1, 1) + "", Toast.LENGTH_LONG)
.show();
*/
            if (resultCode == Activity.RESULT_OK) {
                mCameraImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            } else if (resultCode == Activity.RESULT_CANCELED) {
// User cancelled the image capture
                System.out.println("User cancelled the image capture");
            } else {
// Image capture failed, advise user
                Toast.makeText(getApplicationContext(),
                        "Image NOT captured successfully",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
// getMenuInflater().inflate(R.menu.take_picture, menu);
        return true;
    }


    public boolean CheckEditTextIsEmptyOrNot(){

        F_name_Holder = F_name.getText().toString();
        S_name_Holder=S_name.getText().toString();
        Age_Holder = Age.getText().toString();
        Gender_Holder =Gender;
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
                progressDialog = ProgressDialog.show(MissingPerson.this,"Loading Data",null,true,true);

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();
                Log.e("**********", "onPostExecute: "+finalResult);
                Toast.makeText(MissingPerson.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MissingPerson.this, Navigation.class);

                intent.putExtra("UserEmail","email");

                startActivity(intent);

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