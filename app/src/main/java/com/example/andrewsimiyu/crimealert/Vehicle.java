package com.example.andrewsimiyu.crimealert;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;


public class Vehicle extends AppCompatActivity {

    Button Done;
    EditText Plate_no, Model, Description ;
    String Plate_no_Holder, Model_Holder, Description_Holder;
    ImageView ivUpload;
    String finalResult ;
    String HttpURL = "https://andrewsimiyu.000webhostapp.com/CrimeAlert/VehicleForm.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        //Assign Id'S
        Plate_no = findViewById(R.id.etPlateNo);
         Model = findViewById(R.id.etModel);
        Description = findViewById(R.id.etDescription);

        ivUpload = findViewById(R.id.ivAttachment);

        Done = findViewById(R.id.btnDone);


        //Adding Click Listener on button.
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    FormSubmitFunction(Plate_no_Holder,Model_Holder, Description_Holder);

                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(Vehicle.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    public void CheckEditTextIsEmptyOrNot(){

        Plate_no_Holder = Plate_no.getText().toString();
        Model_Holder = Model.getText().toString();
        Description_Holder = Description.getText().toString();


        CheckEditText = !TextUtils.isEmpty(Plate_no_Holder) && !TextUtils.isEmpty(Model_Holder) && !TextUtils.isEmpty(Description_Holder);

    }

    public void FormSubmitFunction(final String Plate_no, final String Model,  final String Description){
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
                Toast.makeText(Vehicle.this, httpResponseMsg, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("Plate_no",params[0]);

                hashMap.put("Model",params[1]);

                hashMap.put("Description",params[2]);



                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        FormSubmitFunctionClass FormSubmitFunctionClass = new FormSubmitFunctionClass();

        FormSubmitFunctionClass.execute(Plate_no, Model, Description);
    }

}