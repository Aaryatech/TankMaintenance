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
import com.ats.tankmaintenance.adapter.EmployeeListAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Employee;
import com.ats.tankmaintenance.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    EmployeeListAdapter mAdapter;
    private ArrayList<Employee> empList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee, container, false);
        getActivity().setTitle("Employee Master");

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment adf = new AddEmployeeFragment();
                Bundle args = new Bundle();
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "EmployeeFragment").commit();

            }
        });

        getEmployeeList();

      //  prepareData();
        return view;
    }

    private void getEmployeeList() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Employee>> listCall = Constants.myInterface.getAllUserList();
            listCall.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("EMPLOYEE LIST : ", "-------------------------------EMPLOYEE LIST--------------------------------" + response.body());

                            empList.clear();
                            empList = response.body();

                            mAdapter = new EmployeeListAdapter(empList, getActivity());
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

    private void prepareData() {

//        EmployeeList employeeList = new EmployeeList("Tejas Patil","7845962145",R.drawable.profile,"Panchavati");
//        empList.add(employeeList);
//
//         employeeList = new EmployeeList("Pravin Bhamre","7845962145",R.drawable.profile,"Panchavati");
//        empList.add(employeeList);
//
//         employeeList = new EmployeeList("Jayant Patil","7845962145",R.drawable.profile,"Panchavati");
//        empList.add(employeeList);
//
//         employeeList = new EmployeeList("Tejas Patil","7845962145",R.drawable.profile,"Panchavati");
//        empList.add(employeeList);


    }

}
