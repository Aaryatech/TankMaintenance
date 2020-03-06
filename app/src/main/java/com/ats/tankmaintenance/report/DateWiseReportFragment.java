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

import com.ats.tankmaintenance.BuildConfig;
import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.DateReportAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.DateReport;
import com.ats.tankmaintenance.model.Info;
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
public class DateWiseReportFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<DateReport> dateReportList = new ArrayList<>();
    DateReportAdapter adapter;
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
        View view= inflater.inflate(R.layout.fragment_date_wise_report, container, false);
        getActivity().setTitle("Date Wise Collection Report");
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

                            if (!response.body().getError()) {

                                 info=response.body();
                                Log.e("Date","--------------------------------------Date----------------------"+response.body().getMsg());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                getDateReport(response.body().getMsg(),sdf.format(System.currentTimeMillis()));

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

    private void getDateReport(String fromDate, String toDate) {
        Log.e("PARAMETER","           FROM DATE                      "+fromDate +"           TO DATE           "  +toDate );
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DateReport>> listCall = Constants.myInterface.getDatePaymentWiseReport(fromDate,toDate);
            listCall.enqueue(new Callback<ArrayList<DateReport>>() {
                @Override
                public void onResponse(Call<ArrayList<DateReport>> call, Response<ArrayList<DateReport>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DATE REPORT LIST : ", " - " + response.body());
                            dateReportList.clear();
                            dateReportList = response.body();

                            adapter = new DateReportAdapter(dateReportList, getContext());
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
                public void onFailure(Call<ArrayList<DateReport>> call, Throwable t) {
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

        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                createDatePaymentReportPDF(dateReportList,info.getMsg(),sdf.format(System.currentTimeMillis()));

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

    private void createDatePaymentReportPDF(ArrayList<DateReport> dateReportList, String fromDate, String todate) {

        Log.e("PARAMETER","--------------------------"+"FROM DATE"+fromDate +"   "+"TO DATE"+todate);

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

            String fileName = "Date_Wise_Payment_Report_" + dateReportList.get(0).getPaymentDate() + ".pdf";
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

                cell = new PdfPCell(new Paragraph("Report : Date Wise Payment Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("From Date : " + fromDate));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("To Date : " + todate));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(dateTable);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(3);
                float[] columnWidth = new float[]{10, 30, 30};
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


                cell = new PdfPCell(new Phrase("PAYMENT DATE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("PAYMENT AMT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                float total = 0;
                for (int i = 0; i < dateReportList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + dateReportList.get(i).getPaymentDate());


                    cell = new PdfPCell(new Phrase("" + dateReportList.get(i).getCostRs()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

//                    cell = new PdfPCell(new Phrase("" + dateReportList.get(i).getTotalAmt()));
//                    cell.setHorizontalAlignment(2);
//                    cell.setBackgroundColor(myColor);
//                    table.addCell(cell);
//
//                    cell = new PdfPCell(new Phrase("" + dateReportList.get(i).getCostRs()));
//                    cell.setHorizontalAlignment(2);
//                    cell.setBackgroundColor(myColor);
//                    table.addCell(cell);


                    // total = total + (monthReportList.get(i).getPayableAmount());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

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
