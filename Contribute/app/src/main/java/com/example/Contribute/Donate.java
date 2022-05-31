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
                if (TextUtils.isEmpty(fullname)) {
                    mFullName.setError("Name is Required.");
                    return;
                }

                if (TextUtils.isEmpty(addressss)) {
                    address.setError("Required.");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    pincode.setError("Required.");
                    return;
                }
                if (TextUtils.isEmpty(foodDurationTime)) {
                    foodDuration.setError("Required.");
                    return;
                }
                if (TextUtils.isEmpty(thingstoDonate)) {
                    donorthings.setError("Required.");
                    return;
                }
                if (phone.length() < 10) {
                    mPhone.setError("Phone Number Must be >=10 Characters");
                    return;
                }

                userID = fAuth.getCurrentUser().getUid();
                CollectionReference collectionReference = fStore.collection("user data");
                Map<String, Object> user = new HashMap<>();
                user.put("name", fullname);
                user.put("food item", "");
                user.put("phone", phone);
                user.put("description", description);
                user.put("pincode", code);
                user.put("address", addressss);
                user.put("thingstoDonate", thingstoDonate);
                user.put("userid", userID);
                user.put("latitude", latitude);
                user.put("longitude", longitude);
                user.put("isOpen", "true");
                user.put("foodDurationTime", foodDurationTime);
                user.put("type", type);


                collectionReference.add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                sendNotification(fullname, thingstoDonate, type);
                                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Success!");
                                Intent intent = new Intent(Donate.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Error!", e);
                            }
                        });
            }
        });

        navigate_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   checkPermmission
                checkGPS();
            }
        });
    }

    private void sendNotification(String fullname, String fooditem, String type) {
        String topic = "/topics/FOOD";
        String title = "DONOR";
        try {
            JSONObject notification = new JSONObject();
            JSONObject notificationBody = new JSONObject();
            try {
                notificationBody.put("title", title);
                notificationBody.put("message", fullname + " wants to donate " + fooditem);
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
                    Toast.makeText(Donate.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Donate.this, "Notification not sent", Toast.LENGTH_SHORT).show();
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


