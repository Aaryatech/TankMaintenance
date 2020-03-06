package com.ats.tankmaintenance.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.AreaListDialogAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Customer;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.ats.tankmaintenance.utils.PermissionsUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomerFragment extends Fragment implements View.OnClickListener {
//private Spinner spArea;
private TextView tvAreaId,spArea;
private EditText edName,edMob,edAddress,edContPerName,edContPerMob,edNextCleaning,edOverHeadTank,edUndergrounTank,edOverHeadTankCost,edUndergrounTankCost;
private Button btnSubmit;
Dialog dialog;
ArrayList<String>areaArray = new ArrayList<>();
    AreaListDialogAdapter areaListAdapter;
ArrayList<String> locationNameList = new ArrayList<>();
ArrayList<Location> locationList = new ArrayList<>();
ArrayList<Integer> locationIdList = new ArrayList<>();
BroadcastReceiver mBroadcastReceiver;

private final int REQUEST_CODE=99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_customer, container, false);
        spArea=(TextView) view.findViewById(R.id.spArea);
        tvAreaId=(TextView) view.findViewById(R.id.tvAreaId);
        edName=(EditText)view.findViewById(R.id.edCustomerName);
        edMob=(EditText)view.findViewById(R.id.edMob);
        edAddress=(EditText)view.findViewById(R.id.edAddress);
        edContPerName=(EditText)view.findViewById(R.id.edContactPerName);
        edContPerMob=(EditText)view.findViewById(R.id.edContPerMob);
        edNextCleaning=(EditText)view.findViewById(R.id.edNextCleaning);
        edOverHeadTank=(EditText)view.findViewById(R.id.edOverHeadCount);
        edOverHeadTankCost=(EditText)view.findViewById(R.id.edOverHeadCost);
        edUndergrounTank=(EditText)view.findViewById(R.id.edUnderGroundTankCount);
        edUndergrounTankCost=(EditText)view.findViewById(R.id.edUnderGroundTankCost);

        if (PermissionsUtil.checkAndRequestPermissions(getActivity())) {
        }

        btnSubmit=(Button)view.findViewById(R.id.btn_submit);

        getLocation();

        btnSubmit.setOnClickListener(this);
        edMob.setOnClickListener(this);
        edContPerMob.setOnClickListener(this);
        spArea.setOnClickListener(this);


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("Broad cast data","---------------"+intent);
                if (intent.getAction().equals("CUSTOMER_DATA")) {
                    handlePushNotification(intent);

                }
            }
        };

        return view;
    }

    private void handlePushNotification(Intent intent) {
        Log.e("handlePushNotification", "------------------------------------**********");
        dialog.dismiss();
        String locationName = intent.getStringExtra("locationName");
        int locationId = intent.getIntExtra("locationId", 0);
        Log.e("AREA NAME : ", " " + locationName);
        Log.e("AREA ID : ", " " + locationId);
        spArea.setText("" + locationName);
        tvAreaId.setText("" + locationId);

    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("CUSTOMER_DATA"));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
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
                            locationList.clear();
                            locationIdList.clear();

                            locationList=response.body();
                            locationNameList.add("Select Location");
                            locationIdList.add(0);

                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    locationIdList.add(response.body().get(i).getLocationId());
                                    locationNameList.add(response.body().get(i).getLocationName());
                                }

                              //  ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationNameList);
                             //   spArea.setAdapter(projectAdapter);
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

        if(v.getId()==R.id.edMob)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }if(v.getId()==R.id.edContPerMob)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 100);
        }else if(v.getId()==R.id.spArea)
        {
            showDialog();

        }
        else if(v.getId()==R.id.btn_submit)
        {
            String strName,strMob,strAddress,strContPerName,strContPerMob,strNextCleaning,strOverHeadTank,strOverHeadTankCost,strUndergrounTank,strUndergroundTankCost;
            boolean isValidArea=false, isValidName = false, isValidMob = false, isValidAddress = false, isValidPerName = false, isValidPerMob = false,isValidNextCleaning = false, isValidLowerTank = false, isValidLowerTankCost = false, isValidUpperTank = false,isValidUpperTankCost = false;
            strName=edName.getText().toString();
            strMob=edMob.getText().toString();
            strAddress=edAddress.getText().toString();
            strContPerName=edContPerName.getText().toString();
            strContPerMob=edContPerMob.getText().toString();
            strNextCleaning=edNextCleaning.getText().toString();
            strOverHeadTank=edOverHeadTank.getText().toString();
            strOverHeadTankCost=edOverHeadTankCost.getText().toString();
            strUndergrounTank=edUndergrounTank.getText().toString();
            strUndergroundTankCost=edUndergrounTankCost.getText().toString();
           // String locationName = locationNameList.get(spArea.getSelectedItemPosition());
            String locId =tvAreaId.getText().toString();
            String locName =spArea.getText().toString();

            int locationId=0,nextFrequency=0;
            float noUpperTank = 0,noUpperTankCost= 0,noLowerTank= 0,noLowerTankCost= 0,nextCleaning= 0;
            try {
                 //locationId = locationIdList.get(spArea.getSelectedItemPosition());
                 locationId = Integer.parseInt(locId);
                 noUpperTank = Float.parseFloat(strOverHeadTank);
                 noUpperTankCost = Float.parseFloat(strOverHeadTankCost);
                 noLowerTank = Float.parseFloat(strUndergrounTank);
                 noLowerTankCost = Float.parseFloat(strUndergroundTankCost);
                 nextCleaning = Float.parseFloat(strNextCleaning);
                nextFrequency= Integer.parseInt(strNextCleaning);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
//            if (locationId == 0) {
//                TextView viewProj = (TextView) spArea.getSelectedView();
//                viewProj.setError("required");
//            }else{
//                TextView viewProj = (TextView) spArea.getSelectedView();
//                viewProj.setError(null);
//            }
            if (locName.isEmpty()) {
                spArea.setError("required");
            } else {
                spArea.setError(null);
                isValidArea = true;
            }

            if (strName.isEmpty()) {
                edName.setError("required");
            } else {
                edName.setError(null);
                isValidName = true;
            }
            if (strMob.isEmpty()) {
                edMob.setError("required");
            }
//            else if (strMob.length() != 10) {
//                edMob.setError("required 10 digits");
//            }
            else if (strMob.equalsIgnoreCase("0000000000")) {
                edMob.setError("invalid number");
            } else {
                edMob.setError(null);
                isValidMob = true;
            }
            if (strAddress.isEmpty()) {
                edAddress.setError("required");
            } else {
                edAddress.setError(null);
                isValidAddress = true;
            }
            if (strContPerName.isEmpty()) {
                edContPerName.setError("required");
            } else {
                edContPerName.setError(null);
                isValidPerName = true;
            }
            if (strContPerMob.isEmpty()) {
                edContPerMob.setError("required");
            }
//            else if (strContPerMob.length() != 10) {
//                edContPerMob.setError("required 10 digits");
//            }
            else if (strContPerMob.equalsIgnoreCase("0000000000")) {
                edContPerMob.setError("invalid number");
            } else {
                edContPerMob.setError(null);
                isValidPerMob = true;
            }
            if (strNextCleaning.isEmpty()) {
                edNextCleaning.setError("required");
            }
            else if (nextFrequency <=0) {
                edNextCleaning.setError("invalid number");
            }
            else {
                edNextCleaning.setError(null);
                isValidNextCleaning = true;
            }
            if (strOverHeadTank.isEmpty()) {
                edOverHeadTank.setError("required");
            } else {
                edOverHeadTank.setError(null);
                isValidLowerTank = true;
            }
            if (strOverHeadTankCost.isEmpty()) {
                edOverHeadTankCost.setError("required");
            } else {
                edOverHeadTankCost.setError(null);
                isValidLowerTankCost = true;
            }
            if (strUndergrounTank.isEmpty()) {
                edUndergrounTank.setError("required");
            } else {
                edUndergrounTank.setError(null);
                isValidUpperTank = true;
            }
            if (strUndergroundTankCost.isEmpty()) {
                edUndergrounTankCost.setError("required");
            } else {
                edUndergrounTankCost.setError(null);
                isValidUpperTankCost = true;
            }

            if(isValidArea && isValidName && isValidMob && isValidAddress && isValidPerName && isValidPerMob && isValidNextCleaning && isValidLowerTank && isValidLowerTankCost && isValidUpperTank && isValidUpperTankCost)
            {
                final Customer customer = new Customer(0,strName,strAddress,strMob,"",strContPerName,strContPerMob,noUpperTank,noLowerTank,noUpperTankCost,noLowerTankCost,locationId,nextCleaning,1,1,0,0,0,"","","",0,"");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to save Customer?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getSaveCustomer(customer);

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

    private void showDialog() {

        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dialog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

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
                        filterCustomer1(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterCustomer1(String text) {
        ArrayList<Location> temp1 = new ArrayList();
        for (Location d : locationList) {
            if (d.getLocationName().toLowerCase().contains(text.toLowerCase()) ) {
                temp1.add(d);
            }
        }
        //update recyclerview
        areaListAdapter.updateList(temp1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor c = contentResolver.query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String contactNumberName = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                String contactNumber = num.replaceAll("\\s+","");
                                // Toast.makeText(getContext(), "Number="+num, Toast.LENGTH_LONG).show();
                                edMob.setText(contactNumber);
                                edContPerMob.setText(contactNumber);
                                edName.setText(contactNumberName);
                                edContPerName.setText(contactNumberName);
                            }
                        }
                    }
                    break;
                }

            case (100):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor c = contentResolver.query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String contactNumberName = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                String contactNumber = num.replaceAll("\\s+","");
                                // Toast.makeText(getContext(), "Number="+num, Toast.LENGTH_LONG).show();

                                edContPerMob.setText(contactNumber);
                                edContPerName.setText(contactNumberName);
                            }
                        }
                    }
                    break;
                }
        }
    }


    private void getSaveCustomer(Customer customer) {
        Log.e("PARAMETER","---------------------------------------CUSTOMER--------------------------"+customer);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
             commonDialog.show();

            Call<Customer> listCall = Constants.myInterface.saveCustomer(customer);
            listCall.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE CUSTOMER : ", " ------------------------------SAVE CUSTOMER------------------------- " + response.body());
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new MainFragment(), "HomeFragment");
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
                public void onFailure(Call<Customer> call, Throwable t) {
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
}
