package com.ats.tankmaintenance.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.AssignScheduleAdapter;
import com.ats.tankmaintenance.adapter.EmpAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Employee;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.model.Work;
import com.ats.tankmaintenance.model.WorkHistory;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddScheduleWorkFragment extends Fragment implements View.OnClickListener {
    private EditText edDate, edNextDate, edLowerTankCount, edUpperTankCount, edLowerTanlCost, edUpperTankCost, edDiscount, edTotal, edTime, edLocation, edCustomer;
    public RecyclerView recyclerView1, recyclerView;
    private Button btnSubmit;
    private TextView tvAssignEmployee;
    int AreaId;
    WorkHistory model;
    String empId = "";
    String strEmpId;
    String strOverHeadTank, strOverHeadTankCost, strUndergrounTank, strUndergroundTankCost, strDiscount, strTotal, strDate, strNextDate, strTime, strCustomer, strLocation;
    float noUpperTank,noUpperTankCost,noLowerTank,noLowerTankCost,discountAmt,totalAmt;
    int location;
     String dateTo,dateNext;

    long  toDateMillis;
    int yyyy, mm, dd;
    float lowerCost,upperCost;

    private AssignScheduleAdapter mAdapter;
    private EmpAdapter empAdapter;
    private ArrayList<Employee> empList = new ArrayList<>();
    private ArrayList<WorkHistory> WorkList = new ArrayList<>();
    public static ArrayList<Employee> assignStaticList = new ArrayList<>();

    ArrayList<String> locationNameList = new ArrayList<>();
    ArrayList<Integer> locationIdList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_schedule_work, container, false);
        getActivity().setTitle("Schedule Work");
        edLocation = view.findViewById(R.id.edLocation);
        edCustomer = view.findViewById(R.id.edCustomer);
        edDate = view.findViewById(R.id.edDate);
        edNextDate = view.findViewById(R.id.edNextDate);
        edLowerTankCount = view.findViewById(R.id.edLowerTank);
        edUpperTankCount = view.findViewById(R.id.edUpperTank);
        edLowerTanlCost = view.findViewById(R.id.edLowerTankCost);
        edUpperTankCost = view.findViewById(R.id.edUpperTankCost);
        edDiscount = view.findViewById(R.id.edDiscount);
        edTotal = view.findViewById(R.id.edTotal);
        edTime = view.findViewById(R.id.edTime);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        recyclerView1 = view.findViewById(R.id.recyclerView);
        tvAssignEmployee = view.findViewById(R.id.tv_assignEmployee);

        String quoteStr = getArguments().getString("model");
        Gson gson = new Gson();

        try {
            model = gson.fromJson(quoteStr, WorkHistory.class);
            Log.e("Schedule Model","----------------------------"+model);

            edCustomer.setText(model.getCustomerName());
            edDate.setText(model.getWorkDate());
            edNextDate.setText(model.getNextDate());
            edLowerTankCount.setText("" + model.getNoOfLowerTank());
            edUpperTankCount.setText("" + model.getNoOfUpperTank());
            edLowerTanlCost.setText("" + model.getAmtLowerTank());
            edUpperTankCost.setText("" + model.getAmtUpperTank());
            edDiscount.setText("" + model.getDiscAmt());
            edTotal.setText("" + model.getTotalAmt());
            edTime.setText(model.getWorkTime());

            getAssignEmployee();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // WorkList.add(model);

        getLocation();

        edLowerTankCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String lowTank = s.toString();
                    int lowerTank = Integer.parseInt(lowTank);
                    Log.e("lowerTank1", "----------------" + lowerTank);

                    model.setNoOfLowerTank(lowerTank);

                    lowerCost = (model.getAmtLowerTank()*model.getNoOfLowerTank());
                    Log.e("loewrCoast1", "----------------" + lowerCost);

                    upperCost = (model.getNoOfUpperTank() * model.getAmtUpperTank());
                    Log.e("upperCost1", "----------------" + upperCost);

                    edLowerTanlCost.setText("" + lowerCost);
                    float sum =lowerCost+upperCost;
                    Log.e("sum1", "----------------" + sum);

                    float total = sum-model.getDiscAmt();

                    model.setTotalAmt((int) total);

                    Log.e("total1", "----------------" + total);

                    edTotal.setText("" + total);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edUpperTankCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String uppTank = s.toString();
                    int upperTank = Integer.parseInt(uppTank);

                    model.setNoOfUpperTank(upperTank);

                    lowerCost = (model.getNoOfLowerTank() * model.getAmtLowerTank());
                    Log.e("lowerCost2", "----------------" + lowerCost);

                    upperCost = (model.getNoOfUpperTank() * model.getAmtUpperTank());
                    Log.e("upperCost2", "----------------" + upperCost);

                    edUpperTankCost.setText("" +upperCost);

                    float sum = lowerCost+ upperCost;
                    Log.e("sum2", "----------------" + sum);

                    float total = sum - model.getDiscAmt();

                    model.setTotalAmt((int) total);

                    Log.e("total2", "----------------" + total);

                    edTotal.setText("" + total);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String dis = s.toString();
                    int discount = Integer.parseInt(dis);

                    model.setDiscAmt(discount);

                    lowerCost = (model.getNoOfLowerTank() * model.getAmtLowerTank());
                    Log.e("lowerCost2", "----------------" + lowerCost);

                    upperCost = (model.getNoOfUpperTank() * model.getAmtUpperTank());
                    Log.e("upperCost2", "----------------" + upperCost);

                    float sum = lowerCost+ upperCost;
                    Log.e("sum2", "----------------" + sum);

                    float total = sum - model.getDiscAmt();

                    model.setTotalAmt((int) total);

                    Log.e("total2", "----------------" + total);

                    edTotal.setText("" + total);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        tvAssignEmployee.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        edNextDate.setOnClickListener(this);
        edDate.setOnClickListener(this);
        edTime.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_assignEmployee) {
            new EmployeeAssigneDialog(getContext()).show();
        }else if(v.getId()==R.id.edDate)
        {
            int yr, mn, dy;
            if (toDateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(toDateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), nextDateListener, yr, mn, dy);
            dialog.show();

        }else if(v.getId()==R.id.edTime)
        {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    edTime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Start Time");
            mTimePicker.show();
        }
        else if (v.getId() == R.id.btnSubmit) {

            boolean isValidDiscount = false, isValidTotal = false, isValidDate = false, isValidNextDate = false, isValidLowerTank = false, isValidLowerTankCost = false, isValidUpperTank = false, isValidUpperTankCost = false, isValidTime = false, isValidCustomer = false, isValidLocation = false;

            strUndergrounTank = edLowerTankCount.getText().toString();
            strOverHeadTank = edUpperTankCount.getText().toString();
            strUndergroundTankCost = edLowerTanlCost.getText().toString();
            strOverHeadTankCost = edUpperTankCost.getText().toString();
            strDiscount = edDiscount.getText().toString();
            strTotal = edTotal.getText().toString();
            strDate = edDate.getText().toString();
            strNextDate = edNextDate.getText().toString();
            strTime = edTime.getText().toString();
            strCustomer = edCustomer.getText().toString();
            strLocation = edLocation.getText().toString();

            try {
                noUpperTank = Float.parseFloat(strOverHeadTank);
                noUpperTankCost = Float.parseFloat(strOverHeadTankCost);
                noLowerTank = Float.parseFloat(strUndergrounTank);
                noLowerTankCost = Float.parseFloat(strUndergroundTankCost);
                discountAmt = Float.parseFloat(strDiscount);
                totalAmt = Float.parseFloat(strTotal);
                location= Integer.parseInt(strLocation);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            // int locationId = locationIdList.get(spArea.getSelectedItemPosition());
            // int customerId = customerIdList.get(spCustomer.getSelectedItemPosition());

            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date toDate = null;
            try {
                toDate = formatter1.parse(strDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateTo = formatter3.format(toDate);

            Date toNext = null;
            try {
                toNext = formatter1.parse(strNextDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }

             dateNext = formatter3.format(toNext);

            if (strOverHeadTank.isEmpty()) {
                edUpperTankCount.setError("required");
            } else {
                edUpperTankCount.setError(null);
                isValidLowerTank = true;
            }
            if (strCustomer.isEmpty()) {
                edCustomer.setError("required");
            } else {
                edCustomer.setError(null);
                isValidCustomer = true;
            }
            if (strLocation.isEmpty()) {
                edLocation.setError("required");
            } else {
                edLocation.setError(null);
                isValidLocation = true;
            }
            if (strOverHeadTankCost.isEmpty()) {
                edUpperTankCost.setError("required");
            } else {
                edUpperTankCost.setError(null);
                isValidLowerTankCost = true;
            }
            if (strUndergrounTank.isEmpty()) {
                edLowerTankCount.setError("required");
            } else {
                edLowerTankCount.setError(null);
                isValidUpperTank = true;
            }
            if (strUndergroundTankCost.isEmpty()) {
                edLowerTanlCost.setError("required");
            } else {
                edLowerTanlCost.setError(null);
                isValidUpperTankCost = true;
            }
            if (strDiscount.isEmpty()) {
                edDiscount.setError("required");
            } else {
                edDiscount.setError(null);
                isValidDiscount = true;
            }
            if (strTime.isEmpty()) {
                edTime.setError("required");
            } else {
                edTime.setError(null);
                isValidTime = true;
            }
            if (strTotal.isEmpty()) {
                edTotal.setError("required");
            } else {
                edTotal.setError(null);
                isValidTotal = true;
            }

            if (isValidDiscount && isValidTotal && isValidLowerTank && isValidLowerTankCost && isValidUpperTank && isValidUpperTankCost && isValidTime && isValidCustomer && isValidLocation ) {
                final Work work1 = new Work(model.getWorkId(), model.getCustomerId(), model.getEmployeeId(), model.getWorkDate(), model.getWorkTime(), model.getSequenceNumber(), model.getNoOfLowerTank(), model.getNoOfUpperTank(), model.getNoOfHrSpend(), model.getAmtLowerTank(), model.getAmtUpperTank(), model.getFinalAmt(), model.getDiscAmt(), model.getTotalAmt(), model.getRemark(), model.getNextDate(), 2, model.getCustomerFrequency(), model.getBillNumber(), model.getDelStatus(), model.getIsUsed(), model.getExInt1(), 0, 0, null, null, null, "");
               // final Work work1 = new Work(model.getWorkId(), model.getCustomerId(), strEmpId, dateTo, strTime, model.getSequenceNumber(), noLowerTank, noUpperTank, model.getNoOfHrSpend(), noLowerTankCost, noUpperTankCost, model.getFinalAmt(), discountAmt, totalAmt, model.getRemark(), dateNext, 2, model.getCustomerFrequency(), model.getBillNumber(), model.getDelStatus(), model.getIsUsed(), 0, 0, 0, null, null, null, "");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to Schedule Work?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getSaveScheduleWork(work1);

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
            }
        }

    }

    DatePickerDialog.OnDateSetListener nextDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDate.setText(dd + "-" + mm + "-" + yyyy);
            // tvToDate.setText(yyyy + "-" + mm + "-" + dd);
            Date todayDate = Calendar.getInstance().getTime();
            toDateMillis = todayDate.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();
        }
    };

    private void getSaveScheduleWork(Work work1) {
        Log.e("PARAMETER", "---------------------------------------WORK1--------------------------" + work1);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Work> listCall = Constants.myInterface.saveWork(work1);
            listCall.enqueue(new Callback<Work>() {
                @Override
                public void onResponse(Call<Work> call, Response<Work> response) {
                    try {
                        if (response.body() != null) {

                            assignStaticList.clear();
                            Log.e("RESECHEDULE  WORK : ", " ------------------------------RESECHEDULE WORK------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            final Work work =new Work(0,model.getCustomerId(),strEmpId,dateTo,strTime,0,noLowerTank,noUpperTank,0,noLowerTankCost,noUpperTankCost,0,discountAmt,totalAmt,"",dateNext,0,0,"",1,1,location,0,0,null,null,null,"");
                            getSaveWork(work);

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
                public void onFailure(Call<Work> call, Throwable t) {
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
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Location>> listCall = Constants.myInterface.getLocationList();
            listCall.enqueue(new Callback<ArrayList<Location>>() {
                @Override
                public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("LOCATION LIST : ", " -----------------------------------LOCATION LIST---------------------------- " + response.body());

                            locationNameList.clear();
                            locationIdList.clear();

                            locationNameList.add("Select Location");
                            locationIdList.add(0);

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    locationIdList.add(response.body().get(i).getLocationId());
                                    locationNameList.add(response.body().get(i).getLocationName());
                                }

                                for (int j = 0; j < locationIdList.size(); j++) {
                                    if (locationIdList.get(j)==  model.getExInt1()) {
                                        Log.e("Location111","-------------------");
                                        edLocation.setText(locationNameList.get(j));
                                    }
                                }

                            }
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception1 : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Location>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private void getSaveWork(Work work) {
        Log.e("PARAMETER", "---------------------------------------WORK--------------------------" + work);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Work> listCall = Constants.myInterface.saveWork(work);
            listCall.enqueue(new Callback<Work>() {
                @Override
                public void onResponse(Call<Work> call, Response<Work> response) {
                    try {
                        if (response.body() != null) {

                            assignStaticList.clear();
                            Log.e("RESECHEDULE NEW WORK : ", " ------------------------------RESECHEDULE WORK------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new ScheduleListFragment(), "ScheduleFragment");
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
                public void onFailure(Call<Work> call, Throwable t) {
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
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private class EmployeeAssigneDialog extends Dialog {

        public Button btnCancel, btnSubmit;
//        private AssignEmpAdapter mAdapter;
//        private ArrayList<AssignEmpList> empList = new ArrayList<>();

        public EmployeeAssigneDialog(@NonNull Context context) {
            super(context);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_assigne_employee);
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
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            //getAssignEmployee();
            try {

                mAdapter = new AssignScheduleAdapter(assignStaticList, getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                    getAssignUser();


                }
            });

            // prepareData();
        }
    }

    private void getAssignUser() {

        Log.e("Static List", "--------------------------Static--------------------------" + assignStaticList);

        ArrayList<Employee> assignedEmpArray = new ArrayList<>();

        if (assignStaticList != null) {
            if (assignStaticList.size() > 0) {
                for (int i = 0; i < assignStaticList.size(); i++) {
                    if (assignStaticList.get(i).getChecked()) {
                        assignedEmpArray.add(assignStaticList.get(i));
                    }
                }
            }

            empAdapter = new EmpAdapter(assignedEmpArray, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView1.setLayoutManager(mLayoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(empAdapter);
        }

        try {

            for (int i = 0; i < assignedEmpArray.size(); i++) {
                empId = empId + "," + String.valueOf(assignedEmpArray.get(i).getUserId());

            }
            Log.e("Id", "--------------------------EMP ID--------------------------" + empId);

            strEmpId = empId.substring(1);
            Log.e("Id--", "--------------------------EMP ID--------------------------" + strEmpId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAssignEmployee() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Employee>> listCall = Constants.myInterface.getAllUserList();
            listCall.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGN WORK LIST : ", " -----------------------------------ASSIGN WORK LIST---------------------------- " + response.body());

                            empList.clear();
                            empList = response.body();

                            assignStaticList.clear();
                            assignStaticList = empList;

                            for (int i = 0; i < assignStaticList.size(); i++) {
                                assignStaticList.get(i).setChecked(false);
                            }

                            String strEmpId="";
                            if (model != null) {
                                strEmpId = model.getEmployeeId();
                            }

                            List<String> list = Arrays.asList(strEmpId.split("\\s*,\\s*"));

                            Log.e("LIST", "----------------------" + list);

                            // assignStaticList.clear();


                            getAssignUser();
                            Log.e("BIN", "---------------------------------Model-----------------" + empList);
                            for (int j = 0; j < assignStaticList.size(); j++) {

                                for (int k = 0; k < list.size(); k++) {

                                    if (assignStaticList.get(j).getUserId() == Integer.parseInt(list.get(k))) {

                                        assignStaticList.get(j).setChecked(true);
                                        // assignStaticList.add(empList.get(j));

                                    }
                                }
                            }
                            Log.e("ADD SCH WORK FRG", "-------------------STATIC LIST : ---------------------------- " + assignStaticList);

                            mAdapter = new AssignScheduleAdapter(assignStaticList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);


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
                public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
