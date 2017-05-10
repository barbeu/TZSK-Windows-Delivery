package com.example.tzadmin.tzsk_windows.TabFragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tzadmin.tzsk_windows.R;

/**
 * Created by tzadmin on 06.05.17.
 */

public class Tab2Statuses extends Fragment {

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2statuses, container, false);
        return rootView;
    }
}
