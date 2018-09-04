package com.example.warre.anuzo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.warre.anuzo.R;

public class GoFragment extends Fragment {

    public GoFragment() {
        // Required empty public constructor
    }

    public static GoFragment newInstance() {
        return new GoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_go, container, false);

        return rootView;
    }
}
