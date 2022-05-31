package com.example.Contribute;

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
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserDetailsItems extends AppCompatActivity {

    String name, address, phone, description, type, latitude, longitude, ISOPEN;
    TextView nameSet, addressSet, phoneSet, descriptionSet, typeSet;
    ImageView phoneCall;
    SupportMapFragment mapFragment;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private int REQUEST_CODE = 11;
    Button submitStatus;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_items);

        phoneCall = findViewById(R.id.phoneCall);
        nameSet = findViewById(R.id.userDetailsName);
        addressSet = findViewById(R.id.addresUserDetails);
        submitStatus = findViewById(R.id.submitStatus);
        phoneSet = findViewById(R.id.phoneUserDetails);
        descriptionSet = findViewById(R.id.descriptionUserDetails);
        typeSet = findViewById(R.id.type);

        try {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

            address = getIntent().getStringExtra("ADDRESS");
            name = getIntent().getStringExtra("NAME");
            ISOPEN = getIntent().getStringExtra("ISOPEN");
            latitude = getIntent().getStringExtra("LATITUDE");
            longitude = getIntent().getStringExtra("LONGITUDE");
            phone = getIntent().getStringExtra("PHONE");
            type = getIntent().getStringExtra("TYPE");
            description = getIntent().getStringExtra("DESCRIPTION");

            nameSet.setText(name);
            addressSet.setText(address);
            phoneSet.setText(phone);
            descriptionSet.setText(description);
            typeSet.setText(type);

            phoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneSet.getText().toString(), null));
                    startActivity(intent);
                }
            });

            if (ISOPEN.equals("true")) {
                submitStatus.setVisibility(View.VISIBLE);
            } else {
                submitStatus.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (latitude != null && longitude != null && !latitude.isEmpty() && !longitude.isEmpty()) {

            try {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2222);
                }

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                MarkerOptions markerOptions = new MarkerOptions();
                                // Set position of marker
                                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                markerOptions.position(latLng);
                                // Set title of marker
                                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                                // Remove all marker
                                googleMap.clear();
                                // Animating to zoom the marker
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                // Add marker on map
                                googleMap.addMarker(markerOptions);
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    }
                } else {
                    showGPSdisabledAlert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showGPSdisabledAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("GPS ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog ale = alert.create();
        alert.show();
    }


}