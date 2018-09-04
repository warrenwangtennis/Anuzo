package com.example.warre.anuzo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class RequestDefaultLocationActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LocationActivity";

    private EditText mStreetAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZipCode;
    private Button mDone;
    private TextView mSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_default_location);

        mStreetAddress = findViewById(R.id.street_address);
        mCity = findViewById(R.id.city);
        mState = findViewById(R.id.state);
        mZipCode = findViewById(R.id.zip_code);
        mDone = findViewById(R.id.done);
        mSkip = findViewById(R.id.skip);

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sStreetAddress = mStreetAddress.getText().toString();
                String sCity = mCity.getText().toString();
                String sState = mState.getText().toString();
                String sZipCode = mZipCode.getText().toString();
                String fullAddress = String.format("%s %s %s %s", sStreetAddress, sCity, sState, sZipCode);

                LatLng loc = null;
                try {
                    loc = Utility.getLocationFromAddress(getApplicationContext(), fullAddress);
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.toString());
                }

                Log.e(LOG_TAG, "loc:" + loc);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", loc.latitude);
                returnIntent.putExtra("longitude", loc.longitude);
                returnIntent.putExtra("streetAddress", sStreetAddress);
                returnIntent.putExtra("city", sCity);
                returnIntent.putExtra("state", sState);
                returnIntent.putExtra("zipCode", sZipCode);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }
}
