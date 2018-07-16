package com.myapplicationdev.android.p09_gettingmylocation;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyService extends Service {
    public MyService() {
    }

    boolean started;
    FusedLocationProviderClient client;
    LocationCallback mLocationCallback;
    String folderLocation;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("Service", "Service created");
        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(MyService.this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();
                    String msg = "Latitude : " + lat + " Longitude: " + lng + "\n";
                    //Code for file writing
                    try {
                        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
                        File targetFile = new File(folderLocation, "data.txt");
                        FileWriter writer = new FileWriter(targetFile, true);
                        writer.write(msg);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        Log.e("failed","failed to write");
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started == false) {
            started = true;
            Log.d("Service", "Service started");
            Toast.makeText(MyService.this,"Service started",Toast.LENGTH_LONG).show();
        } else {
            Log.d("Service", "Service is still running");
            Toast.makeText(MyService.this,"Service is running",Toast.LENGTH_LONG).show();
        }

        //requestLocationUpdate
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //removeLocationUpdate
        client.removeLocationUpdates(mLocationCallback);
        Log.d("Service", "Service exited");
        super.onDestroy();
    }
}
