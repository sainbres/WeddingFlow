package com.sainbres.shu.weddingflow.BudgetTabFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Fragments.PaymentBottomSheetFragment;
import com.sainbres.shu.weddingflow.Models.Payment;
import com.sainbres.shu.weddingflow.Models.Payment_Table;
import com.sainbres.shu.weddingflow.PaymentAdapter;
import com.sainbres.shu.weddingflow.R;

import java.util.List;


public class BudgetBreakdownFragment extends Fragment {

    View view;
    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;
    private int budgetId;

    PaymentAdapter adapter;
    RecyclerView recyclerView;

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
        view = inflater.inflate(R.layout.fragment_budget_breakdown, container, false);

        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Editor = SharedPrefs.edit();

        budgetId = SharedPrefs.getInt(getString(R.string.SP_BudgetId), -1);

        List<Payment> payments = SQLite.select().from(Payment.class).where(Payment_Table.BudgetId.eq(budgetId)).orderBy(Payment_Table.Date, true).queryList();

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentBottomSheetFragment bottomSheet = new PaymentBottomSheetFragment();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "bottomSheet");
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new PaymentAdapter(payments, getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

}