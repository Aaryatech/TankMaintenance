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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.BuildConfig;
import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.adapter.PaymentDetailAdapter;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.model.PaymentDetail;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;
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
public class CustDetailReportFragment extends Fragment {
    Payment payment;
    private TextView tvName,tvMob,tvLocation,tvWorkAmt,tvPayAmt,tvUpperTankCost,tvLowertankCost,tvPending;
    int diff;
    ArrayList<PaymentDetail> PaymentDetailList = new ArrayList<>();
    PaymentDetailAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnAddPayment;

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
        View view= inflater.inflate(R.layout.fragment_cust_detail_report, container, false);

        getActivity().setTitle("Customer Payment Detail");

        tvName=(TextView) view.findViewById(R.id.tvName);
        tvMob=(TextView) view.findViewById(R.id.tvMob);
        tvLocation=(TextView) view.findViewById(R.id.tvLocation);
        tvWorkAmt=(TextView)view.findViewById(R.id.tvWorkerAmt);
        tvPayAmt=(TextView) view.findViewById(R.id.tvPayAmt);
        tvPending=(TextView) view.findViewById(R.id.tvPending);

        btnAddPayment=(Button)view.findViewById(R.id.btnAddPayment);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vital/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String upcomingStr = getArguments().getString("model");
        Gson gson = new Gson();

        try {
            payment = gson.fromJson(upcomingStr, Payment.class);
            Log.e("responce", "-----------------------" + payment);

            tvName.setText(payment.getCustomerName());
            tvMob.setText(payment.getCustomerPhone());
            tvLocation.setText(payment.getCustomerAddress());
            tvWorkAmt.setText(""+payment.getWorkAmt());
            tvPayAmt.setText(""+payment.getPayAmt());
            diff= (payment.getWorkAmt() - payment.getPayAmt());
            tvPending.setText("Pending : "+diff+"/-");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getPaymentDetail(payment.getCustomerId());
        return view;
    }

    private void getPaymentDetail(Integer customerId) {
        Log.e("PARAMETER","CUST_ID" +customerId);

        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<PaymentDetail>> listCall = Constants.myInterface.getTotalAmountByCustId(customerId);
            listCall.enqueue(new Callback<ArrayList<PaymentDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<PaymentDetail>> call, Response<ArrayList<PaymentDetail>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PAYMENT LIST : ", " - " + response.body());
                            PaymentDetailList.clear();
                            PaymentDetailList = response.body();

                            adapter = new PaymentDetailAdapter(PaymentDetailList, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
                public void onFailure(Call<ArrayList<PaymentDetail>> call, Throwable t) {
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
                createCustDetailReportPDF(PaymentDetailList,payment);

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

    private void createCustDetailReportPDF(ArrayList<PaymentDetail> paymentDetailList, Payment payment) {

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

            String fileName = "Cust_Wise_Detail_Report_" + payment.getCustomerName() + ".pdf";
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

                cell = new PdfPCell(new Paragraph("Report : Customer Wise Detail Report", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("Customer Name: " + payment.getCustomerName()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Mobile Number : " + payment.getCustomerPhone()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Work Amt: " + payment.getWorkAmt()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Payment Amt: " + payment.getPayAmt()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                int  diff= (payment.getWorkAmt() - payment.getPayAmt());

                cell = new PdfPCell(new Paragraph("Pending Amt: " +diff ));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph(""));
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

                PdfPTable table = new PdfPTable(4);
                float[] columnWidth = new float[]{10, 30, 30, 30};
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

                cell = new PdfPCell(new Phrase("WORK AMT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                float total = 0;
                for (int i = 0; i < paymentDetailList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + paymentDetailList.get(i).getPaymentDate());


                    cell = new PdfPCell(new Phrase("" + paymentDetailList.get(i).getCostRs()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + paymentDetailList.get(i).getTotalAmt()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


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
