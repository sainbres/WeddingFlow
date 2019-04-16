package com.sainbres.shu.weddingflow.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sainbres.shu.weddingflow.R;


public class OrganizeFragment extends Fragment {

    public OrganizeFragment() {
        // Required empty public constructor
    }

    public static OrganizeFragment newInstance(String param1, String param2) {
        OrganizeFragment fragment = new OrganizeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organize, container, false);
    }

}