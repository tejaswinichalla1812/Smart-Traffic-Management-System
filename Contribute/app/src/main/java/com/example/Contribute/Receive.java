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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Receive extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private int REQUEST_CODE = 11;
    SupportMapFragment mapFragment;
    EditText mFullName,mDescription,pincode,addresss,phone_tv;
    Button mSubmitBtn,navigate_donor;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID,latitude="",longitude="";
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        mFullName = findViewById(R.id.receivername);
        pincode = findViewById(R.id.pincode);
        mDescription = findViewById(R.id.description);
        phone_tv = findViewById(R.id.phone_tv);
        addresss = findViewById(R.id.addresss);
        mSubmitBtn=findViewById(R.id.delete);
        navigate_donor=findViewById(R.id.navigate_donor);

        fAuth=FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        /*
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapFragment.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

*/
        navigate_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGPS();

            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = mFullName.getText().toString().trim();
                String code = pincode.getText().toString();
                String number = phone_tv.getText().toString();
                String addressss = addresss.getText().toString();
                String description= mDescription.getText().toString().trim();
                String type= "Receiver";

                if(TextUtils.isEmpty(fullname))
                {
                    mFullName.setError("Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(addressss))
                {
                    addresss.setError("Address is Required.");
                    return;
                }

                if(number.length() < 10)
                {
                    phone_tv.setError("Phone Number Must be >=10 Characters");
                    return;
                }

                if(TextUtils.isEmpty(code))
                {
                    pincode.setError("pincode is Required.");
                    return;
                }
                if(TextUtils.isEmpty(description))
                {
                    mFullName.setError("Description is Required.");
                    return;
                }
         */
                userID = fAuth.getCurrentUser().getUid();
                //DocumentReference documentReference = fStore.collection("receiver").document(userID);
                CollectionReference collectionReference = fStore.collection("user data");

                //   GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                Map<String,Object> user = new HashMap<>();
                user.put("timestamp", FieldValue.serverTimestamp());
                user.put("name",fullname);
                user.put("description",description);
                user.put("pincode",code);
                user.put("address",addressss);
                user.put("thingstoDonate", "");

                user.put("phone",number);
                // user.put("location",geoPoint);
                user.put("userid",userID);
                user.put("latitude", latitude);
                user.put("longitude", longitude);
                user.put("isOpen", "true");
                user.put("foodDurationTime", "1");

                user.put("type",type);

                collectionReference.add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                sendNotification(fullname," : we need your help");

                                Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"Success!");

                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                Intent intent = new Intent(Receive.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Error!", e);
                            }
                        });

            }
        });
    }