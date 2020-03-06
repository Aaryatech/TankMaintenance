package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.activity.MainActivity;
import com.ats.tankmaintenance.fragment.AddScheduleWorkFragment;
import com.ats.tankmaintenance.fragment.ScheduleListDetailFragment;
import com.ats.tankmaintenance.model.User;
import com.ats.tankmaintenance.model.WorkHistory;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.MyViewHolder> {
    private ArrayList<WorkHistory> scheduleList;
    private Context context;

    public ScheduleListAdapter(ArrayList<WorkHistory> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_schedule_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleListAdapter.MyViewHolder myViewHolder, int i) {
        final WorkHistory model = scheduleList.get(i);

        myViewHolder.tvCustName.setText(model.getCustomerName());
        myViewHolder.tvRemark.setText(model.getCustomerAddress());
        myViewHolder.tvMob.setText(model.getCustomerPhone());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

        Date TODate = null;
        try {
            TODate = formatter.parse(model.getWorkDate());//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String workDate = formatter1.format(TODate);
        myViewHolder.tvDate.setText("Work Date :"+workDate);

        Date fromDate = null;
        try {
            fromDate = formatter.parse(model.getNextDate());//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String nextDate = formatter1.format(fromDate);
        myViewHolder.tvNextDate.setText("Next Date :"+nextDate);

        if (model.getUser() != null) {
            ArrayList<User> detailList = new ArrayList<>();
            for (int j = 0; j < model.getUser().size(); j++) {
                detailList.add(model.getUser().get(j));
            }

            EmpListAdapter adapter = new EmpListAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(mLayoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            myViewHolder.recyclerView.setAdapter(adapter);
        }
        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItems.setVisibility(View.VISIBLE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_up));
        } else {
            myViewHolder.llItems.setVisibility(View.GONE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
        }

        myViewHolder.tvMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=model.getCustomerPhone();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +number));
                context.startActivity(intent);
            }
        });

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

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

//                Intent intent = new Intent(context, ScheduleListDetailActivity.class);
//                Bundle args = new Bundle();
//                args.putString("model", json);
//                intent.putExtra("model", json);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);

                MainActivity activity = (MainActivity) context;
                Fragment adf = new ScheduleListDetailFragment();
                Bundle args = new Bundle();
                args.putString("model", json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ScheduleFragment").commit();

            }
        });

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add_schedule, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_addSchedule) {
                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            MainActivity activity = (MainActivity) context;
                            Fragment adf = new AddScheduleWorkFragment();
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
        public TextView tvCustName, tvNextDate, tvDate, tvRemark, tvItems,tvMob;
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
            tvMob=itemView.findViewById(R.id.tvMob);
        }
    }
}
