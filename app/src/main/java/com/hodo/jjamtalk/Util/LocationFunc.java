package com.hodo.jjamtalk.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hodo.jjamtalk.InputProfile;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by boram on 2017-08-05.
 */

public class LocationFunc {

    private static LocationFunc _Instance;

    public static LocationFunc getInstance() {
        if (_Instance == null)
            _Instance = new LocationFunc();

        return _Instance;
    }

    private LocationFunc()
    {

    }
    LocationManager locationManager;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(Context mContext, final Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(mContext)
                        .setTitle("동의서")
                        .setMessage("위치 정보 권한 동의서")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(mActivity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    public String getCity(Context mContext, double Lat, double Lon)
    {

        Geocoder _Geocoder = new Geocoder(mContext);
        String _Result = "";
        try {
            Iterator<Address> _Addresses = _Geocoder.getFromLocation(Lat,
                    Lon, 1).iterator();
            if (_Addresses != null) {
                while (_Addresses.hasNext()) {
                    Address namedLoc = _Addresses.next();
                    String placeName = namedLoc.getLocality();
                    String AdminName = namedLoc.getAdminArea();
                    String featureName = namedLoc.getFeatureName();
                    String country = namedLoc.getCountryName();
                    String road = namedLoc.getThoroughfare();

                    _Result = AdminName;
                }
            }
        } catch (IOException e) {
        }

        return  _Result;
    }


    public float getDistance(double SourceLat, double SourceLon, double DestLat, double DestLon)
    {

        Location SourceLoc = new Location("Source");
        SourceLoc.setLatitude(SourceLat);
        SourceLoc.setLongitude(SourceLon);

        Location DestLoc = new Location("Destination");
        DestLoc.setLatitude(DestLat);
        DestLoc.setLongitude(DestLon);

        return SourceLoc.distanceTo(DestLoc);
    }
}
