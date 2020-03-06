package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.MonthReport;

import java.util.ArrayList;

public class MonthReportAdapter extends RecyclerView.Adapter<MonthReportAdapter.MyViewHolder>  {
private ArrayList<MonthReport> monthReportList;
private Context context;

    public MonthReportAdapter(ArrayList<MonthReport> monthReportList, Context context) {
        this.monthReportList = monthReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public MonthReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_month_report_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthReportAdapter.MyViewHolder myViewHolder, int i) {
        MonthReport model=monthReportList.get(i);
        myViewHolder.tvMonth.setText(model.getMonthName());
       // myViewHolder.tvDate.setText(model.getMonthDate());
        myViewHolder.tvYear.setText(model.getYear());
        myViewHolder.tvPayAmt.setText(""+model.getCostRs()+"/-");
        myViewHolder.tvTotal.setText(""+model.getTotalAmt()+"/-");
       // myViewHolder.tvDiscount.setText(""+model.getDiscAmt());

    }

    @Override
    public int getItemCount() {
        return monthReportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMonth,tvDate,tvYear,tvPayAmt,tvTotal,tvDiscount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth=itemView.findViewById(R.id.tvMonth);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvYear=itemView.findViewById(R.id.tvYear);
            tvPayAmt=itemView.findViewById(R.id.tvPayAmt);
            tvTotal=itemView.findViewById(R.id.tvTotal);
            tvDiscount=itemView.findViewById(R.id.tvDisc);
        }
    }
}
