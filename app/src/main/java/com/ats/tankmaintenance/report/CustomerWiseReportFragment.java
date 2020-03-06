package com.ats.tankmaintenance.report;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.BuildConfig;
import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.CustReportAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerWiseReportFragment extends Fragment {
private RecyclerView recyclerView;
private Spinner spArea;
ArrayList<Payment> custReportList = new ArrayList<>();
ArrayList<Payment> temp;
CustReportAdapter adapter;
int locationId;

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

ArrayList<String> locationNameList = new ArrayList<>();
ArrayList<Integer> locationIdList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer_wise_report, container, false);
        getActivity().setTitle("Customer Report");
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        spArea=(Spinner)view.findViewById(R.id.spArea);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vital/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        getLocation();
        getCustomerWiseReport(0);

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(position!=0)
                    {
                        locationId=locationIdList.get(position);
                        getCustomerWiseReport(locationId);

                    }
                }catch (Exception e) {
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void getCustomerWiseReport(int areaId) {
        Log.e("PARAMETER","------------------------------------AREA ID---------------------"+areaId);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Payment>> listCall = Constants.myInterface.getCustomerWiseReport(areaId);
            listCall.enqueue(new Callback<ArrayList<Payment>>() {
                @Override
                public void onResponse(Call<ArrayList<Payment>> call, Response<ArrayList<Payment>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CUSTOMER REPORT LIST : ", " - " + response.body());
                            custReportList.clear();
                            custReportList = response.body();

                            adapter = new CustReportAdapter(custReportList, getContext());
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
                public void onFailure(Call<ArrayList<Payment>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);

        SearchView searchView = (SearchView) item.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        ImageView v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        v.setImageResource(R.drawable.ic_search_white); //Changing the image

        MenuItem itemDownload = menu.findItem(R.id.action_download);
        //item.setVisible(true);
        itemDownload.setVisible(true);

        searchEditText.addTextChangedListener(new TextWatcher() {
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

        searchView.setQueryHint("search");
    }

    private void FilterSearch(String s) {
        temp = new ArrayList();
        for (Payment d : custReportList) {
            if (d.getCustomerName().toLowerCase().contains(s.toLowerCase())) {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        //MenuItem item = menu.findItem(R.id.action_filter);
//        MenuItem itemDownload = menu.findItem(R.id.action_download);
//        //item.setVisible(true);
//        itemDownload.setVisible(true);
//
//    }


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

        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                createCustomerReportPDF(custReportList);

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

    private void createCustomerReportPDF(ArrayList<Payment> custReportList) {

        final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Customer_Wise_Report_" + custReportList.get(0).getCustomerName() + ".pdf";
            file = new File(dir, fileName);
            // file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            // file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Vital", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report : Customer Wise Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

//                PdfPTable dateTable = new PdfPTable(2);
//                dateTable.setWidthPercentage(100);
//                cell = new PdfPCell(new Paragraph("From Date : " + fromDate));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                dateTable.addCell(cell);
//
//                cell = new PdfPCell(new Paragraph("To Date : " + todate));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

//                cell = new PdfPCell();
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setColspan(1);
//                cell.addElement(dateTable);
//                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 30, 30, 30, 30,30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("CUST NAME", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("CUST MOB", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("WORK AMT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PAYMENT AMT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PENDING AMT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                float total = 0;
                for (int i = 0; i < custReportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + custReportList.get(i).getCustomerName());


                    cell = new PdfPCell(new Phrase("" + custReportList.get(i).getCustomerPhone()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + custReportList.get(i).getWorkAmt()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + custReportList.get(i).getPayAmt()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    int diff =(custReportList.get(i).getWorkAmt() - custReportList.get(i).getPayAmt());

                    cell = new PdfPCell(new Phrase("" + diff));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


                    // total = total + (monthReportList.get(i).getPayableAmount());
                }

                //----BLANK ROW
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBackgroundColor(myColor);
//                table.addCell(cell);
//
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBackgroundColor(myColor);
//                table.addCell(cell);
//
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBackgroundColor(myColor);
//                table.addCell(cell);
//
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBackgroundColor(myColor);
//                table.addCell(cell);
//
//                cell = new PdfPCell(new Phrase(""));
//                cell.setBackgroundColor(myColor);
//                table.addCell(cell);
//
//                cell = new PdfPCell(new Phrase(" "));
//                cell.setBackgroundColor(myColor);
//                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 50, 50};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

//                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
//                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
//                cell.setBackgroundColor(myColor);
//                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

//                Paragraph paragraph=new Paragraph("Vital1");
//                paragraph.setAlignment(c);
//                doc.add(new Paragraph("Vital1"));

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(getActivity(), "Unable To Generate", Toast.LENGTH_SHORT).show();
                Log.e("Mytag2","--------------------");
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(getActivity(), authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getActivity().startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Generate", Toast.LENGTH_SHORT).show();
                    Log.e("Mytag1","--------------------");
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Log.e("Mytag","--------------------");
            Toast.makeText(getActivity(), "Unable To Generate", Toast.LENGTH_SHORT).show();
        }
    }


}
