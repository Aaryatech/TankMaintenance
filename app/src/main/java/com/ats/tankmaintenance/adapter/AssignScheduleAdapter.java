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

import static com.ats.tankmaintenance.fragment.AddScheduleWorkFragment.assignStaticList;

public class AssignScheduleAdapter extends RecyclerView.Adapter<AssignScheduleAdapter.MyViewHolder> {
    private ArrayList<Employee> empList;

    private Context context;

    public AssignScheduleAdapter(ArrayList<Employee> empList, Context context) {
        this.empList = empList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssignScheduleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assign_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AssignScheduleAdapter.MyViewHolder myViewHolder, int i) {
        final Employee model=empList.get(i);
        myViewHolder.tvEmp.setText(model.getUserName());

        Log.e("MODEL ASSIGN","----------------------"+model);

        if (model.getChecked()) {
            myViewHolder.checkBox.setChecked(true);
           /* if(assignStaticList!=null) {
                if (assignStaticList.isEmpty()) {
                    assignStaticList.add(model);
                } else{
                    for (int j = 0; j < assignStaticList.size(); j++) {
                        if ((assignStaticList.get(j).getUserId()!=(model.getUserId()))) {
                            assignStaticList.add(model);
                        }
                    }
                }
            }else{
                assignStaticList.add(model);
            }
            Log.e("CHECK List", "-----------------" + assignStaticList);*/

        } else {
            myViewHolder.checkBox.setChecked(false);
          //  assignStaticList.remove(model);
        }

        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("CHECK", "-----------------" + model);

                  //  myViewHolder.checkBox.setChecked(true);
                    model.setChecked(true);
                   /* if(assignStaticList!=null) {

                        if (assignStaticList.isEmpty()) {
                            assignStaticList.add(model);
                        } else {
                            for (int j = 0; j < assignStaticList.size(); j++) {
                                if (!(assignStaticList.get(j).getUserId().equals(model.getUserId()))) {
                                    assignStaticList.add(model);
                                }
                            }
                        }
                    }else {
                        assignStaticList.add(model);
                    }*/

                    Log.e("Model List", "-----------------" + model);
                    Log.e("CHECK List On", "-----------------" + assignStaticList);

                } else {
                    Log.e("UN CHECK", "-----------------" + model);

                  //  myViewHolder.checkBox.setChecked(false);
                    model.setChecked(false);
                  //  assignStaticList.remove(model);
                    Log.e("UN CHECK List", "-----------------" + assignStaticList);
                    Log.e("Model un check List", "-----------------" + model);

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
