package com.ats.tankmaintenance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.UserDetailAdapter;
import com.ats.tankmaintenance.model.User;
import com.ats.tankmaintenance.model.WorkHistory;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkHistoryDetailActivity extends AppCompatActivity {
WorkHistory workHistory;
private RecyclerView recyclerView;
private TextView tvDate,tvNextdate,tvWorkTime,tvName,tvAddress,tvContact,tvPerName,tvperContact,tvLowerTank,tvUpperTank,tvLowerTankCost,tvUpperTankCost,tvDiscount,tvTotal,tvRemark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_history_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Work History Detail");

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

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        String upcomingStr = getIntent().getStringExtra("model");
        Gson gson = new Gson();

        try {
            workHistory = gson.fromJson(upcomingStr, WorkHistory.class);
            Log.e("responce", "-----------------------" + workHistory);

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

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date TODate = null;
            try {
                TODate = formatter.parse(workHistory.getWorkDate());//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String workDate = formatter1.format(TODate);
            tvDate.setText("Work Date :"+workDate);


            Date fromDate = null;
            try {
                fromDate = formatter.parse(workHistory.getNextDate());//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String nextDate = formatter1.format(fromDate);
            tvNextdate.setText("Next Date :"+nextDate);

            if (workHistory.getUser() != null) {
                ArrayList<User> detailList = new ArrayList<>();
                for (int i = 0; i < workHistory.getUser().size(); i++) {
                    detailList.add(workHistory.getUser().get(i));
                }

                UserDetailAdapter adapter = new UserDetailAdapter(detailList, getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
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
