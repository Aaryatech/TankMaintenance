package com.ats.tankmaintenance.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.PaymentDetailAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.model.PaymentDetail;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerWiseDetailReportActivity extends AppCompatActivity {
    Payment payment;
    private TextView tvName,tvMob,tvLocation,tvWorkAmt,tvPayAmt,tvUpperTankCost,tvLowertankCost,tvPending;
    int diff;
    ArrayList<PaymentDetail> PaymentDetailList = new ArrayList<>();
    PaymentDetailAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnAddPayment;

    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_wise_detail_report);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setTitle("Payment Detail");

        tvName=(TextView) findViewById(R.id.tvName);
        tvMob=(TextView) findViewById(R.id.tvMob);
        tvLocation=(TextView) findViewById(R.id.tvLocation);
        tvWorkAmt=(TextView) findViewById(R.id.tvWorkerAmt);
        tvPayAmt=(TextView) findViewById(R.id.tvPayAmt);
        tvPending=(TextView) findViewById(R.id.tvPending);

        btnAddPayment=(Button)findViewById(R.id.btnAddPayment);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vital/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String upcomingStr = getIntent().getStringExtra("model");
        Gson gson = new Gson();

        try {
            payment = gson.fromJson(upcomingStr, Payment.class);
            Log.e("responce", "-----------------------" + payment);

            tvName.setText(payment.getCustomerName());
            tvMob.setText(payment.getCustomerPhone());
            tvLocation.setText(payment.getCustomerAddress());
            tvWorkAmt.setText(""+payment.getWorkAmt());
            tvPayAmt.setText(""+payment.getPayAmt());
            diff= (payment.getWorkAmt() - payment.getPayAmt());
            tvPending.setText("Pending : "+diff+"/-");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getPaymentDetail(payment.getCustomerId());
    }
    private void getPaymentDetail(Integer customerId) {
        Log.e("PARAMETER","CUST_ID" +customerId);

        if (Constants.isOnline(getApplicationContext())) {
            final CommonDialog commonDialog = new CommonDialog(getApplicationContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<PaymentDetail>> listCall = Constants.myInterface.getTotalAmountByCustId(customerId);
            listCall.enqueue(new Callback<ArrayList<PaymentDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<PaymentDetail>> call, Response<ArrayList<PaymentDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PAYMENT LIST : ", " - " + response.body());
                            PaymentDetailList.clear();
                            PaymentDetailList = response.body();

                            adapter = new PaymentDetailAdapter(PaymentDetailList, getApplicationContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PaymentDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



}
