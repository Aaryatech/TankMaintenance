package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.Employee;

import java.util.ArrayList;

import static com.ats.tankmaintenance.fragment.AddWorkFragment.assignEmpStaticList;

public class AssignEmpAdapter extends RecyclerView.Adapter<AssignEmpAdapter.MyViewHolder> {
    private ArrayList<Employee> empList;
    private Context context;

    public AssignEmpAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssignEmpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assign_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AssignEmpAdapter.MyViewHolder myViewHolder, int i) {
            final Employee model=empList.get(i);
            myViewHolder.tvEmp.setText(model.getUserName());

//        if (model.getChecked()) {
//            myViewHolder.checkBox.setChecked(true);
//        } else {
//            myViewHolder.checkBox.setChecked(false);
//        }

        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                   // myViewHolder.checkBox.setChecked(true);
                    model.setChecked(true);
                   // assignEmpStaticList.add(model);
                    Log.e("CHECK List", "-----------------" + assignEmpStaticList);
                    Log.e("Model CHECK List", "-----------------" + model);

                } else {

                   // myViewHolder.checkBox.setChecked(false);
                    model.setChecked(false);
                   // assignEmpStaticList.remove(model);
                    Log.e("UN CHECK List", "-----------------" + assignEmpStaticList);
                    Log.e("MODEL UN CHECK List", "-----------------" + model);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmp;
        private CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmp=itemView.findViewById(R.id.tvEmp);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
}
