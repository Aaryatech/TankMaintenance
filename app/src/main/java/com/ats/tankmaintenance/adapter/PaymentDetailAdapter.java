package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.PaymentDetail;

import java.util.ArrayList;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.MyViewHolder>  {
private ArrayList<PaymentDetail> paymentDetailList;
private Context context;

    public PaymentDetailAdapter(ArrayList<PaymentDetail> paymentDetailList, Context context) {
        this.paymentDetailList = paymentDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_payment_detail_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentDetailAdapter.MyViewHolder myViewHolder, int i) {
        PaymentDetail model=paymentDetailList.get(i);
        myViewHolder.tvPaymentdate.setText(""+model.getPaymentDate());
        myViewHolder.tvPaymentAmt.setText(""+model.getCostRs());
        myViewHolder.tvWorkAmt.setText(""+model.getTotalAmt());

    }

    @Override
    public int getItemCount() {
        return paymentDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPaymentdate,tvPaymentAmt,tvWorkAmt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentdate=itemView.findViewById(R.id.tvPaymentDate);
            tvPaymentAmt=itemView.findViewById(R.id.tvPaymentAmt);
            tvWorkAmt=itemView.findViewById(R.id.tvWorkAmt);
        }
    }
}
