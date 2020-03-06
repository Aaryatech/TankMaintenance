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
import com.ats.tankmaintenance.adapter.AreaListAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.utils.CommonDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AreaFragment extends Fragment {
private FloatingActionButton fab;
private RecyclerView recyclerView;
AreaListAdapter mAdapter;
private ArrayList<Location> areaList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_area, container, false);
        getActivity().setTitle("Area Master");

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment adf = new AddAreaFragment();
                Bundle args = new Bundle();
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "AreaFragment").commit();

            }
        });

        getLocationList();

        return view;
    }

    private void getLocationList() {
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Location>> listCall = Constants.myInterface.getLocationList();
            listCall.enqueue(new Callback<ArrayList<Location>>() {
                @Override
                public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("LOCATION LIST : ", "-------------------------------LOCATION LIST--------------------------------" + response.body());

                            areaList.clear();
                            areaList = response.body();

                            mAdapter = new AreaListAdapter(areaList, getActivity());
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



}
