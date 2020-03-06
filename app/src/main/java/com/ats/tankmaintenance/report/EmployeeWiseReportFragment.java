package com.ats.tankmaintenance.report;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.EmployeeReportAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.EmployeeReport;
import com.ats.tankmaintenance.model.Info;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeWiseReportFragment extends Fragment {
private RecyclerView recyclerView;
ArrayList<EmployeeReport> empReportList = new ArrayList<>();
EmployeeReportAdapter adapter;
Info info;

    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_employee_wise_report, container, false);
        getActivity().setTitle("Employee Report");
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vital/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        getFromDate();
        return view;
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
                             info=response.body();

                            if (!response.body().getError()) {

                                Log.e("Date","--------------------------------------Date----------------------"+response.body().getMsg());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                getEmpReport(0,response.body().getMsg(),sdf.format(System.currentTimeMillis()));

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

    private void getEmpReport(int empId, String formDate, String toDate) {
        Log.e("PARAMETER","                FROM DATE             "+formDate +"           TO DATE          "  +toDate +"             EMP ID       "+empId);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<EmployeeReport>> listCall = Constants.myInterface.getEmployeeWiseReport(empId,formDate,toDate);
            listCall.enqueue(new Callback<ArrayList<EmployeeReport>>() {
                @Override
                public void onResponse(Call<ArrayList<EmployeeReport>> call, Response<ArrayList<EmployeeReport>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("EMPLOYEE REPORT LIST : ", " - " + response.body());
                            empReportList.clear();
                            empReportList = response.body();

                            adapter = new EmployeeReportAdapter(empReportList, getContext());
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
                public void onFailure(Call<ArrayList<EmployeeReport>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //MenuItem item = menu.findItem(R.id.action_filter);
        MenuItem itemDownload = menu.findItem(R.id.action_download);
        //item.setVisible(true);
        itemDownload.setVisible(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View menuItemView = getView().findViewById(R.id.action_download);
        switch (item.getItemId()) {
            case R.id.action_download:
                downloadDialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void downloadDialog() {

        final Dialog openDialog = new Dialog(getActivity());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_download);
        openDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 50;
        wlp.y = 100;
        // wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.width = 350;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        TextView tvPdf = openDialog.findViewById(R.id.tvPdf);
        TextView tvExcel = openDialog.findViewById(R.id.tvExcel);
        Log.e("Emp Pdf","-------------------------------------------");
        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Log.e("Emp Pdf","-------------------------------------------"+empReportList);
                createEmpReportPDF(empReportList,info.getMsg(),sdf.format(System.currentTimeMillis()));

                openDialog.dismiss();
            }
        });

        tvExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });

        openDialog.show();

    }

    private void createEmpReportPDF(ArrayList<EmployeeReport> empReportList, String fromDate, String toDate) {
    }


}
