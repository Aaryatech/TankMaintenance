package com.ats.tankmaintenance.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.tankmaintenance.BuildConfig;
import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.activity.MainActivity;
import com.ats.tankmaintenance.constant.Constants;
import com.ats.tankmaintenance.fragment.EditCustomerFragment;
import com.ats.tankmaintenance.fragment.MainFragment;
import com.ats.tankmaintenance.model.CustomerLocationName;
import com.ats.tankmaintenance.model.Info;
import com.ats.tankmaintenance.utils.CommonDialog;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
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

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.MyViewHolder>  {
    private ArrayList<CustomerLocationName> custList;
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


    public CustomerListAdapter(ArrayList<CustomerLocationName> custList, Context context) {
        this.custList = custList;
        this.context = context;
        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "TankApp" + File.separator + "Quotation");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @NonNull
    @Override
    public CustomerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cust_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListAdapter.MyViewHolder myViewHolder, int i) {
        final CustomerLocationName model = custList.get(i);
        myViewHolder.tvCustName.setText(model.getCustomerName());
        myViewHolder.tvMob.setText(model.getCustomerPhone());
        myViewHolder.tvAddress.setText(model.getCustomerAddress());
        myViewHolder.tvArea.setText(model.getLocationName());

        myViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.qutation_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {
                            Gson gson = new Gson();
                            String json = gson.toJson(model);

                            MainActivity activity = (MainActivity) context;
                            Fragment adf = new EditCustomerFragment();
                            Bundle args = new Bundle();
                            args.putString("model", json);
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();

                        }else if(menuItem.getItemId()==R.id.action_delete)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do you want to delete Customer?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteCustomer(model.getCustomerId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else if(menuItem.getItemId()==R.id.action_quotation)
                        {
                            createQuotationPDF(model);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }


    public void createQuotationPDF(CustomerLocationName model) {

        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();

        Log.e("fileNmae","-------------------------------fileName--------------------"+model.getCustomerId());

        Document doc = new Document();

        doc.setMargins(-16, -17, 10, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font boldFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldFont1WithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD | Font.UNDERLINE);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE);
        Font boldTextFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Font textFontWithUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL | Font.UNDERLINE);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();


            String fileName = model.getCustomerName() + "Tank.pdf";


            file = new File(dir, fileName);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();


            try {

                //create table

                PdfPTable pdf = new PdfPTable(1);
                pdf.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Tank Service", boldTextFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph("", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph("Scientific Cleaners of all types of water storage tanks ", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                cell = new PdfPCell(new Paragraph("Contact: 9762020986, 9657460742", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);


                cell = new PdfPCell(new Paragraph(" ", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);


                cell = new PdfPCell(new Paragraph("", boldFont1));
                cell.setBorder(Rectangle.BOTTOM);
                cell.setHorizontalAlignment(1);
                pdf.addCell(cell);

                //---------------------------------------------------------------


                PdfPTable ptDate = new PdfPTable(4);
                float[] colWidth = new float[]{17, 50, 50, 15};
                //float[] colWidth = new float[]{17, 50};
                ptDate.setWidths(colWidth);
                ptDate.setTotalWidth(colWidth);
                ptDate.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("Date: ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                cell.setColspan(1);
                ptDate.addCell(cell);

//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = sdf.parse(model.getC().get(0).getQuotDate());
//
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    cell = new PdfPCell(new Paragraph("" + sdf.format(System.currentTimeMillis()), textFont));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(2);
                    cell.setColspan(1);
                    ptDate.addCell(cell);

//
//                //------------------------------------------------------------------
//
                PdfPTable ptTo = new PdfPTable(1);
                ptTo.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);


                cell = new PdfPCell(new Paragraph("To", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + model.getCustomerName(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + model.getCustomerAddress(), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo.addCell(cell);

//
//                //--------------------------------------------------------------------
//
                PdfPTable ptTo1 = new PdfPTable(1);
                ptTo1.setWidthPercentage(100);

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");

                cell = new PdfPCell(new Paragraph("Reference: Out Discussion Dated "+sdf1.format(System.currentTimeMillis()), textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

                cell = new PdfPCell(new Paragraph("Subject:Requirement for thank cleaning(Upper & Lower tanks)", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);


                cell = new PdfPCell(new Paragraph("Dear Sir,", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);


                cell = new PdfPCell(new Paragraph("We thank you for the giving us an opportunity to quote for the subject tank cleaning. Please find details of our quotation as below :", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTo1.addCell(cell);

//                //-------------------------------------------------------------------
//
                PdfPTable ptProject2 = new PdfPTable(2);
                float[] colProjWidth2 = new float[]{10,50};
                ptProject2.setWidths(colProjWidth2);
                ptProject2.setTotalWidth(colProjWidth2);
                ptProject2.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject2.addCell(cell);


                cell = new PdfPCell(new Paragraph("Scope :", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("1) Removal of water from tank\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("2) Removal of mud/debris from tank\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("3) Brushing of tank with special liquid(Soap Liquid)\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("4) Rinsing of tank with water\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);


                cell = new PdfPCell(new Paragraph("5) Disinfection of tank using scientifically proven liquids (LOC Liquid of Amway, Potassium Permanganate) \n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

                cell = new PdfPCell(new Paragraph("6) Drying of tank\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject2.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject2.addCell(cell);

//
//                //-------------------------------------------------------------------
//
                PdfPTable ptProject3 = new PdfPTable(2);
                float[] colProjWidt3 = new float[]{10,50};
                ptProject3.setWidths(colProjWidt3);
                ptProject3.setTotalWidth(colProjWidt3);
                ptProject3.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject3.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject3.addCell(cell);


                cell = new PdfPCell(new Paragraph("Assumptions :", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject3.addCell(cell);

                cell = new PdfPCell(new Paragraph("1) One person from customer should be present during cleaning activity.\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject3.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject3.addCell(cell);

                cell = new PdfPCell(new Paragraph("2) You should inform all the member of the building about tank cleaning before one day.\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject3.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject3.addCell(cell);

                cell = new PdfPCell(new Paragraph("3) Electrical supply to be provided by customer.\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject3.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                ptProject3.addCell(cell);


                //-------------------------------------------------------------------

                PdfPTable ptProject4 = new PdfPTable(1);
                ptProject4.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject4.addCell(cell);

                cell = new PdfPCell(new Paragraph("Will be communicated 2 day before start of work.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject4.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject4.addCell(cell);

//
//                //-------------------------------------------------------------------
//
                PdfPTable ptProject5 = new PdfPTable(1);
                ptProject5.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Payment terms :", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject5.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject5.addCell(cell);

                cell = new PdfPCell(new Paragraph("Four Time Water Tank Claning:", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptProject5.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptProject5.addCell(cell);




                //----------------------------------------------------------------------

                PdfPTable ptItemHead = new PdfPTable(4);
                float[] colItemHeadWidth = new float[]{30, 10, 15, 15};
                ptItemHead.setWidths(colItemHeadWidth);
                ptItemHead.setTotalWidth(colItemHeadWidth);
                ptItemHead.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Tank Details", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Quantity", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Rate", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("Amount", textFont));
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);


//                //-------------------------------------------------------------------------------

                        cell = new PdfPCell(new Paragraph("" + "Over Head Tank ", textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + model.getNoOfUpperTank(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(1);
                        ptItemHead.addCell(cell);


                        cell = new PdfPCell(new Paragraph("" + "", textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(0);
                        ptItemHead.addCell(cell);

                        cell = new PdfPCell(new Paragraph("" + model.getCostUppertankPerpieces(), textFont));
                        // cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(1);
                        ptItemHead.addCell(cell);


                cell = new PdfPCell(new Paragraph("" + "Underground Tank(Fire) ", textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + model.getNoOfLowerTank(), textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);


                cell = new PdfPCell(new Paragraph("" + "", textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + model.getCostLowertankPerpieces(), textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

                //-------------------------------------------------------------------------------

                cell = new PdfPCell(new Paragraph("" , textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptItemHead.addCell(cell);

                cell = new PdfPCell(new Paragraph("", textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptItemHead.addCell(cell);


                cell = new PdfPCell(new Paragraph("Total :" , textFont));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                ptItemHead.addCell(cell);

                int sum= (int) (model.getCostUppertankPerpieces()+model.getCostLowertankPerpieces());

                cell = new PdfPCell(new Paragraph("" +sum, boldFont1));
                // cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                ptItemHead.addCell(cell);

//                //--------------------------------------------------------------------------

                PdfPTable ptTerms = new PdfPTable(1);
                ptTerms.setWidthPercentage(100);

//                               cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph("Notes:- 1) 100% advance before execustion of tank cleaning work.", boldFont1));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptTerms.addCell(cell);


                //----------------------------------------------------------------------

                PdfPTable ptBank = new PdfPTable(1);
                ptBank.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("We look forward for your valuable order.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);

                cell = new PdfPCell(new Paragraph("Ensuring best quality service.", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptBank.addCell(cell);


//                //-----------------------------------------------------------------------------
//
                PdfPTable ptThank = new PdfPTable(1);
                ptThank.setWidthPercentage(100);

//                cell = new PdfPCell(new Paragraph(" ", textFont));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(0);
//                ptThank.addCell(cell);

                cell = new PdfPCell(new Paragraph( "Thanking you,\n", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                ptThank.addCell(cell);


//---------------------------------------------------------------------------------


                PdfPTable ptProj = new PdfPTable(1);
                ptProj.setWidthPercentage(100);


                cell = new PdfPCell(new Paragraph("For, ", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                //cell.setColspan(1);
                ptProj.addCell(cell);

                //"For "+quoteHeader.getProj().getProjName()
                cell = new PdfPCell(new Paragraph("Vital Services,", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                //cell.setColspan(1);
                ptProj.addCell(cell);

                cell = new PdfPCell(new Paragraph("Ganesh Thombore", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                //cell.setColspan(1);
                ptProj.addCell(cell);

                cell = new PdfPCell(new Paragraph("(Proprietor)", textFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                //cell.setColspan(1);
                ptProj.addCell(cell);


                //---------------------------------------------------------------

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pdf);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptDate);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTo);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTo1);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject2);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject3);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject4);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProject5);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptItemHead);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptTerms);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptBank);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptThank);
                pTable.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptProj);


                pTable.addCell(cell);


                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 50, 30, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                doc.add(table);

            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);
                Log.e("File","file1-------------------"+file1);

//                if (emailQuote == 0 && quotStatus == 1) {

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
                }


                // }
// else {
//                    shareViaEmail(quoteHeader.getCustId(), fileName);
//                    Toast.makeText(context, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
//                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            // Log.e("Mytag","-------------------Error--------------------"+ e.getStackTrace());
            Toast.makeText(context, "Unable To Generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCustomer(int customerId) {

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.deleteCustomer(customerId);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DELETE CUSTOMER: ", " - " + response.body());

                            if (!response.body().getError()) {

                                MainActivity activity = (MainActivity) context;

                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new MainFragment(), "HomeFragment");
                                ft.commit();

                            } else {
                                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return custList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustName, tvMob, tvAddress, tvArea;
        public CardView cardView;
        public ImageView ivEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvMob = itemView.findViewById(R.id.tvMob);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvArea = itemView.findViewById(R.id.tvArea);
            cardView=itemView.findViewById(R.id.cardView);
            ivEdit=itemView.findViewById(R.id.ivEdit);
        }
    }
}
