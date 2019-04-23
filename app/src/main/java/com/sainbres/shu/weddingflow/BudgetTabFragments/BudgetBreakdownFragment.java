package com.sainbres.shu.weddingflow.BudgetTabFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sainbres.shu.weddingflow.R;


public class BudgetBreakdownFragment extends Fragment {

    public BudgetBreakdownFragment() {
        // Required empty public constructor
    }

    public static BudgetBreakdownFragment newInstance(String param1, String param2) {
        BudgetBreakdownFragment fragment = new BudgetBreakdownFragment();
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
        return inflater.inflate(R.layout.fragment_budget_breakdown, container, false);
    }

}