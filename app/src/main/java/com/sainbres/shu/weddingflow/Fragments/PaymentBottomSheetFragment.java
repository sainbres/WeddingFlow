package com.sainbres.shu.weddingflow.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sainbres.shu.weddingflow.AddPaymentActivitiy;
import com.sainbres.shu.weddingflow.R;


public class PaymentBottomSheetFragment extends BottomSheetDialogFragment {

    public PaymentBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_bottom_sheet, container, false);

        Button addPaymentBtn = view.findViewById(R.id.addPaymentBtn);
        Button editSavingsBtn = view.findViewById(R.id.editSavingsBtn);

        addPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getView().getContext(), AddPaymentActivitiy.class);
                startActivity(intent);
            }
        });

        editSavingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

}