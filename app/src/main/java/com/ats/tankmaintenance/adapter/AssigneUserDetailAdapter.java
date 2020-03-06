package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AssigneUserDetailAdapter extends RecyclerView.Adapter<AssigneUserDetailAdapter.MyViewHolder>  {
private ArrayList<User> assignedUserList;
private Context context;

    public AssigneUserDetailAdapter(ArrayList<User> userList, Context context) {
        assignedUserList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssigneUserDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_assigned_user_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssigneUserDetailAdapter.MyViewHolder myViewHolder, int i) {
        final User model = assignedUserList.get(i);
        myViewHolder.tvName.setText(model.getUserName());
        myViewHolder.tvMob.setText(model.getMobileNumber());
        myViewHolder.tvEmpDesig.setText(model.getDesignation());

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf1=new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date=sdf.parse(model.getDateOfBirth());
            String dt=sdf1.format(date);
            Log.e("Date ----",""+dt);
            myViewHolder.tvDate.setText(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return assignedUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvMob, tvDate,tvEmpDesig;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMob = itemView.findViewById(R.id.tvMob);
            tvEmpDesig = itemView.findViewById(R.id.tvEmpDesign);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
