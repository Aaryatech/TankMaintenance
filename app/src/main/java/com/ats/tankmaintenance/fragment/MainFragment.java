package com.ats.tankmaintenance.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.CustomerListAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.CustomerLocationName;
import com.ats.tankmaintenance.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    CustomerListAdapter mAdapter;
    private ArrayList<CustomerLocationName> custList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Add Customer");
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment adf = new AddCustomerFragment();
                Bundle args = new Bundle();
                // args.putString("type", "leave");
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();


            }
        });

        getCustomerList();

        return view;
    }

    private void getCustomerList() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CustomerLocationName>> listCall = Constants.myInterface.getAllCustomerList();
            listCall.enqueue(new Callback<ArrayList<CustomerLocationName>>() {
                @Override
                public void onResponse(Call<ArrayList<CustomerLocationName>> call, Response<ArrayList<CustomerLocationName>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CUSTOMER LIST : ", "-------------------------------CUSTOMER LIST--------------------------------" + response.body());

                            custList.clear();
                            custList = response.body();

                            mAdapter = new CustomerListAdapter(custList, getActivity());
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





}
