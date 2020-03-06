package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.activity.MainActivity;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.report.CustDetailReportFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CustReportAdapter extends RecyclerView.Adapter<CustReportAdapter.MyViewHolder> {
private ArrayList<Payment> custReportList;
private Context context;

    public CustReportAdapter(ArrayList<Payment> custReportList, Context context) {
        this.custReportList = custReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_customer_report_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustReportAdapter.MyViewHolder myViewHolder, int i) {
        final Payment model=custReportList.get(i);
        myViewHolder.tvName.setText(model.getCustomerName());
        myViewHolder.tvMob.setText(model.getCustomerPhone());
        myViewHolder.tvLocation.setText(model.getCustomerAddress());
        myViewHolder.tvWorkAmt.setText(""+model.getWorkAmt());
        myViewHolder.tvPayAmt.setText(""+model.getPayAmt());
        myViewHolder.tvUpperTankCost.setText(""+model.getCostUppertankPerpieces());
        myViewHolder.tvLowertankCost.setText(""+model.getCostLowertankPerpieces());

        int diff =(model.getWorkAmt() - model.getPayAmt());
        Log.e("Diff","---------------------------------"+diff);
        Log.e("WORK","---------------------------------"+model.getWorkAmt());
        Log.e("Pay","---------------------------------"+model.getPayAmt());
        myViewHolder.tvPending.setText("Pending : "+diff+"/-");

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String json = gson.toJson(model);
//                Intent intent = new Intent(context, CustomerWiseDetailReportActivity.class);
//                Bundle args = new Bundle();
//                args.putString("model", json);
//                intent.putExtra("model", json);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);

                MainActivity activity = (MainActivity) context;
                Fragment adf = new CustDetailReportFragment();
                Bundle args = new Bundle();
                args.putString("model", json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "CustomerFragment").commit();


            }
        });

    }
    public  void updateList(ArrayList<Payment> list) {
        custReportList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return custReportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvMob,tvLocation,tvWorkAmt,tvPayAmt,tvUpperTankCost,tvLowertankCost,tvPending;
        private ImageView ivEdit;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvMob=itemView.findViewById(R.id.tvMob);
            tvLocation=itemView.findViewById(R.id.tvLocation);
            tvWorkAmt=itemView.findViewById(R.id.tvWorkerAmt);
            tvPayAmt=itemView.findViewById(R.id.tvPayAmt);
            tvUpperTankCost=itemView.findViewById(R.id.tvUpperTankCost);
            tvLowertankCost=itemView.findViewById(R.id.tvLowerTankCost);
            tvPending=itemView.findViewById(R.id.tvPending);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
