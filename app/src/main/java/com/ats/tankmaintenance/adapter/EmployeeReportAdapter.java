package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.EmployeeReport;

import java.util.ArrayList;

public class EmployeeReportAdapter  extends RecyclerView.Adapter<EmployeeReportAdapter.MyViewHolder> {
private ArrayList<EmployeeReport> empList;
private Context context;

    public EmployeeReportAdapter(ArrayList<EmployeeReport> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_employee_report, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeReportAdapter.MyViewHolder myViewHolder, int i) {
        final EmployeeReport model = empList.get(i);

        myViewHolder.tvCustName.setText(model.getCustomerName());
        myViewHolder.tvNextDate.setText(model.getNextDate());
        myViewHolder.tvDate.setText(model.getWorkDate());
        myViewHolder.tvRemark.setText(model.getCustomerAddress());
        myViewHolder.tvUserName.setText(model.getUserName());
        myViewHolder.tvDOB.setText(""+model.getDateOfBirth());
        myViewHolder.tvDesig.setText(model.getDesignation());


        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItems.setVisibility(View.VISIBLE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_up));
        } else {
            myViewHolder.llItems.setVisibility(View.GONE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
        }

        myViewHolder.tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItems.setVisibility(View.VISIBLE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItems.setVisibility(View.GONE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustName, tvNextDate, tvDate, tvRemark, tvItems,tvUserName,tvDOB,tvDesig;
        public RecyclerView recyclerView;
        public CardView cardView;
        public ImageView imageView,ivEdit;
        public LinearLayout llItems;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNextDate = itemView.findViewById(R.id.tvNextDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            tvItems = itemView.findViewById(R.id.tvItems);
            llItems = itemView.findViewById(R.id.llItems);
            imageView = itemView.findViewById(R.id.imageView);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            tvUserName=itemView.findViewById(R.id.userName);
            tvDOB=itemView.findViewById(R.id.DOB);
            tvDesig=itemView.findViewById(R.id.Desig);
        }
    }
}
