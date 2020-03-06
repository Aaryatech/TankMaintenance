package com.ats.tankmaintenance.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ats.tankmaintenance.R;
import com.ats.tankmaintenance.fragment.AddAreaFragment;
import com.ats.tankmaintenance.fragment.AddCustomerFragment;
import com.ats.tankmaintenance.fragment.AddEmployeeFragment;
import com.ats.tankmaintenance.fragment.AddScheduleWorkFragment;
import com.ats.tankmaintenance.fragment.AddWorkFragment;
import com.ats.tankmaintenance.fragment.AreaFragment;
import com.ats.tankmaintenance.fragment.AssigneWorkFragment;
import com.ats.tankmaintenance.fragment.EditAreaFragment;
import com.ats.tankmaintenance.fragment.EditCustomerFragment;
import com.ats.tankmaintenance.fragment.EditEmployeeFragment;
import com.ats.tankmaintenance.fragment.EmployeeFragment;
import com.ats.tankmaintenance.fragment.MainFragment;
import com.ats.tankmaintenance.fragment.PaymentDetailFragment;
import com.ats.tankmaintenance.fragment.PaymentListFragment;
import com.ats.tankmaintenance.fragment.ScheduleListDetailFragment;
import com.ats.tankmaintenance.fragment.ScheduleListFragment;
import com.ats.tankmaintenance.fragment.SetFromDateFragment;
import com.ats.tankmaintenance.fragment.WorkHistoryFragment;
import com.ats.tankmaintenance.model.Employee;
import com.ats.tankmaintenance.report.CustDetailReportFragment;
import com.ats.tankmaintenance.report.CustomerWiseReportFragment;
import com.ats.tankmaintenance.report.DateWiseReportFragment;
import com.ats.tankmaintenance.report.DateWiseWorkReportFragment;
import com.ats.tankmaintenance.report.EmployeeWiseReportFragment;
import com.ats.tankmaintenance.report.MonthWiseReportFragment;
import com.ats.tankmaintenance.utils.CustomSharedPreference;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
Employee loginUser;
boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String userStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_USER);
        Gson gson = new Gson();
        loginUser = gson.fromJson(userStr, Employee.class);
        Log.e("HOME_ACTIVITY : ", "--------USER-------" + loginUser);

//        try {
//            String strAssign = getIntent().getStringExtra("isassigne");
//            Log.e("ASSIGNE : ", "--------USER-------" + strAssign);
//            if(strAssign!=null) {
//                if (strAssign.equalsIgnoreCase("isassigne")) {
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, new AssigneWorkFragment(), "HomeFragment");
//                    ft.commit();
//                }
//            }
//            else{
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, new ScheduleListFragment(), "HomeFragment");
//                ft.commit();
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        try {
            if(loginUser!=null) {
                if (loginUser.getDesignation().equalsIgnoreCase("Admin")) {

                    navigationView.getMenu().findItem(R.id.nav_customer).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_employee).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_area).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_setFromDate).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_scheduleList).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_paymentList).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_report).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_master).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_new_work).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_regular_work).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_recovery).setVisible(true);

