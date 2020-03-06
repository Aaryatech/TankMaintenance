package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.DateReport;

import java.util.ArrayList;

public class DateReportAdapter extends RecyclerView.Adapter<DateReportAdapter.MyViewHolder> {
    private ArrayList<DateReport> dateReportList;
    private Context context;

    public DateReportAdapter(ArrayList<DateReport> dateReportList, Context context) {
        this.dateReportList = dateReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public DateReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_date_report_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DateReportAdapter.MyViewHolder myViewHolder, int i) {
        final DateReport model = dateReportList.get(i);
        Log.e("Date Report List","-------------------------------"+dateReportList);
        myViewHolder.tvPayDate.setText(model.getPaymentDate());
        myViewHolder.tvPayAmt.setText(model.getCostRs()+"/-");

    }

    @Override
    public int getItemCount() {
        return dateReportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPayDate, tvPayAmt;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPayDate = itemView.findViewById(R.id.tvPayDate);
            tvPayAmt = itemView.findViewById(R.id.tvPayAmt);

        }
    }
}
