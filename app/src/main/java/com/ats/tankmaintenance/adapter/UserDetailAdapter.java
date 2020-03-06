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

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.MyViewHolder> {
    private ArrayList<User> UserList;
    private Context context;

    public UserDetailAdapter(ArrayList<User> userList, Context context) {
        UserList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_user_detail_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailAdapter.MyViewHolder myViewHolder, int i) {
            User model=UserList.get(i);
        Log.e("Model","------------------------------------------Bin------------------------------"+model);
//            myViewHolder.tvDate.setText(""+model.getDateOfBirth());
            myViewHolder.tvUserName.setText(model.getUserName());
            myViewHolder.tvContact.setText(""+model.getMobileNumber());
            myViewHolder.tvDesig.setText(model.getDesignation());

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
        return UserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate,tvUserName,tvContact,tvDesig;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvUserName=itemView.findViewById(R.id.tvUserName);
            tvContact=itemView.findViewById(R.id.tvContact);
            tvDesig=itemView.findViewById(R.id.tvDesig);
        }
    }
}
