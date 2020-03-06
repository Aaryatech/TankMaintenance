package com.ats.tankmaintenance.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.AssigneUserDetailAdapter;
import com.ats.tankmaintenance.fragment.AddScheduleWorkFragment;
import com.ats.tankmaintenance.model.User;
import com.ats.tankmaintenance.model.WorkHistory;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ScheduleListDetailActivity extends AppCompatActivity implements View.OnClickListener {
WorkHistory workHistory;
private RecyclerView recyclerView;
private TextView tvDate,tvNextdate,tvWorkTime,tvName,tvAddress,tvContact,tvPerName,tvperContact,tvLowerTank,tvUpperTank,tvLowerTankCost,tvUpperTankCost,tvDiscount,tvTotal,tvRemark;
private Button btn_schedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Assigned Work");

        tvDate=(TextView)findViewById(R.id.tvDate);
        tvNextdate=(TextView)findViewById(R.id.tvNextDate);
        tvWorkTime=(TextView)findViewById(R.id.tvWorkTime);
        tvName=(TextView)findViewById(R.id.tvName);
        tvAddress=(TextView)findViewById(R.id.tvAddress);
        tvContact=(TextView)findViewById(R.id.tvContact);
        tvPerName=(TextView)findViewById(R.id.tvCustName);
        tvperContact=(TextView)findViewById(R.id.tvCustContacat);
        tvLowerTank=(TextView)findViewById(R.id.tvLowerTank);
        tvUpperTank=(TextView)findViewById(R.id.tvUpperTank);
        tvLowerTankCost=(TextView)findViewById(R.id.tvLowerTankCost);
        tvUpperTankCost=(TextView)findViewById(R.id.tvUpperTankCost);
        tvDiscount=(TextView)findViewById(R.id.tvDicount);
        tvTotal=(TextView)findViewById(R.id.tvTotalAmount);
        tvRemark=(TextView)findViewById(R.id.tvRemark);
        btn_schedule=(Button)findViewById(R.id.btn_schWork);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        String upcomingStr = getIntent().getStringExtra("model");
        Gson gson = new Gson();

        try {
            workHistory = gson.fromJson(upcomingStr, WorkHistory.class);
            Log.e("Schedule work responce", "-----------------------" + workHistory);

            tvDate.setText(workHistory.getWorkDate());
            tvNextdate.setText(workHistory.getNextDate());
            tvWorkTime.setText(workHistory.getWorkTime());
            tvName.setText(workHistory.getCustomerName());
            tvAddress.setText(workHistory.getCustomerAddress());
            tvContact.setText(workHistory.getCustomerPhone());
            tvPerName.setText(workHistory.getCustomerContactName());
            tvperContact.setText(workHistory.getCustomerContactNumber());
            tvRemark.setText(workHistory.getRemark());
            tvLowerTank.setText("" + workHistory.getNoOfLowerTank());
            tvUpperTank.setText("" + workHistory.getNoOfUpperTank());
            tvLowerTankCost.setText("" + workHistory.getAmtLowerTank());
            tvUpperTankCost.setText("" + workHistory.getAmtUpperTank());
            tvDiscount.setText("" + workHistory.getDiscAmt() + "%");
            tvTotal.setText("" + workHistory.getTotalAmt() + "/-");

            if (workHistory.getUser() != null) {
                ArrayList<User> detailList = new ArrayList<>();
                for (int i = 0; i < workHistory.getUser().size(); i++) {
                    detailList.add(workHistory.getUser().get(i));
                }

                AssigneUserDetailAdapter adapter = new AssigneUserDetailAdapter(detailList, getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        btn_schedule.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_schWork)
        {
            Gson gson = new Gson();
            String json = gson.toJson(workHistory);
            Fragment adf = new AddScheduleWorkFragment();
            Bundle args = new Bundle();
            args.putString("model", json);
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();

        }

    }
}