//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, new ScheduleListFragment(), "HomeFragment");
//                    ft.commit();

                    try {
                        String strAssign = getIntent().getStringExtra("isassigne");
                        Log.e("ASSIGNE : ", "--------USER-------" + strAssign);
                        if(strAssign!=null) {
                            if (strAssign.equalsIgnoreCase("isassigne")) {
                                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                                ft1.replace(R.id.content_frame, new AssigneWorkFragment(), "HomeFragment");
                                ft1.commit();
                            }
                        }
                        else{
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content_frame, new ScheduleListFragment(), "HomeFragment");
                            ft2.commit();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
                else {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new AssigneWorkFragment(), "HomeFragment");
                    ft.commit();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        View header = navigationView.getHeaderView(0);

        TextView tvNavHeadName = header.findViewById(R.id.tvNavHeadName);
        TextView tvNavHeadDesg = header.findViewById(R.id.tvNavHeadDesg);
        CircleImageView ivNavHeadPhoto = header.findViewById(R.id.ivNavHeadPhoto);

        if (loginUser != null) {
            tvNavHeadName.setText("" + loginUser.getUserName());
            tvNavHeadDesg.setText("" + loginUser.getDesignation());
        }

        if (loginUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }

    }

    @Override
    public void onBackPressed() {
        Fragment exit = getSupportFragmentManager().findFragmentByTag("Exit");
        Fragment mainFragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
        Fragment employeeFragment = getSupportFragmentManager().findFragmentByTag("EmployeeFragment");
        Fragment areaFragment = getSupportFragmentManager().findFragmentByTag("AreaFragment");
        Fragment scheduleFragment = getSupportFragmentManager().findFragmentByTag("ScheduleFragment");
        Fragment scheduleListDetailFragment = getSupportFragmentManager().findFragmentByTag("ScheduleListDetailFragment");
        Fragment paymentFragment = getSupportFragmentManager().findFragmentByTag("PaymentFragment");
        Fragment custReportFragment = getSupportFragmentManager().findFragmentByTag("CustomerFragment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (exit instanceof MainFragment && exit.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
            builder.setMessage("Exit Application ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
        } else if (mainFragment instanceof EmployeeFragment && mainFragment.isVisible() ||
                mainFragment instanceof AddCustomerFragment && mainFragment.isVisible() ||
                mainFragment instanceof AddEmployeeFragment && mainFragment.isVisible() ||
                mainFragment instanceof AddAreaFragment && mainFragment.isVisible() ||
                mainFragment instanceof AddWorkFragment && mainFragment.isVisible() ||
                mainFragment instanceof AddScheduleWorkFragment && mainFragment.isVisible() ||
                mainFragment instanceof EditCustomerFragment && mainFragment.isVisible() ||
                mainFragment instanceof PaymentListFragment && mainFragment.isVisible() ||
                mainFragment instanceof EditAreaFragment && mainFragment.isVisible() ||
                mainFragment instanceof AssigneWorkFragment && mainFragment.isVisible() ||
                mainFragment instanceof PaymentListFragment && mainFragment.isVisible() ||
                mainFragment instanceof WorkHistoryFragment && mainFragment.isVisible() ||
                mainFragment instanceof ScheduleListFragment && mainFragment.isVisible() ||
                mainFragment instanceof ScheduleListDetailFragment && mainFragment.isVisible() ||
                mainFragment instanceof SetFromDateFragment && mainFragment.isVisible() ||
                mainFragment instanceof AreaFragment && mainFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MainFragment(), "Exit");
            ft.commit();

        }else if (employeeFragment instanceof EditEmployeeFragment && employeeFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeFragment(), "HomeFragment");
            ft.commit();
           // EmployeeFragment

        }  else if (areaFragment instanceof EditAreaFragment && areaFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AreaFragment(), "HomeFragment");
            ft.commit();
           // AreaFragment

        }else if (scheduleFragment instanceof AddScheduleWorkFragment && scheduleFragment.isVisible() ||
                scheduleFragment instanceof ScheduleListDetailFragment && scheduleFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ScheduleListFragment(), "HomeFragment");
            ft.commit();
            // ScheduleFragment

        } else if (scheduleListDetailFragment instanceof AddScheduleWorkFragment && scheduleListDetailFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ScheduleListFragment(), "HomeFragment");
            ft.commit();
            // ScheduleFragment

        }else if (paymentFragment instanceof PaymentDetailFragment && paymentFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new PaymentListFragment(), "HomeFragment");
            ft.commit();
            // ScheduleFragment

        }else if (custReportFragment instanceof CustDetailReportFragment && custReportFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CustomerWiseReportFragment(), "HomeFragment");
            ft.commit();
            // ScheduleFragment

        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_customer) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MainFragment(), "Exit");
            ft.commit();
        } else if (id == R.id.nav_employee) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeFragment(), "HomeFragment");
            ft.commit();
        } else if (id == R.id.nav_area) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AreaFragment(), "HomeFragment");
            ft.commit();
        } else if (id == R.id.nav_setFromDate) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SetFromDateFragment(), "HomeFragment");
            ft.commit();
        } else if (id == R.id.nav_scheduleList) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ScheduleListFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_addSchedule) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddWorkFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_workHistory) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new WorkHistoryFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_assigneWork) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AssigneWorkFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_paymentList) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new PaymentListFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_custReport) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CustomerWiseReportFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_monthReport) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MonthWiseReportFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_empReport) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeWiseReportFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_dateReport) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DateWiseReportFragment(), "HomeFragment");
            ft.commit();
        }else if (id == R.id.nav_dateWorkReport) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DateWiseWorkReportFragment(), "HomeFragment");
            ft.commit();
        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    CustomSharedPreference.deletePreference(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();


        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
