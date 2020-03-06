package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.User;

import java.util.ArrayList;

public class EmpListAdapter extends RecyclerView.Adapter<EmpListAdapter.MyViewHolder>  {
    private ArrayList<User> UserList;
    private Context context;

    public EmpListAdapter(ArrayList<User> userList, Context context) {
        UserList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmpListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_emp_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpListAdapter.MyViewHolder myViewHolder, int i) {
        final User model = UserList.get(i);
        Log.e("Mytag","model-----------------------"+model);
        Log.e("Mytag model","----------------------------List------------------------------------"+UserList);
        try {
            myViewHolder.tvName.setText("" + model.getUserName());
            myViewHolder.tvDOB.setText("" + model.getMobileNumber());
            // getTaxableValue
            myViewHolder.tvDesig.setText("" + model.getDesignation());

            if (i % 2 == 0) {
                myViewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                myViewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        myViewHolder.tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=model.getMobileNumber();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +number));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDOB, tvDesig;
        public LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDOB = itemView.findViewById(R.id.tvDOB);
            tvDesig = itemView.findViewById(R.id.tvDesig);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
