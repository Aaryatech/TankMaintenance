package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.DateWorkReport;

import java.util.ArrayList;

public class DateWiseReportAdapter extends RecyclerView.Adapter<DateWiseReportAdapter.MyViewHolder> {
private ArrayList<DateWorkReport> dateReportList;
private Context context;

    public DateWiseReportAdapter(ArrayList<DateWorkReport> dateReportList, Context context) {
        this.dateReportList = dateReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public DateWiseReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_date_work_report, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DateWiseReportAdapter.MyViewHolder myViewHolder, int i) {
        final DateWorkReport model = dateReportList.get(i);
        myViewHolder.tvWorkDate.setText(model.getWorkDate());
        int workAmt=model.getTotalAmt()+model.getDiscAmt();
        myViewHolder.tvWorkAmt.setText(""+workAmt);

    }

    @Override
    public int getItemCount() {
        return dateReportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWorkDate, tvWorkAmt;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkDate = itemView.findViewById(R.id.tvWorkDate);
            tvWorkAmt = itemView.findViewById(R.id.tvWorkAmt);

        }
    }
}
