package com.ats.tankmaintenance.adapter;

import android.content.Context;
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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private ArrayList<User> UserList;
    private Context context;

    public UserListAdapter(ArrayList<User> userList, Context context) {
        UserList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapet_user_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder myViewHolder, int i) {
        final User model = UserList.get(i);
        Log.e("Mytag","model-----------------------"+model);

        myViewHolder.tvName.setText(model.getUserName());
        myViewHolder.tvDOB.setText("" + model.getDateOfBirth());
        // getTaxableValue
        myViewHolder.tvDesig.setText(""+model.getDesignation());

        if (i % 2 == 0) {
            myViewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            myViewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
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
