package com.example.Contribute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Donate extends AppCompatActivity {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private int REQUEST_CODE = 11;
    SupportMapFragment mapFragment;
    EditText mFullName,  mDescription, mPhone, pincode, address,donorthings,foodDuration;
    Button mSubmitBtn, navigate_donor;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID,latitude="",longitude="";
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        /*Views initialization*/
        donorthings = findViewById(R.id.donorthings);
        foodDuration = findViewById(R.id.foodDuration);
        mFullName = findViewById(R.id.donorname);
        navigate_donor = findViewById(R.id.navigate_donor);
        mPhone = findViewById(R.id.donor_phone);
        pincode = findViewById(R.id.pincode);
        mDescription = findViewById(R.id.description);
        mSubmitBtn = findViewById(R.id.delete);
        address = findViewById(R.id.address);

        /* getting an instance of firebase auth and firebase firestore for authentication and saving data*/
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        /* saving data to firebase server*/
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getting data from views id and storing in related varaibles*/
                String fullname = mFullName.getText().toString().trim();
                String thingstoDonate = donorthings.getText().toString().trim();
                String description = mDescription.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String code = pincode.getText().toString();
                String addressss = address.getText().toString();
                String foodDurationTime = foodDuration.getText().toString();
                String type = "Donor";
