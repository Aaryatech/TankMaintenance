package com.ats.tankmaintenance.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.AssigneUserDetailAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.AddPayment;
import com.ats.tankmaintenance.model.Info;
import com.ats.tankmaintenance.model.User;
import com.ats.tankmaintenance.model.WorkHistory;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignedWorkDetailActivity extends AppCompatActivity implements View.OnClickListener {
    WorkHistory workHistory;
    private RecyclerView recyclerView;
    private EditText edNextDate;
    private TextView tvDate, tvNextdate, tvWorkTime, tvName, tvAddress, tvContact, tvPerName, tvperContact, tvLowerTank, tvUpperTank, tvLowerTankCost, tvUpperTankCost, tvDiscount, tvTotal, tvRemark;
    private Button btn_submit;
    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_work_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle("Assigned Work Detail");

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvNextdate = (TextView) findViewById(R.id.tvNextDate);
        tvWorkTime = (TextView) findViewById(R.id.tvWorkTime);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvContact = (TextView) findViewById(R.id.tvContact);
        tvPerName = (TextView) findViewById(R.id.tvCustName);
        tvperContact = (TextView) findViewById(R.id.tvCustContacat);
        tvLowerTank = (TextView) findViewById(R.id.tvLowerTank);
        tvUpperTank = (TextView) findViewById(R.id.tvUpperTank);
        tvLowerTankCost = (TextView) findViewById(R.id.tvLowerTankCost);
        tvUpperTankCost = (TextView) findViewById(R.id.tvUpperTankCost);
        tvDiscount = (TextView) findViewById(R.id.tvDicount);
        tvTotal = (TextView) findViewById(R.id.tvTotalAmount);
        tvRemark = (TextView) findViewById(R.id.tvRemark);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        edNextDate = (EditText) findViewById(R.id.edNextDate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        String upcomingStr = getIntent().getStringExtra("model");
        Gson gson = new Gson();
        try {

            workHistory = gson.fromJson(upcomingStr, WorkHistory.class);
            Log.e("Assigned work responce", "-----------------------" + workHistory);

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
                    String number = workHistory.getCustomerPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            });

            tvperContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = workHistory.getCustomerContactNumber();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
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
            tvDate.setText("Work Date :" + workDate);

            Date fromDate = null;
            try {
                fromDate = formatter.parse(workHistory.getNextDate());//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String nextDate = formatter1.format(fromDate);
            tvNextdate.setText("Next Date :" + nextDate);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        //--------------------------------NextDate logic--------------------------------

        //  Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

        Date work = null;
        Log.e("Work DATE - ", "-------------*************************------------------ " +workHistory.getWorkDate());
        try {
            work = formatter1.parse(workHistory.getWorkDate());//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("Work - ", "-------------*************************------------------ " + work.getTime());
        // toDateMillis = todayDate.getTime();
        toDateMillis = work.getTime();

        Log.e("Work FRQ - ", "-------------*************************------------------ " + workHistory.getCustomerFrequency());

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTimeInMillis(work.getTime());
        //dateCal. setTime(work);
        dateCal.add(Calendar.MONTH, workHistory.getCustomerFrequency());
        String fromdate = formatter2.format(dateCal.getTime());//catch exception
        edNextDate.setText(fromdate);

        toDateMillis = dateCal.getTimeInMillis();

        edNextDate.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edNextDate) {
            int yr, mn, dy;
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTimeInMillis(toDateMillis);
            yr = dateCal.get(Calendar.YEAR);
            mn = dateCal.get(Calendar.MONTH);
            dy = dateCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AssignedWorkDetailActivity.this, nextDateListener, yr, mn, dy);
            dialog.show();

        }
        if (v.getId() == R.id.btn_submit) {
            String strNextDate = edNextDate.getText().toString();
            final String dateTo;

            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date toDate = null;
            try {
                toDate = formatter1.parse(strNextDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateTo = formatter3.format(toDate);

            getUpdate(workHistory.getWorkId(), 1, dateTo);

        }
    }

    private void getUpdate(Integer workId, int status, String NextDate) {
        if (Constants.isOnline(getApplicationContext())) {
            final CommonDialog commonDialog = new CommonDialog(getApplicationContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateStatusWork(workId, status, NextDate);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, final Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE WORK: ", " - " + response.body());

                            if (!response.body().getError()) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                AddPayment addPayment=new AddPayment(0,sdf.format(System.currentTimeMillis()),workHistory.getCustomerId(),0,1,0,"","",0,0,0,"","","","",false);
                                getAddPayment(addPayment);

                            } else {
                                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                            }
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAddPayment(AddPayment addPayment) {
        Log.e("PARAMETER","---------------------------------------WORK--------------------------"+addPayment);

        if (Constants.isOnline(getApplicationContext())) {
            final CommonDialog commonDialog = new CommonDialog(getApplicationContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<AddPayment> listCall = Constants.myInterface.savePayment(addPayment);
            listCall.enqueue(new Callback<AddPayment>() {
                @Override
                public void onResponse(Call<AddPayment> call, final Response<AddPayment> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE NEW PAYMENT  : ", " ------------------------------SAVE NEW PAYMENT------------------------- " + response.body());
                            AlertDialog.Builder builder = new AlertDialog.Builder(AssignedWorkDetailActivity.this, R.style.AlertDialogTheme);
                            builder.setTitle("Confirmation");
                            builder.setMessage("Do you want to complete Work?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Toast.makeText(getApplicationContext(), response.body().getMsg() + " " + "successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("isassigne", "isassigne");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getApplicationContext().getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getApplicationContext().getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<AddPayment> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getApplicationContext().getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener nextDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edNextDate.setText(dd + "-" + mm + "-" + yyyy);
            // tvToDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();
        }
    };
}
