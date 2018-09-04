package com.example.warre.anuzo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Utility {
    public static LatLng getLocationFromAddress(Context context, String strAddress) throws IOException {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        // May throw an IOException
        address = coder.getFromLocationName(strAddress, 5);
        if (address == null) {
            return null;
        }

        Address location = address.get(0);
        p1 = new LatLng(location.getLatitude(), location.getLongitude());

        return p1;
    }
}
