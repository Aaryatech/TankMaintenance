package com.ats.tankmaintenance.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.AreaListDialogAdapter;
import com.ats.tankmaintenance.adapter.AssignEmpAdapter;
import com.ats.tankmaintenance.adapter.CustListDialogAdapter;
import com.ats.tankmaintenance.adapter.EmpAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Customer;
import com.ats.tankmaintenance.model.CustomerLocationName;
import com.ats.tankmaintenance.model.Employee;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.model.Work;
import com.ats.tankmaintenance.utils.CommonDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddWorkFragment extends Fragment implements View.OnClickListener{
//private Spinner ;
private EditText edDate,edNextDate,edLowerTankCount,edUpperTankCount,edLowerTanlCost,edUpperTankCost,edDiscount,edTotal,edTime,edFrequency;
private RecyclerView recyclerView1;
private Button btnSubmit;
private TextView tvAssignEmployee,tvAreaId,spArea,spCustomer,tvCustId,tvDate;
long fromDateMillis, toDateMillis;
int yyyy, mm, dd,AreaId;
String empId="";
String strEmpId;
CheckBox cbAll;

private EmpAdapter empAdapter;
private AreaListDialogAdapter areaListAdapter;
private CustListDialogAdapter custListAdapter;

int locationId= 0,customerId = 0,freq;
float lowerCost,upperCost;

private AssignEmpAdapter mAdapter;
public RecyclerView recyclerView;
BroadcastReceiver mBroadcastReceiver;
Dialog dialog;
private ArrayList<Employee> empList = new ArrayList<>();

ArrayList<String> areaArray = new ArrayList<>();
ArrayList<String> custArray = new ArrayList<>();
public static ArrayList<Employee> assignEmpStaticList = new ArrayList<>();

ArrayList<String> locationNameList = new ArrayList<>();
ArrayList<Location> locationList = new ArrayList<>();
ArrayList<Integer> locationIdList = new ArrayList<>();

ArrayList<String> customerNameList = new ArrayList<>();
ArrayList<Integer> customerIdList = new ArrayList<>();
ArrayList<Customer> customerList = new ArrayList<>();
ArrayList<CustomerLocationName> custList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_schedule_master, container, false);

        getActivity().setTitle("Add New Work");

        spArea=view.findViewById(R.id.spArea);
        tvAreaId=view.findViewById(R.id.tvAreaId);
        tvCustId=view.findViewById(R.id.tvCustId);
        spCustomer=view.findViewById(R.id.spCustomer);
        edDate=view.findViewById(R.id.edDate);
        tvDate=view.findViewById(R.id.tvDate);
        edNextDate=view.findViewById(R.id.edNextDate);
        edLowerTankCount=view.findViewById(R.id.edLowerTank);
        edUpperTankCount=view.findViewById(R.id.edUpperTank);
        edLowerTanlCost=view.findViewById(R.id.edLowerTankCost);
        edUpperTankCost=view.findViewById(R.id.edUpperTankCost);
        edDiscount=view.findViewById(R.id.edDiscount);
        edTotal=view.findViewById(R.id.edTotal);
        edTime=view.findViewById(R.id.edTime);
        edFrequency=view.findViewById(R.id.edFrequency);

        btnSubmit=view.findViewById(R.id.btnSubmit);

        recyclerView1=view.findViewById(R.id.recyclerView);

        tvAssignEmployee=view.findViewById(R.id.tv_assignEmployee);

        getLocation();

        getAssignEmployee();

        spArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int yr, mn, dy;
                if (fromDateMillis > 0) {
                    Calendar purchaseCal = Calendar.getInstance();
                    purchaseCal.setTimeInMillis(fromDateMillis);
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                } else {
                    Calendar purchaseCal = Calendar.getInstance();
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(getContext(), fromDateListener, yr, mn, dy);
                dialog.show();
            }
        });

//        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    if(position!=0)
//                    {
//                        AreaId=locationIdList.get(position);
//                        Log.e("Id","-------"+AreaId);
//
//                        getCustomer(AreaId);
//
//                    }
//                }catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        spCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog1();
            }
        });

