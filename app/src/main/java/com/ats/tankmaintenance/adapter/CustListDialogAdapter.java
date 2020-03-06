package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.Customer;

import java.util.ArrayList;

public class CustListDialogAdapter extends RecyclerView.Adapter<CustListDialogAdapter.MyViewHolder> {

    private ArrayList<Customer> custList;
    private Context context;

    public CustListDialogAdapter(ArrayList<Customer> custList, Context context) {
        this.custList = custList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustListDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_area_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustListDialogAdapter.MyViewHolder myViewHolder, int i) {
        final Customer model= custList.get(i);
        myViewHolder.tvName.setText(""+model.getCustomerName());

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent customerDataIntent = new Intent();
                customerDataIntent.setAction("CUST_DATA");
                customerDataIntent.putExtra("custName", model.getCustomerName());
                customerDataIntent.putExtra("custId", model.getCustomerId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(customerDataIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return custList.size();
    }

    public void updateList(ArrayList<Customer> list) {
        custList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
