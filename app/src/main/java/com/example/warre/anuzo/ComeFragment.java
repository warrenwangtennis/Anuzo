package com.example.warre.anuzo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.warre.anuzo.R;

public class ComeFragment extends Fragment {

    public ComeFragment() {
        // Required empty public constructor
    }

    public static ComeFragment newInstance() {
        return new ComeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_come, container, false);
    }
}
