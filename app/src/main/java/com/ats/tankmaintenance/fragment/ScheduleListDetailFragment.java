package com.ats.tankmaintenance.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.AssigneUserDetailAdapter;
import com.ats.tankmaintenance.model.User;
import com.ats.tankmaintenance.model.WorkHistory;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleListDetailFragment extends Fragment implements View.OnClickListener {
    WorkHistory workHistory;
    private RecyclerView recyclerView;
    private TextView tvDate,tvNextdate,tvWorkTime,tvName,tvAddress,tvContact,tvPerName,tvperContact,tvLowerTank,tvUpperTank,tvLowerTankCost,tvUpperTankCost,tvDiscount,tvTotal,tvRemark;
    private Button btn_schedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_schedule_list_detail, container, false);

        getActivity().setTitle("Schedule Work Detail");

        tvDate=(TextView)view.findViewById(R.id.tvDate);
        tvNextdate=(TextView)view.findViewById(R.id.tvNextDate);
        tvWorkTime=(TextView)view.findViewById(R.id.tvWorkTime);
        tvName=(TextView)view.findViewById(R.id.tvName);
        tvAddress=(TextView)view.findViewById(R.id.tvAddress);
        tvContact=(TextView)view.findViewById(R.id.tvContact);
        tvPerName=(TextView)view.findViewById(R.id.tvCustName);
        tvperContact=(TextView)view.findViewById(R.id.tvCustContacat);
        tvLowerTank=(TextView)view.findViewById(R.id.tvLowerTank);
        tvUpperTank=(TextView)view.findViewById(R.id.tvUpperTank);
        tvLowerTankCost=(TextView)view.findViewById(R.id.tvLowerTankCost);
        tvUpperTankCost=(TextView)view.findViewById(R.id.tvUpperTankCost);
        tvDiscount=(TextView)view.findViewById(R.id.tvDicount);
        tvTotal=(TextView)view.findViewById(R.id.tvTotalAmount);
        tvRemark=(TextView)view.findViewById(R.id.tvRemark);
        btn_schedule=(Button)view.findViewById(R.id.btn_schWork);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        //String upcomingStr = getIntent().getStringExtra("model");
        String upcomingStr = getArguments().getString("model");
        Gson gson = new Gson();

        try {
            workHistory = gson.fromJson(upcomingStr, WorkHistory.class);
            Log.e("Schedule work responce", "-----------------------" + workHistory);

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

            tvContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number=workHistory.getCustomerPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" +number));
                    startActivity(intent);
                }
            });

            tvperContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number=workHistory.getCustomerContactNumber();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" +number));
                    startActivity(intent);
                }
            });

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

                AssigneUserDetailAdapter adapter = new AssigneUserDetailAdapter(detailList, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        btn_schedule.setOnClickListener(this);

        return view;
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
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "ScheduleListDetailFragment").commit();
        }
    }
}
