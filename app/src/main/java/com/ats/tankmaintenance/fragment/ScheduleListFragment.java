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
import com.ats.tankmaintenance.adapter.ScheduleListAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.WorkHistory;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.ats.tankmaintenance.utils.PermissionsUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleListFragment extends Fragment {
private RecyclerView recyclerView;
ArrayList<WorkHistory> scheduleList = new ArrayList<>();
ScheduleListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        getActivity().setTitle("Schedule List");

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        if (PermissionsUtil.checkAndRequestPermissions(getActivity())) {
        }

        getScheduleList(1);

        return view;
    }

    private void getScheduleList(int status) {
        Log.e("PARAMETERS : ", "        STATUS : " + status);

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<WorkHistory>> listCall = Constants.myInterface.getWorkListSchedule(status);
            listCall.enqueue(new Callback<ArrayList<WorkHistory>>() {
                @Override
                public void onResponse(Call<ArrayList<WorkHistory>> call, Response<ArrayList<WorkHistory>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SCHEDULE LIST : ", " - " + response.body());
                            scheduleList.clear();
                            scheduleList = response.body();

                            adapter = new ScheduleListAdapter(scheduleList, getContext());
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
