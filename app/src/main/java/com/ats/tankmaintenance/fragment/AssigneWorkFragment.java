package com.ats.tankmaintenance.fragment;


import android.os.Bundle;
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
import com.ats.tankmaintenance.adapter.AssignedWorkAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.WorkHistory;
import com.ats.tankmaintenance.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssigneWorkFragment extends Fragment {
private RecyclerView recyclerView;
ArrayList<WorkHistory> assignedWorkList = new ArrayList<>();
AssignedWorkAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_assigne_work, container, false);
        getActivity().setTitle("Pending Work");
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        getAssignedWork(0);
        return view;
    }

    private void getAssignedWork(int status) {
        Log.e("PARAMETERS : ", "        STATUS : " + status);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<WorkHistory>> listCall = Constants.myInterface.getWorkByStatus(status);
            listCall.enqueue(new Callback<ArrayList<WorkHistory>>() {
                @Override
                public void onResponse(Call<ArrayList<WorkHistory>> call, Response<ArrayList<WorkHistory>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("ASSIGNED LIST : ", " - " + response.body());
                            assignedWorkList.clear();
                            assignedWorkList = response.body();
                            // createQuotationPDF(quotList);

                            adapter = new AssignedWorkAdapter(assignedWorkList, getContext());
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
