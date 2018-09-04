package com.example.warre.anuzo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.warre.anuzo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {
    private static final String LOG_TAG = "SettingsFragment";
    private static int REQUEST_DEFAULT = 2;

    private SharedPreferences mUserData;
    private FirebaseAuth mAuth;

    private TextView mStreetAddress;
    private TextView mCity;
    private TextView mState;
    private TextView mZipCode;
    private Button mEditDefaultAddress;
    private Button mLogOut;
    private Button mDeleteAccount;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserData = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mStreetAddress = view.findViewById(R.id.street_address);
        mCity = view.findViewById(R.id.city);
        mState = view.findViewById(R.id.state);
        mZipCode = view.findViewById(R.id.zip_code);
        mEditDefaultAddress = view.findViewById(R.id.edit_default_address);
        mLogOut = view.findViewById(R.id.log_out);
        mDeleteAccount = view.findViewById(R.id.delete_account);

        updateUI();

        mEditDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsFragment.this.getActivity(), RequestDefaultLocationActivity2.class);
                startActivityForResult(i, REQUEST_DEFAULT);
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mUserData.edit().clear().commit();
                Intent i = new Intent(SettingsFragment.this.getActivity(), LoginActivity.class);
                SettingsFragment.this.getActivity().finish();
                startActivity(i);
            }
        });

        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mUserData.edit().clear().commit();

                Intent i = new Intent(SettingsFragment.this.getActivity(), LoginActivity.class);
                SettingsFragment.this.getActivity().finish();
                startActivity(i);
            }
        });

        return view;
    }

    public void updateUI() {
        if (mUserData.contains("defaultStreetAddress")) {
            mStreetAddress.setText(mUserData.getString("defaultStreetAddress", "error"));
        } else {
            Log.e(LOG_TAG, "no street address");
        }
        if (mUserData.contains("defaultCity")) {
            mCity.setText(mUserData.getString("defaultCity", "error"));
        } else {
            Log.e(LOG_TAG, "no city");
        }
        if (mUserData.contains("defaultState")) {
            mState.setText(mUserData.getString("defaultState", "error"));
        } else {
            Log.e(LOG_TAG, "no state");
        }
        if (mUserData.contains("defaultZipCode")) {
            mZipCode.setText(mUserData.getString("defaultZipCode", "error"));
        } else {
            Log.e(LOG_TAG, "no zip code");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(LOG_TAG, "on activity result");
        if (requestCode == REQUEST_DEFAULT) {
            if (resultCode == Activity.RESULT_OK) {
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String streetAddress = data.getStringExtra("streetAddress");
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String zipCode = data.getStringExtra("zipCode");

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference users = db.collection("users");
                DocumentReference userRef = users.document(mAuth.getUid());

                Map<String, Object> map = new HashMap<>();
                map.put("defaultLatitude", latitude);
                map.put("defaultLongitude", longitude);
                map.put("defaultStreetAddress", streetAddress);
                map.put("defaultCity", city);
                map.put("defaultState", state);
                map.put("defaultZipCode", zipCode);
                userRef.update(map);
                Log.e(LOG_TAG, "updated database");

                SharedPreferences.Editor edit = mUserData.edit();
                edit.putLong("defaultLatitude", Double.doubleToRawLongBits(latitude));
                edit.putLong("defaultLongitude", Double.doubleToRawLongBits(longitude));
                edit.putString("defaultStreetAddress", streetAddress);
                edit.putString("defaultCity", city);
                edit.putString("defaultState", state);
                edit.putString("defaultZipCode", zipCode);
                edit.commit();
                Log.e(LOG_TAG, "committed shared prefs");

                updateUI();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(LOG_TAG, "user canceled");
            }
        }
    }
}
