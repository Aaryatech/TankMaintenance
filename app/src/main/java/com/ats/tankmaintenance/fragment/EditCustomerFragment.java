package com.ats.tankmaintenance.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Customer;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCustomerFragment extends Fragment implements View.OnClickListener {
    private Spinner spArea;
    private EditText edName,edMob,edAddress,edContPerName,edContPerMob,edNextCleaning,edOverHeadTank,edUndergrounTank,edOverHeadTankCost,edUndergrounTankCost;
    private Button btnSubmit;
    Customer model;
    ArrayList<String> areaArray = new ArrayList<>();

    ArrayList<String> locationNameList = new ArrayList<>();
    ArrayList<Integer> locationIdList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_customer, container, false);

        spArea=(Spinner)view.findViewById(R.id.spArea);
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

        String custStr = getArguments().getString("model");
        Gson gson = new Gson();

        try {
            model = gson.fromJson(custStr, Customer.class);
            edName.setText(model.getCustomerName());
            edMob.setText(model.getCustomerPhone());
            edAddress.setText(model.getCustomerAddress());
            edContPerName.setText(model.getCustomerContactName());
            edContPerMob.setText(model.getCustomerContactNumber());
            edNextCleaning.setText("" + model.getFrequency());
            edOverHeadTank.setText("" + model.getNoOfUpperTank());
            edOverHeadTankCost.setText("" + model.getCostUppertankPerpieces());
            edUndergrounTank.setText("" + model.getNoOfLowerTank());
            edUndergrounTankCost.setText("" + model.getCostLowertankPerpieces());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        btnSubmit=(Button)view.findViewById(R.id.btn_submit);

        getLocation();

        btnSubmit.setOnClickListener(this);

        return view;
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

                                ArrayAdapter<String> projectAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationNameList);
                                spArea.setAdapter(projectAdapter);
                            }

                            if (model != null) {
                                int position = 0;
                                if (locationIdList.size() > 0) {
                                    for (int i = 0; i < locationIdList.size(); i++) {
                                        if (model.getAreaId() == locationIdList.get(i)) {
                                            position = i;
                                            break;
                                        }
                                    }
                                }
                                spArea.setSelection(position);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_submit)
        {
            String strName,strMob,strAddress,strContPerName,strContPerMob,strNextCleaning,strOverHeadTank,strOverHeadTankCost,strUndergrounTank,strUndergroundTankCost;
            boolean isValidName = false, isValidMob = false, isValidAddress = false, isValidPerName = false, isValidPerMob = false,isValidNextCleaning = false, isValidLowerTank = false, isValidLowerTankCost = false, isValidUpperTank = false,isValidUpperTankCost = false;
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
            int locationId = locationIdList.get(spArea.getSelectedItemPosition());

            float noUpperTank= Float.parseFloat(strOverHeadTank);
            float noUpperTankCost= Float.parseFloat(strOverHeadTankCost);
            float noLowerTank= Float.parseFloat(strUndergrounTank);
            float noLowerTankCost= Float.parseFloat(strUndergroundTankCost);
            float nextCleaning= Float.parseFloat(strNextCleaning);

            if (locationId == 0) {
                TextView viewProj = (TextView) spArea.getSelectedView();
                viewProj.setError("required");
            }else{
                TextView viewProj = (TextView) spArea.getSelectedView();
                viewProj.setError(null);
            }
            if (strName.isEmpty()) {
                edName.setError("required");
            } else {
                edName.setError(null);
                isValidName = true;
            }
            if (strMob.isEmpty()) {
                edMob.setError("required");
            } else if (strMob.length() != 10) {
                edMob.setError("required 10 digits");
            } else if (strMob.equalsIgnoreCase("0000000000")) {
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
            } else if (strContPerMob.length() != 10) {
                edContPerMob.setError("required 10 digits");
            } else if (strContPerMob.equalsIgnoreCase("0000000000")) {
                edContPerMob.setError("invalid number");
            } else {
                edContPerMob.setError(null);
                isValidPerMob = true;
            }
            if (strNextCleaning.isEmpty()) {
                edNextCleaning.setError("required");
            } else {
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

            if(isValidName && isValidMob && isValidAddress && isValidPerName && isValidPerMob && isValidNextCleaning && isValidLowerTank && isValidLowerTankCost && isValidUpperTank && isValidUpperTankCost)
            {
                final Customer customer = new Customer(model.getCustomerId(),strName,strAddress,strMob,"",strContPerName,strContPerMob,noUpperTank,noLowerTank,noUpperTankCost,noLowerTankCost,locationId,nextCleaning,model.getDelStatus(),model.getIsUsed(),model.getExInt1(),model.getExInt2(),model.getExInt3(),model.getExVar1(),model.getExVar2(),model.getExVar3(),model.getExFloat1(),model.getRemark());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to edit Customer?");
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

                            Log.e("EDIT CUSTOMER : ", " ------------------------------EDIT CUSTOMER------------------------- " + response.body());
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
