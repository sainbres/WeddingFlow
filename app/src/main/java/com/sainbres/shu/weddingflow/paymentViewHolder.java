package com.sainbres.shu.weddingflow;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class paymentViewHolder extends RecyclerView.ViewHolder {
    TextView paymentName;
    TextView paymentMemo;
    TextView paymentAmount;
    TextView paymentDate;

    paymentViewHolder(View itemView)
    {
        super(itemView);
        paymentName = (TextView)itemView.findViewById(R.id.paymentName);
        paymentMemo = (TextView)itemView.findViewById(R.id.paymentMemo);
        paymentAmount = (TextView)itemView.findViewById(R.id.paymentAmount);
        paymentDate = (TextView)itemView.findViewById(R.id.paymentDate);
    }
}
