package com.ats.tankmaintenance.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.WorkHistoryAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Info;
import com.ats.tankmaintenance.model.WorkHistory;
import com.ats.tankmaintenance.utils.CommonDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkHistoryFragment extends Fragment {
private RecyclerView recyclerView;
ArrayList<WorkHistory> workHistoryList = new ArrayList<>();
WorkHistoryAdapter adapter;
private EditText ed_search;
ArrayList<WorkHistory> temp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_history, container, false);
        getActivity().setTitle("Work History");
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        ed_search = (EditText) view.findViewById(R.id.ed_search);

        getFromDate();

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                FilterSearch(charSequence.toString());
                // empAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        
        return view;
    }

    private void FilterSearch(String s) {
        temp = new ArrayList();
        for (WorkHistory d : workHistoryList) {
            if (d.getCustomerName().toLowerCase().contains(s.toLowerCase())) {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }

    private void getFromDate() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.getSettingValue();
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("FROM DATE: ", " - " + response.body());

                            if (!response.body().getError()) {

                                Log.e("Date","--------------------------------------Date----------------------"+response.body().getMsg());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                getWorkHistory(0,response.body().getMsg(),sdf.format(System.currentTimeMillis()));

                            } else {
                                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getWorkHistory(int custId, String fromDate, String toDate) {
        Log.e("PARAMETERS : ", "        CUST ID : " + custId + "      FROM DATE : " + fromDate + "         TO DATE : " + toDate );

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            ArrayList<Integer> statusList = new ArrayList<>();
            statusList.add(1);
            statusList.add(2);


            Call<ArrayList<WorkHistory>> listCall = Constants.myInterface.getWorkHistoryByCustId(custId,statusList, fromDate, toDate);
            listCall.enqueue(new Callback<ArrayList<WorkHistory>>() {
                @Override
                public void onResponse(Call<ArrayList<WorkHistory>> call, Response<ArrayList<WorkHistory>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("WORK HISTORY LIST : ", " - " + response.body());
                            workHistoryList.clear();
                            workHistoryList = response.body();
                            // createQuotationPDF(quotList);

                            adapter = new WorkHistoryAdapter(workHistoryList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
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
                public void onFailure(Call<ArrayList<WorkHistory>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
