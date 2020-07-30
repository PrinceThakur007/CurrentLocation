package com.example.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    EditText latutude;
    EditText longitiude;
    Button getLocation;
    TextView adress;

    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();

    String lat, longi;
    boolean isGpsEnable = false;
    boolean isNetworkEnable = false;


    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latutude = findViewById(R.id.latitude_editText);
        longitiude = findViewById(R.id.longitude_edittext);
        getLocation = findViewById(R.id.getlocation_button);
        adress = findViewById(R.id.addressTextView);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }


        });

        checkUserPermission();
    }


    void getMyLocation() {
        try {
            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {

        }
        try {
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {

        }


        if (!isGpsEnable && !isNetworkEnable) {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Attention");
            builder.setMessage("Sorry the location is not available Please allow it !");
            builder.create().show();

        }

        if (isGpsEnable) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.
                    requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }


        if (isNetworkEnable) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

    }


    class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                lat = location.getLatitude() + "";
                longi = location.getLongitude() + "";

                latutude.setText(lat);
                longitiude.setText(longi);


                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address1 = addresses.get(0).getAddressLine(0);

                adress.setText((address1));
            }
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
    }


    boolean checkUserPermission() {
        int location1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);


        List<String> peremissonList = new ArrayList<>();
        if (location1 != PackageManager.PERMISSION_GRANTED) {
            peremissonList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location2 != PackageManager.PERMISSION_GRANTED) {
            peremissonList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (peremissonList.isEmpty()) {
            ActivityCompat.requestPermissions(this, peremissonList.toArray(new String[peremissonList.size()]), 1);
        }
        return true;
    }
}
