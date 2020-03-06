package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.BuildConfig;
import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.activity.WorkHistoryDetailActivity;
import com.ats.tankmaintenance.model.User;
import com.ats.tankmaintenance.model.WorkHistory;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkHistoryAdapter extends RecyclerView.Adapter<WorkHistoryAdapter.MyViewHolder> {
    private ArrayList<WorkHistory> workHistoryList;
    private Context context;

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

    public WorkHistoryAdapter(ArrayList<WorkHistory> workHistoryList, Context context) {
        this.workHistoryList = workHistoryList;
        this.context = context;
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vital/Reports";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @NonNull
    @Override
    public WorkHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapet_worl_history_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkHistoryAdapter.MyViewHolder myViewHolder, int i) {
        final WorkHistory model = workHistoryList.get(i);

        myViewHolder.tvCustName.setText(model.getCustomerName());
        myViewHolder.tvRemark.setText(model.getCustomerAddress());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

        Date TODate = null;
        try {
            TODate = formatter.parse(model.getWorkDate());//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String workDate = formatter1.format(TODate);
        myViewHolder.tvDate.setText("Work Date :"+workDate);

        Date fromDate = null;
        try {
            fromDate = formatter.parse(model.getNextDate());//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String nextDate = formatter1.format(fromDate);
        myViewHolder.tvNextDate.setText("Next Date :"+nextDate);


        if (model.getUser() != null) {
            ArrayList<User> detailList = new ArrayList<>();
            for (int j = 0; j < model.getUser().size(); j++) {
                detailList.add(model.getUser().get(j));
            }

            EmpListAdapter adapter = new EmpListAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(mLayoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            myViewHolder.recyclerView.setAdapter(adapter);
        }
        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItems.setVisibility(View.VISIBLE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_up));
        } else {
            myViewHolder.llItems.setVisibility(View.GONE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
        }


        myViewHolder.tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItems.setVisibility(View.VISIBLE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_up));
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItems.setVisibility(View.GONE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
                }
            }
        });

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(model);

                Intent intent = new Intent(context, WorkHistoryDetailActivity.class);
                Bundle args = new Bundle();
                args.putString("model", json);
                intent.putExtra("model", json);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_pdf, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.action_pdf){

                            getHistoryPdf(model);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void getHistoryPdf(WorkHistory model) {
        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
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

            String fileName = "Work_History_Detail_Report_" + model.getCustomerName() + ".pdf";
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

                cell = new PdfPCell(new Paragraph("Report : Bill Genrate", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                PdfPTable dateTable = new PdfPTable(2);
                dateTable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("Customer Name: " + model.getCustomerName()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Mobile Number : " + model.getCustomerPhone()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                dateTable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Customer Address: " + model.getCustomerAddress()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                dateTable.addCell(cell);

//                cell = new PdfPCell(new Paragraph("Payment Amt: " + model.getPayAmt()));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                dateTable.addCell(cell);
//
//                int  diff= (payment.getWorkAmt() - payment.getPayAmt());
//
//                cell = new PdfPCell(new Paragraph("Pending Amt: " +diff ));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                dateTable.addCell(cell);

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

                PdfPTable table = new PdfPTable(3);
                float[] columnWidth = new float[]{30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

//                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
//                cell.setHorizontalAlignment(1);
//                cell.setBackgroundColor(myColor1);
//                table.addCell(cell);


                cell = new PdfPCell(new Phrase("TANK TYPE", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("TANK QTY", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("TANK AMT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


               // float total = 0;
               // for (int i = 0; i < model; i++) {
                   //int i=0;
                    //table.addCell("" + (i + 1));

                    table.addCell("Lower Tank");

                    cell = new PdfPCell(new Phrase("" + model.getNoOfLowerTank()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" +model.getAmtLowerTank()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                   // table.addCell("" + (i + 1));

                    table.addCell("Upper Tank");

                    cell = new PdfPCell(new Phrase("" + model.getNoOfUpperTank()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" +model.getAmtUpperTank()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

//                    cell = new PdfPCell(new Phrase("" + model.getTotalAmt()));
//                    cell.setHorizontalAlignment(2);
//                    cell.setBackgroundColor(myColor);
//                    table.addCell(cell);


               // }

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

                int total = 0;
                total =model.getTotalAmt()+model.getDiscAmt();


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  DISCOUNT", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + model.getDiscAmt(), boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  FINAL AMT", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("" + model.getTotalAmt(), boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(context, "Unable To Generate", Toast.LENGTH_SHORT).show();
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
                            Uri uri = FileProvider.getUriForFile(context, authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Generate", Toast.LENGTH_SHORT).show();
                    Log.e("Mytag1","--------------------");
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Log.e("Mytag","--------------------");
            Toast.makeText(context, "Unable To Generate", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return workHistoryList.size();
    }

    public void updateList(ArrayList<WorkHistory> temp) {
        workHistoryList = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustName, tvNextDate, tvDate, tvRemark, tvItems;
        public RecyclerView recyclerView;
        public CardView cardView;
        public ImageView imageView,ivEdit;
        public LinearLayout llItems;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNextDate = itemView.findViewById(R.id.tvNextDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            tvItems = itemView.findViewById(R.id.tvItems);
            llItems = itemView.findViewById(R.id.llItems);
            imageView = itemView.findViewById(R.id.imageView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
