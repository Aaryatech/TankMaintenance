package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.activity.MainActivity;
import com.ats.tankmaintenance.activity.ScheduleDetailActivity;
import com.ats.tankmaintenance.fragment.AddWorkFragment;
import com.ats.tankmaintenance.model.ScheduleList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {
    private ArrayList<ScheduleList> scheduleList;
    private Context context;

    public ScheduleAdapter(ArrayList<ScheduleList> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.MyViewHolder myViewHolder, int i) {
        final ScheduleList model=scheduleList.get(i);
        myViewHolder.tvCustName.setText(model.getName());
        myViewHolder.tvAddress.setText(model.getAddress());
        myViewHolder.tvMob.setText(model.getContact());
        myViewHolder.tvDate.setText(model.getDate());
        myViewHolder.tvLowerTank.setText(model.getLowerTank());
        myViewHolder.tvUpperTank.setText(model.getUpperTank());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                Intent intent = new Intent(context, ScheduleDetailActivity.class);
                Bundle args = new Bundle();
                args.putString("model", json);
                intent.putExtra("model", json);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_edit_list, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {
                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            MainActivity activity = (MainActivity) context;
                            Fragment adf = new AddWorkFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ScheduleFragment").commit();


                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustName,tvAddress,tvMob,tvDate,tvLowerTank,tvUpperTank;
        private CardView cardView;
        private ImageView ivEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName=itemView.findViewById(R.id.tvCustName);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            tvMob=itemView.findViewById(R.id.tvMob);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvLowerTank=itemView.findViewById(R.id.tvLowerTank);
            tvUpperTank=itemView.findViewById(R.id.tvUpperTank);
            cardView=itemView.findViewById(R.id.cardView);
            ivEdit=itemView.findViewById(R.id.ivEdit);
        }
    }
}
