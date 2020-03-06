package com.ats.tankmaintenance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.model.ScheduleList;
import com.google.gson.Gson;

public class ScheduleDetailActivity extends AppCompatActivity {
ScheduleList scheduleList;
public TextView tvCustName,tvCustAdd,tvCustContact,tvSchdate,tvEmpnName,tvLowerTank,tvUpperTank,tvAmtLowerTank,tvAmtUpperTank,tvAmount,tvDiscount,tvTotal,tvRemark,tvNextDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setTitle("Schedule Detail");

        tvCustName=(TextView)findViewById(R.id.tvCustName);
        tvCustAdd=(TextView)findViewById(R.id.tvCustAddress);
        tvCustContact=(TextView)findViewById(R.id.tvCustContact);
        tvSchdate=(TextView)findViewById(R.id.tvSchDate);
        tvEmpnName=(TextView)findViewById(R.id.tvEmpName);
        tvLowerTank=(TextView)findViewById(R.id.tvCntLowerTank);
        tvUpperTank=(TextView)findViewById(R.id.tvCntUpperTank);
        tvAmtLowerTank=(TextView)findViewById(R.id.tvAmtLowerTank);
        tvAmtUpperTank=(TextView)findViewById(R.id.tvAmtUpperTank);
        tvAmount=(TextView)findViewById(R.id.tvAmount);
        tvDiscount=(TextView)findViewById(R.id.tvDisAmt);
        tvTotal=(TextView)findViewById(R.id.tvTotalAmt);
        tvRemark=(TextView)findViewById(R.id.tvEmpRemark);
        tvNextDate=(TextView)findViewById(R.id.tvNextDate);

        String upcomingStr = getIntent().getStringExtra("model");
        Gson gson = new Gson();
        try {
            scheduleList = gson.fromJson(upcomingStr, ScheduleList.class);
            Log.e("responce", "-----------------------" + scheduleList);

            tvCustName.setText(scheduleList.getName());
            tvCustAdd.setText(scheduleList.getAddress());
            tvCustContact.setText(scheduleList.getContact());
            tvSchdate.setText(scheduleList.getDate());
            tvLowerTank.setText("" + scheduleList.getLowerTank());
            tvUpperTank.setText("" + scheduleList.getUpperTank());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
