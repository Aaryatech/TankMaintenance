package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.Employee;

import java.util.ArrayList;

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.MyViewHolder> {
    private ArrayList<Employee> empList;
    private Context context;

    public EmpAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.allocated_emp_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpAdapter.MyViewHolder myViewHolder, int i) {
        final Employee model=empList.get(i);
        myViewHolder.tvEmp.setText(model.getUserName());
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmp=itemView.findViewById(R.id.tvEmp);
        }
    }
}