//        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    if(position!=0)
//                    {
//                        customerId = customerIdList.get(position);
//                        Log.e("CUST ID","--------------------------------"+customerId);
//                        if(customerList!=null) {
//
//                            Log.e("hiii","----------------");
//                            for (int j = 0; j < customerList.size(); j++) {
//
//                                Log.e("hiii1","----------------");
//                                if (customerList.get(j).getCustomerId() == customerId) {
//
//                                    Log.e("hiii2","----------------");
//                                    edLowerTankCount.setText(""+(int) customerList.get(j).getNoOfLowerTank());
//                                    edUpperTankCount.setText(""+(int) customerList.get(j).getNoOfUpperTank());
//                                    edFrequency.setText(""+(int) customerList.get(j).getFrequency());
//
//                                     lowerCost=(customerList.get(j).getCostLowertankPerpieces()*customerList.get(j).getNoOfLowerTank());
//                                    Log.e("hiii3","----------------"+lowerCost);
//
//                                     upperCost=(customerList.get(j).getCostUppertankPerpieces()*customerList.get(j).getNoOfUpperTank());
//                                    Log.e("hiii4","----------------"+upperCost);
//
//                                    edLowerTanlCost.setText(""+lowerCost);
//                                    edUpperTankCost.setText(""+upperCost);
//
//                                    int sum= (int) (lowerCost+upperCost);
//
//                                    edDiscount.setText("0");
//
//                                    edTotal.setText(""+sum);
//
//                                     freq= (int) customerList.get(j).getFrequency();
//                                   Log.e("freq","----------------"+freq);
//
//                                    Date todayDate = Calendar.getInstance().getTime();
//                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
//                                    String currentDate = formatter.format(todayDate);
//
//                                    fromDateMillis = todayDate.getTime();
//                                    toDateMillis = todayDate.getTime();
//                                    edDate.setText(currentDate);
//
//                                    String strDate=edDate.getText().toString();
//                                    Date date = null;
//
//                                    try {
//                                        date = formatter. parse(strDate);
//                                    }catch (Exception e)
//                                    {
//
//                                    }
//
//                                    Calendar dateCal = Calendar.getInstance();
//                                    dateCal. setTime(date);
//                                    dateCal.add(Calendar.MONTH, freq);
//                                    String fromdate = formatter.format(dateCal.getTime());//catch exception
//                                    edNextDate.setText(fromdate);
//                                    toDateMillis=dateCal.getTimeInMillis();
//                                }
//                            }
//                        }
//
//                    }
//                }catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


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
                    String discount = s.toString();
                    int dis = Integer.parseInt(discount);
                    Log.e("hiii5", "----------------" + s.toString());
                    int sum = (int) (lowerCost + upperCost);
                    Log.e("hiii6", "----------------" + sum);
                    int totalSum = sum - dis;
                    Log.e("hiii8", "----------------" + totalSum);
                    edTotal.setText("" + totalSum);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

