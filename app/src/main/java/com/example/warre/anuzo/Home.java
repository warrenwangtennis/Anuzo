package com.example.warre.anuzo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    private static final String LOG_TAG = "Home";
    private static int REQUEST_DEFAULT = 1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private DocumentSnapshot mSnap;
    private SharedPreferences mUserData;
    private DocumentReference mUserRef;

    private MyPagerAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String from = i.getStringExtra("from");
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mUserData = PreferenceManager.getDefaultSharedPreferences(this);
        CollectionReference users = mDb.collection("users");
        mUserRef = users.document(mAuth.getUid());
        if (from.equals("login")) {
            Log.e(LOG_TAG, "login");
            if (!mAuth.getUid().equals(mUserData.getString("uid", ""))) {
                String s = "different user\n";
                s += "auth:" + mAuth.getUid() + "\n";
                s += "userdata:" + mUserData.getString("uid", "");
                Log.e(LOG_TAG, s);
            }
        } else if (from.equals("register")) {
            Log.e(LOG_TAG, "register");
            String name = i.getStringExtra("name");
            String email = i.getStringExtra("email");

            SharedPreferences.Editor edit = mUserData.edit();
            edit.putString("uid", mAuth.getUid());
            edit.putString("name", name);
            edit.putString("email", email);
            edit.commit();

            Map<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("email", email);
            mUserRef.set(data);

            Intent i2 = new Intent(Home.this, RequestDefaultLocationActivity.class);
            startActivityForResult(i2, REQUEST_DEFAULT);
        } else {
            Log.e(LOG_TAG, "not login or register");
        }

//        mUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (!document.exists()) {
//                        initializeNewUser(mUserRef);
//                    }
//                    mSnap = task.getResult();
//                } else {
//                    Log.e(LOG_TAG, "get failed with ", task.getException());
//                }
//            }
//        });

        setContentView(R.layout.activity_home);


        mAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mPager = findViewById(R.id.vpPager);
        mPager.setAdapter(mAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(LOG_TAG, "on ac re" + requestCode + " " + resultCode);
        if (requestCode == REQUEST_DEFAULT) {
            if (resultCode == Activity.RESULT_OK) {
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String streetAddress = data.getStringExtra("streetAddress");
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String zipCode = data.getStringExtra("zipCode");

                Map<String, Object> map = new HashMap<>();
                map.put("defaultLatitude", latitude);
                map.put("defaultLongitude", longitude);
                map.put("defaultStreetAddress", streetAddress);
                map.put("defaultCity", city);
                map.put("defaultState", state);
                map.put("defaultZipCode", zipCode);
                Log.e(LOG_TAG, "update database");
                mUserRef.update(map);

                SharedPreferences.Editor edit = mUserData.edit();
                edit.putLong("defaultLatitude", Double.doubleToRawLongBits(latitude));
                edit.putLong("defaultLongitude", Double.doubleToRawLongBits(longitude));
                edit.putString("defaultStreetAddress", streetAddress);
                edit.putString("defaultCity", city);
                edit.putString("defaultState", state);
                edit.putString("defaultZipCode", zipCode);
                edit.commit();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(LOG_TAG, "user canceled");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    private void initializeNewUser(DocumentReference userRef) {
//        Log.e(LOG_TAG, "initializing new user");
//
//    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return GoFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ComeFragment.newInstance();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return FriendsFragment.newInstance();
                case 3:
                    return SettingsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Home.this.getApplicationContext().getResources().getString(R.string.go_fragment_title);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Home.this.getApplicationContext().getResources().getString(R.string.come_fragment_title);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return Home.this.getApplicationContext().getResources().getString(R.string.friends_fragment_title);
                case 3:
                    return Home.this.getApplicationContext().getResources().getString(R.string.settings_fragment_title);
                default:
                    return null;

            }
        }
    }
}