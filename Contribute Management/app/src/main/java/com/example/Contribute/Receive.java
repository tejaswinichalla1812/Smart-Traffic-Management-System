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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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


                userID = fAuth.getCurrentUser().getUid();
                //DocumentReference documentReference = fStore.collection("receiver").document(userID);
                CollectionReference collectionReference = fStore.collection("user data");
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

             //   GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                Map<String,Object> user = new HashMap<>();
                user.put("timestamp", FieldValue.serverTimestamp());
                user.put("name",fullname);
                user.put("timeID", currentTime);

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

                FirebaseFirestore db=FirebaseFirestore.getInstance();
                db.collection("user data").document(currentTime)
                        .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sendNotification(fullname," : we need your help");

                        Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Success!");

                        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Intent intent = new Intent(Receive.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error!", e);

                    }
                });


   /*             collectionReference.add(user)
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
*/
            }
        });


    }

    private void sendNotification(String fullname, String fooditem) {
        String topic = "/topics/FOOD";
        String title = "RECEIVE";

        try {
            JSONObject notification = new JSONObject();
            JSONObject notificationBody = new JSONObject();
            try {
                notificationBody.put("title", title);
                notificationBody.put("message", fullname + fooditem);
                notification.put("to", topic);
                notification.put("data", notificationBody);
            } catch (Exception e) {
                e.getMessage();
            }
            sendPayload(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendPayload(JSONObject notification) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);

            String url = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(url, notification, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(Receive.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Receive.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    String api_key_header_value = "Key=AAAA5fPOgyY:APA91bFNMsEmGFiVPIhelK92PPeJR0b4wiqTr7uvaHHkGFP6bAWvtrjX7kL9H7ws2pIJPzm_2SwLAGk1vNXgE-DDDbcZ81GdxxeMVuJkZ1dF0q_RMq20Et8BRsUVT7fuN6fmzROB6qd7";
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkGPS() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2222);
        }
        LocationManager locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Enable GPS ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), FoodMap.class);
            startActivityForResult(intent,1111);
        }else{
            showGPSdisabledAlert();
        }
    }

    private void showGPSdisabledAlert() {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("GPS ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog ale= alert.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1111){
            if(resultCode==RESULT_OK){
                String addressLocation=data.getStringExtra("ADDRESS");
              Double  latt= data.getDoubleExtra("LATITUDE",0.0d);
                Double longi= data.getDoubleExtra("LONGITUDE",0.0d);
                latitude=latt.toString();
                longitude=longi.toString();

                addresss.setText(addressLocation);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        //MarkerOptions markerOptions1 = new MarkerOptions().position(latLng).title("You are here");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.addMarker(markerOptions1).showInfoWindow();

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        mMap.addMarker(markerOptions).showInfoWindow();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(1000);
        //mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
           //     mapFragment.getMapAsync(this);
            }else{
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
