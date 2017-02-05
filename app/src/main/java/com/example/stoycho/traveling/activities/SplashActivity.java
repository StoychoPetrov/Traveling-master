package com.example.stoycho.traveling.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.stoycho.traveling.R;
import com.example.stoycho.traveling.database.HotelsDatabase;
import com.example.stoycho.traveling.models.Hotel;
import com.example.stoycho.traveling.tasks.Request;
import com.example.stoycho.traveling.utils.Constants;
import com.example.stoycho.traveling.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean locationGranted;
    private boolean timeExpired;
    private boolean loadHotels;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                locationGranted = true;
                startHomeScreen();
            }
        } else {
            locationGranted = true;
            startHomeScreen();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeExpired = true;
                startHomeScreen();
            }
        }, 1000);
    }

    private void startHomeScreen() {
        if (locationGranted && timeExpired && loadHotels) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void loadHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String requestUrl = Constants.URL;
        final HotelsDatabase database = new HotelsDatabase(SplashActivity.this);
        Request getDataFromUrl = new Request(requestUrl) {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    try {
                        List<Hotel> hotels = new ArrayList<>();
                        JSONObject object = new JSONObject(s);
                        JSONArray hotelsJson = object.getJSONArray("data");
                        for (int i = 0; i < hotelsJson.length(); i++) {
                            JSONObject hotelJson = hotelsJson.getJSONObject(i);
                            Hotel hotel = new Hotel();
                            hotel.setmId(hotelJson.getInt("id"));
                            if (hotelJson.has("name"))
                                hotel.setmName(hotelJson.getString("name"));
                            if (hotelJson.has("description"))
                                hotel.setmDescription(hotelJson.getString("description"));
                            if (hotelJson.has("phone"))
                                hotel.setmPhone(hotelJson.getString("phone"));
                            if (hotelJson.has("email"))
                                hotel.setmEmail(hotelJson.getString("email"));
                            if (hotelJson.has("website"))
                                hotel.setmWebsite(hotelJson.getString("website"));
                            if (hotelJson.has("address"))
                                hotel.setmAdress(hotelJson.getString("address"));
                            if (hotelJson.has("latitude") && hotelJson.has("longitude")) {
                                hotel.setmLatitude(hotelJson.getDouble("latitude"));
                                hotel.setmLongitude(hotelJson.getDouble("longitude"));

                                Location location = null;
                                if (mLastLocation != null)
                                    location = new Location(mLastLocation);
                                else {
                                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                    if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    location = new Location(LocationManager.GPS_PROVIDER);
                                }
                                location.setLatitude(hotel.getmLatitude());
                                location.setLongitude(hotel.getmLongitude());

                                double distance = mLastLocation.distanceTo(location);
                                hotel.setmDistance(distance / 1000);
                            }
                            JSONArray imagesJson = hotelJson.getJSONArray("images");
                            List<String> images = new ArrayList<>();
                            for (int j = 0; j < imagesJson.length(); j++) {
                                JSONObject image = imagesJson.getJSONObject(j);
                                images.add(image.getString("path"));
                            }
                            hotel.setmImages(images);
                            hotels.add(hotel);
                            database.insertIntoHotels(hotel);
                        }
                        Utils.setmHotels(hotels);

                        loadHotels = true;
                        startHomeScreen();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(database.getCountOfHotels() == 0)
            getDataFromUrl.execute();
        else {
            hotels = database.selectAllHotels();
            for(Hotel hotel : hotels)
            {
                Location location = null;
                if (mLastLocation != null)
                    location = new Location(mLastLocation);
                else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    location = new Location(LocationManager.GPS_PROVIDER);
                }
                location.setLatitude(hotel.getmLatitude());
                location.setLongitude(hotel.getmLongitude());

                double distance = mLastLocation.distanceTo(location);
                hotel.setmDistance(distance / 1000);
            }
            Utils.setmHotels(hotels);
            loadHotels = true;
            startHomeScreen();
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 4
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    locationGranted = true;
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
                    }
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            startHomeScreen();
            loadHotels();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
