package com.ats.tankmaintenance.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.PaymentDetailAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.AddPayment;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.model.PaymentDetail;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailFragment extends Fragment implements View.OnClickListener{
    Payment payment;
    private TextView tvName,tvMob,tvLocation,tvWorkAmt,tvPayAmt,tvUpperTankCost,tvLowertankCost,tvPending;
    int diff;
    ArrayList<PaymentDetail> PaymentDetailList = new ArrayList<>();
    PaymentDetailAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnAddPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment_detail, container, false);

        getActivity().setTitle("Payment Detail");

        tvName=(TextView)view.findViewById(R.id.tvName);
        tvMob=(TextView) view.findViewById(R.id.tvMob);
        tvLocation=(TextView) view.findViewById(R.id.tvLocation);
        tvWorkAmt=(TextView) view.findViewById(R.id.tvWorkerAmt);
        tvPayAmt=(TextView) view.findViewById(R.id.tvPayAmt);
        tvPending=(TextView) view.findViewById(R.id.tvPending);

        btnAddPayment=(Button)view.findViewById(R.id.btnAddPayment);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        String upcomingStr = getArguments().getString("model");
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

        btnAddPayment.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        new AddPaymentDialog(getActivity()).show();
    }

    private void getPaymentDetail(Integer customerId) {
        Log.e("PARAMETER","CUST_ID" +customerId);

        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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

                            adapter = new PaymentDetailAdapter(PaymentDetailList, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddPaymentDialog extends Dialog {
        public Button btnCancel,btnSubmit;
        public EditText tvName,tvMob,tvLocation,tvPayAmt,tvWorkAmt,edAmount,edPending;

        public AddPaymentDialog( Context context) {
            super(context);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_add_payment);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            tvName=(EditText)findViewById(R.id.tvName);
            tvMob=(EditText)findViewById(R.id.tvMob);
            tvLocation=(EditText)findViewById(R.id.tvLocation);
            tvPayAmt=(EditText)findViewById(R.id.tvPayAmt);
            tvWorkAmt=(EditText)findViewById(R.id.tvWorkAmt);
            edAmount=(EditText)findViewById(R.id.edPayment);
            edAmount=(EditText)findViewById(R.id.edPayment);
            edPending=(EditText)findViewById(R.id.tvPending);

            tvName.setText(payment.getCustomerName());
            tvMob.setText(payment.getCustomerPhone());
            tvLocation.setText(payment.getCustomerAddress());
            tvPayAmt.setText(""+payment.getPayAmt());
            tvWorkAmt.setText(""+payment.getWorkAmt());

            int diff=(payment.getWorkAmt() - payment.getPayAmt());
            edPending.setText(""+diff);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strAmount;
                    boolean isAmount =false;

                    strAmount=edAmount.getText().toString();

                    if (strAmount.isEmpty()) {
                        edAmount.setError("required");
                    } else {
                        edAmount.setError(null);
                        isAmount = true;
                    }
                    float amount = 0;
                    try {
                        amount = Float.parseFloat(strAmount);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    if(isAmount)
                    {
                        AddPayment addPayment=new AddPayment(0,sdf.format(System.currentTimeMillis()),payment.getCustomerId(),amount,1,0,"","",0,0,0,"","","","",false);
                        getAddPayment(addPayment);
                        dismiss();
                    }

                }
            });


        }
    }

    private void getAddPayment(AddPayment addPayment) {
        Log.e("PARAMETER","---------------------------------------WORK--------------------------"+addPayment);

        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<AddPayment> listCall = Constants.myInterface.savePayment(addPayment);
            listCall.enqueue(new Callback<AddPayment>() {
                @Override
                public void onResponse(Call<AddPayment> call, Response<AddPayment> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE PAYMENT : ", " ------------------------------SAVE PAYMENT------------------------- " + response.body());
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new PaymentListFragment(), "HomeFragment");
                            ft.commit();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }
}
