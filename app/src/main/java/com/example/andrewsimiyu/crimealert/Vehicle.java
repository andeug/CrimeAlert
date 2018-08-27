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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;


public class Vehicle extends AppCompatActivity {

    Button Done;
    EditText Plate_no, Model, Description ;
    String Plate_no_Holder, Model_Holder, Description_Holder;
    ImageView ivUpload;
    String finalResult,HttpURL,longitude,latitude;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    LocationManager locationManager;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    //protected static final String TAG = "TakePictureActivity";
    Bitmap  mCameraImage;

    public Vehicle() {
        HttpURL = "https://andrewsimiyu.000webhostapp.com/CrimeAlert/VehicleForm.php";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        //Assign Id'S
        Plate_no = findViewById(R.id.etPlateNo);
         Model = findViewById(R.id.etModel);
        Description = findViewById(R.id.etDescription);

        ivUpload=findViewById(R.id.ivAttachment);
        ivUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(arg0== ivUpload){
                    //on attachment icon click
                    requestTakePictureWithCamera();
                }else{
                    Toast.makeText(Vehicle.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
                }
            }});

        Done = findViewById(R.id.btnDone);


        //Adding Click Listener on button.
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                if (CheckEditTextIsEmptyOrNot()){

                    getLocation();
                }

                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(Vehicle.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void getLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;

        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //Toast.makeText(Vehicle.this, "network", Toast.LENGTH_LONG).show();

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    longitude = location.getLongitude() + "";
                    latitude = location.getLatitude() + "";

                    FormSubmitFunction(Plate_no_Holder,Model_Holder, Description_Holder,longitude,latitude);
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

                    FormSubmitFunction(Plate_no_Holder,Model_Holder, Description_Holder, longitude,latitude);
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

        Plate_no_Holder = Plate_no.getText().toString();
        Model_Holder = Model.getText().toString();
        Description_Holder = Description.getText().toString();


        return !TextUtils.isEmpty(Plate_no_Holder) && !TextUtils.isEmpty(Description_Holder);

    }

    public void FormSubmitFunction(final String Plate_no, final String Model,  final String Description,
                                   final String longitude, final String latitude){

        @SuppressLint("StaticFieldLeak")
        class FormSubmitFunctionClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Vehicle.this,"Loading Data",null,true,true);}

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Log.e("**********", "onPostExecute: "+finalResult);
//                Toast.makeText(Vehicle.this, httpResponseMsg, Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Vehicle.this, Navigation.class);
//
//                intent.putExtra("UserEmail","email");
//
//                startActivity(intent);

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("Plate_no",params[0]);

                hashMap.put("Model",params[1]);

                hashMap.put("Description",params[2]);

                hashMap.put("longitude" , params[3]);

                hashMap.put("latitude", params[4]);



                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        FormSubmitFunctionClass FormSubmitFunctionClass = new FormSubmitFunctionClass();

        FormSubmitFunctionClass.execute(Plate_no, Model, Description,longitude,latitude);
    }

}