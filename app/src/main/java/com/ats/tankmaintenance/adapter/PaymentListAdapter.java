package com.ats.tankmaintenance.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.activity.MainActivity;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.fragment.PaymentDetailFragment;
import com.ats.tankmaintenance.fragment.PaymentListFragment;
import com.ats.tankmaintenance.model.AddPayment;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.MyViewHolder>{
    private ArrayList<Payment> paymentList;
    private Context context;


    public PaymentListAdapter(ArrayList<Payment> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_payment_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentListAdapter.MyViewHolder myViewHolder, int i) {
            final Payment model=paymentList.get(i);
            myViewHolder.tvName.setText(model.getCustomerName());
            myViewHolder.tvMob.setText(model.getCustomerPhone());
            myViewHolder.tvLocation.setText(model.getCustomerAddress());
            myViewHolder.tvWorkAmt.setText(""+model.getWorkAmt());
            myViewHolder.tvPayAmt.setText(""+model.getPayAmt());
            myViewHolder.tvUpperTankCost.setText(""+model.getCostUppertankPerpieces());
            myViewHolder.tvLowertankCost.setText(""+model.getCostLowertankPerpieces());

            int diff =(model.getWorkAmt() - model.getPayAmt());
            Log.e("Diff","---------------------------------"+diff);
            Log.e("WORK","---------------------------------"+model.getWorkAmt());
            Log.e("Pay","---------------------------------"+model.getPayAmt());
            myViewHolder.tvPending.setText("Pending : "+diff+"/-");

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add_payment, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_addPayment) {
                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            MainActivity activity = (MainActivity) context;
                           new AddPaymentDialog(context,model).show();
                            //getData(model);

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

//                Intent intent = new Intent(context, PaymentDetailActivity.class);
//                Bundle args = new Bundle();
//                args.putString("model", json);
//                intent.putExtra("model", json);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);

                MainActivity activity = (MainActivity) context;
                Fragment adf = new PaymentDetailFragment();
                Bundle args = new Bundle();
                args.putString("model", json);
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "PaymentFragment").commit();

            }
        });
    }



    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvMob,tvLocation,tvWorkAmt,tvPayAmt,tvUpperTankCost,tvLowertankCost,tvPending;
        private ImageView ivEdit;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvMob=itemView.findViewById(R.id.tvMob);
            tvLocation=itemView.findViewById(R.id.tvLocation);
            tvWorkAmt=itemView.findViewById(R.id.tvWorkerAmt);
            tvPayAmt=itemView.findViewById(R.id.tvPayAmt);
            tvUpperTankCost=itemView.findViewById(R.id.tvUpperTankCost);
            tvLowertankCost=itemView.findViewById(R.id.tvLowerTankCost);
            tvPending=itemView.findViewById(R.id.tvPending);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    private class AddPaymentDialog extends Dialog {
        public Button btnCancel,btnSubmit;
        public EditText tvName,tvMob,tvLocation,tvPayAmt,tvWorkAmt,edAmount,edPending;

        Payment payment;

        public AddPaymentDialog( Context context,Payment payment) {
            super(context);
            this.payment=payment;
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

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<AddPayment> listCall = Constants.myInterface.savePayment(addPayment);
            listCall.enqueue(new Callback<AddPayment>() {
                @Override
                public void onResponse(Call<AddPayment> call, Response<AddPayment> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE PAYMENT ADAPTER : ", " ------------------------------SAVE PAYMENT------------------------- " + response.body());
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            MainActivity activity = (MainActivity) context;
                            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new PaymentListFragment(), "HomeFragment");
                            ft.commit();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("" + context.getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        builder.setTitle("" + context.getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("" + context.getResources().getString(R.string.app_name));
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
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }
}