//        Date todayDate = Calendar.getInstance().getTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
//        String currentDate = formatter.format(todayDate);
//
//        fromDateMillis = todayDate.getTime();
//        toDateMillis = todayDate.getTime();
//        edDate.setText(currentDate);
//
//        String strDate=edDate.getText().toString();
//        Date date = null;
//        try {
//            date = formatter. parse(strDate);
//        }catch (Exception e)
//        {
//
//        }
//
//        Calendar dateCal = Calendar.getInstance();
//        dateCal. setTime(date);
//        dateCal.add(Calendar.MONTH, freq);
//        String fromdate = formatter.format(dateCal.getTime());//catch exception
//        edNextDate.setText(fromdate);
//        toDateMillis=dateCal.getTimeInMillis();

        edNextDate.setOnClickListener(this);
        tvAssignEmployee.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        edTime.setOnClickListener(this);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("Broad cast data","---------------"+intent);
                if (intent.getAction().equals("CUSTOMER_DATA")) {
                    handlePushNotification(intent);

                }else if (intent.getAction().equals("CUST_DATA")) {
                    handlePushNotification1(intent);

                }
            }
        };

        return view;
    }

    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDate.setText(dd + "-" + mm + "-" + yyyy);
            tvDate.setText(yyyy + "-" + mm + "-" + dd);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();
        }
    };

    private void handlePushNotification(Intent intent) {
        Log.e("handlePushNotification", "------------------------------------**********");
        dialog.dismiss();
        String locationName = intent.getStringExtra("locationName");
        final int locationId = intent.getIntExtra("locationId", 0);
        Log.e("AREA NAME : ", " " + locationName);
        Log.e("AREA ID : ", " " + locationId);
        spArea.setText("" + locationName);
        tvAreaId.setText("" + locationId);

        getCustomer(locationId);

    }

    private void getCustomerAll() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CustomerLocationName>> listCall = Constants.myInterface.getAllCustomerList();
            listCall.enqueue(new Callback<ArrayList<CustomerLocationName>>() {
                @Override
                public void onResponse(Call<ArrayList<CustomerLocationName>> call, Response<ArrayList<CustomerLocationName>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CUSTOMER LIST : ", "-------------------------------CUSTOMER LIST-ALL-------------------------------" + response.body());

                            custList.clear();
                            customerList.clear();
                            custList = response.body();

                            for(int i=0;i<custList.size();i++)
                            {
                                Customer customer=new Customer(custList.get(i).getCustomerId(),custList.get(i).getCustomerName(),custList.get(i).getCustomerAddress(),custList.get(i).getCustomerPhone(),custList.get(i).getCustomerPhone2(),custList.get(i).getCustomerContactName(),custList.get(i).getCustomerContactNumber(),custList.get(i).getNoOfUpperTank(),custList.get(i).getNoOfLowerTank(),custList.get(i).getCostUppertankPerpieces(),custList.get(i).getCostLowertankPerpieces(),custList.get(i).getAreaId(),custList.get(i).getFrequency(),custList.get(i).getDelStatus(),custList.get(i).getIsUsed(),custList.get(i).getExInt1(),custList.get(i).getExInt2(),custList.get(i).getExInt3(),custList.get(i).getExVar1(),custList.get(i).getExVar2(),custList.get(i).getExVar3(),custList.get(i).getExFloat1(),"");
                                customerList.add(customer);
                            }

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
                public void onFailure(Call<ArrayList<CustomerLocationName>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification", "------------------------------------**********");
        dialog.dismiss();
        String custName = intent.getStringExtra("custName");
        customerId = intent.getIntExtra("custId", 0);
        Log.e("CUST NAME : ", " " + custName);
        Log.e("CUST ID : ", " " + customerId);
        spCustomer.setText("" + custName);
        tvCustId.setText("" + customerId);

        Log.e("CUST ID","--------------------------------"+customerId);
        if(customerList!=null) {

            Log.e("hiii","----------------");
            for (int j = 0; j < customerList.size(); j++) {

                Log.e("hiii1","----------------");
                if (customerList.get(j).getCustomerId() == customerId) {

                    Log.e("hiii2","----------------");
                    edLowerTankCount.setText(""+(int) customerList.get(j).getNoOfLowerTank());
                    edUpperTankCount.setText(""+(int) customerList.get(j).getNoOfUpperTank());
                    edFrequency.setText(""+(int) customerList.get(j).getFrequency());

                    lowerCost=(customerList.get(j).getCostLowertankPerpieces()*customerList.get(j).getNoOfLowerTank());
                    Log.e("hiii3","----------------"+lowerCost);

                    upperCost=(customerList.get(j).getCostUppertankPerpieces()*customerList.get(j).getNoOfUpperTank());
                    Log.e("hiii4","----------------"+upperCost);

                    edLowerTanlCost.setText(""+lowerCost);
                    edUpperTankCost.setText(""+upperCost);

                    int sum= (int) (lowerCost+upperCost);

                    edDiscount.setText("0");

                    edTotal.setText(""+sum);

                    freq= (int) customerList.get(j).getFrequency();
                    Log.e("freq","----------------"+freq);

                    Date todayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = formatter.format(todayDate);

                    fromDateMillis = todayDate.getTime();
                    toDateMillis = todayDate.getTime();
                    edDate.setText(currentDate);

                    String strDate=edDate.getText().toString();
                    Date date = null;

                    try {
                        date = formatter. parse(strDate);
                    }catch (Exception e)
                    {

                    }

                    Calendar dateCal = Calendar.getInstance();
                    dateCal. setTime(date);
                    dateCal.add(Calendar.MONTH, freq);
                    String fromdate = formatter.format(dateCal.getTime());//catch exception
                    edNextDate.setText(fromdate);
                    toDateMillis=dateCal.getTimeInMillis();
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("CUSTOMER_DATA"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("CUST_DATA"));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }

    private void showDialog() {

        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.area_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);
        CheckBox cbAll = dialog.findViewById(R.id.cbAll);

        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Log.e("Check","-----------------------------------------------------");
                    spArea.setText("All");
                    tvAreaId.setText(""+-1);
                    dialog.dismiss();
                    getCustomerAll();
                }

            }
        });

        areaListAdapter = new AreaListDialogAdapter(locationList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(areaListAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (areaListAdapter != null) {
                        filterArea(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterArea(String text) {
        ArrayList<Location> temp1 = new ArrayList();
        for (Location d : locationList) {
            if (d.getLocationName().toLowerCase().contains(text.toLowerCase()) ) {
                temp1.add(d);
            }
        }
        //update recyclerview
        areaListAdapter.updateList(temp1);
    }


    private void showDialog1() {

        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        custListAdapter = new CustListDialogAdapter(customerList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(custListAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (custListAdapter != null) {
                        filterCustomer(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterCustomer(String text) {
        ArrayList<Customer> temp1 = new ArrayList();
        for (Customer d : customerList) {
            if (d.getCustomerName().toLowerCase().contains(text.toLowerCase()) ) {
                temp1.add(d);
            }
        }
        //update recyclerview
        custListAdapter.updateList(temp1);
    }
    private void getCustomer(int areaId) {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Customer>> listCall = Constants.myInterface.getCustomerByLocationId(areaId);
            listCall.enqueue(new Callback<ArrayList<Customer>>() {
                @Override
                public void onResponse(Call<ArrayList<Customer>> call, Response<ArrayList<Customer>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CUSTOMER LIST : ", " -----------------------------------CUSTOMER LIST---------------------------- " + response.body());

                            customerNameList.clear();
                            customerIdList.clear();
                            customerList.clear();
                            customerList=response.body();

                            customerNameList.add("Select Customer");
                            customerIdList.add(0);

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    customerIdList.add(response.body().get(i).getCustomerId());
                                    customerNameList.add(response.body().get(i).getCustomerName());
                                }

                               // ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, customerNameList);
                              //  spCustomer.setAdapter(projectAdapter);

                            }
//                            locationId = locationIdList.get(spArea.getSelectedItemPosition());

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
                public void onFailure(Call<ArrayList<Customer>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
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
                            locationList.clear();
                            locationList=response.body();
                            locationNameList.add("Select Location");
                            locationIdList.add(0);

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    locationIdList.add(response.body().get(i).getLocationId());
                                    locationNameList.add(response.body().get(i).getLocationName());
                                }

                              //  ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationNameList);
                               // spArea.setAdapter(projectAdapter);

                            }
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


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.edNextDate)
        {
            int yr, mn, dy;

                Calendar dateCal = Calendar.getInstance();
                dateCal.setTimeInMillis(toDateMillis);
                yr = dateCal.get(Calendar.YEAR);
                mn = dateCal.get(Calendar.MONTH);
                dy = dateCal.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dialog = new DatePickerDialog(getContext(), nextDateListener, yr, mn, dy);
            dialog.show();

        }else if(v.getId()==R.id.tv_assignEmployee)
        {
            new EmployeeAssigneDialog(getContext()).show();

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
        else if(v.getId()==R.id.btnSubmit)
        {
            String strOverHeadTank,strOverHeadTankCost,strUndergrounTank,strUndergroundTankCost,strDiscount,strTotal,strDate,strNextDate,strTime,strFrequency,strAreaId;
            boolean isValidDiscount = false, isValidTotal = false, isValidDate = false, isValidNextDate = false,  isValidLowerTank = false, isValidLowerTankCost = false, isValidUpperTank = false,isValidUpperTankCost = false,isValidTime = false,isValidFrequency = false;

            strUndergrounTank=edLowerTankCount.getText().toString();
            strOverHeadTank=edUpperTankCount.getText().toString();
            strUndergroundTankCost=edLowerTanlCost.getText().toString();
            strOverHeadTankCost=edUpperTankCost.getText().toString();
            strDiscount=edDiscount.getText().toString();
            strTotal=edTotal.getText().toString();
            strDate=edDate.getText().toString();
            strNextDate=edNextDate.getText().toString();
            strTime=edTime.getText().toString();
            strFrequency=edFrequency.getText().toString();
            strAreaId=tvAreaId.getText().toString();

            float noUpperTank = 0,noUpperTankCost= 0,noLowerTank = 0,noLowerTankCost= 0,discountAmt= 0,totalAmt= 0,frequency= 0;

            try {
                 noUpperTank = Float.parseFloat(strOverHeadTank);
                 noUpperTankCost = Float.parseFloat(strOverHeadTankCost);
                 noLowerTank = Float.parseFloat(strUndergrounTank);
                 noLowerTankCost = Float.parseFloat(strUndergroundTankCost);
                 discountAmt = Float.parseFloat(strDiscount);
                 totalAmt = Float.parseFloat(strTotal);
                 frequency = Float.parseFloat(strFrequency);

                 //locationId = locationIdList.get(spArea.getSelectedItemPosition());
                 locationId = Integer.parseInt(strAreaId);

            }catch(Exception e)
            {
                e.printStackTrace();
            }

            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date toDate = null;
            try {
                toDate = formatter1.parse(strDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }
             String dateTo = null;
            try {
                 dateTo = formatter3.format(toDate);
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            Date toNext = null;
            try {
                toNext = formatter1.parse(strNextDate);//catch exception
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String dateNext= null;
            try {
                 dateNext = formatter3.format(toNext);
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            if (strOverHeadTank.isEmpty()) {
                edUpperTankCount.setError("required");
            } else {
                edUpperTankCount.setError(null);
                isValidLowerTank = true;
            }

            if (strFrequency.isEmpty()) {
                edFrequency.setError("required");
            } else {
                edFrequency.setError(null);
                isValidFrequency = true;
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

            if(isValidDiscount && isValidTotal  &&  isValidLowerTank && isValidLowerTankCost && isValidUpperTank && isValidUpperTankCost && isValidTime && isValidFrequency)
            {
                final Work work =new Work(0,customerId,strEmpId,dateTo,strTime,0,noLowerTank,noUpperTank,0,noLowerTankCost,noUpperTankCost,0,discountAmt,totalAmt,"",dateNext,0,frequency,"",1,1,locationId,0,0,null,null,null,"");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to save Work?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Work","--------------------------BIN-----------------------------------------"+work);
                        getSaveWork(work);

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

    private void getSaveWork(Work work) {
        Log.e("PARAMETER","---------------------------------------WORK--------------------------"+work);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Work> listCall = Constants.myInterface.saveWork(work);
            listCall.enqueue(new Callback<Work>() {
                @Override
                public void onResponse(Call<Work> call, Response<Work> response) {
                    try {
                        if (response.body() != null) {

                            assignEmpStaticList.clear();
                            Log.e("SAVE WORK : ", " ------------------------------SAVE WORK------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new AssigneWorkFragment(), "HomeFragment");
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

    private class EmployeeAssigneDialog extends Dialog {

        public Button btnCancel,btnSubmit;

        private EmpAdapter empAdapter;
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

            try {

            mAdapter = new AssignEmpAdapter(assignEmpStaticList, getActivity());
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
            //getAssignEmployee();

           // prepareData();

        }
    }

    private void getAssignEmployee(){
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
                            empList=response.body();

                            assignEmpStaticList.clear();
                            assignEmpStaticList = empList;

                            for (int i = 0; i < assignEmpStaticList.size(); i++) {
                                assignEmpStaticList.get(i).setChecked(false);
                            }

                            getAssignUser();

                            mAdapter = new AssignEmpAdapter(assignEmpStaticList, getActivity());
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

    private void getAssignUser() {

        ArrayList<Employee> assignedEmpArray = new ArrayList<>();

        if (assignEmpStaticList != null) {
            if (assignEmpStaticList.size() > 0) {
                for (int i = 0; i < assignEmpStaticList.size(); i++) {
                    if (assignEmpStaticList.get(i).getChecked()) {
                        assignedEmpArray.add(assignEmpStaticList.get(i));
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

            for (int i=0;i<assignedEmpArray.size();i++)
            {
                empId= empId+ ","+ String.valueOf(assignedEmpArray.get(i).getUserId());

            }
            Log.e("Id","--------------------------EMP ID--------------------------"+empId);

            strEmpId = empId.substring(1);
            Log.e("Id--","--------------------------EMP ID--------------------------"+strEmpId);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
