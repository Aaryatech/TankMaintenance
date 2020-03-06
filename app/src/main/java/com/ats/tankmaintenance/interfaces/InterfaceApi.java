package com.ats.tankmaintenance.interfaces;

import com.ats.tankmaintenance.model.AddPayment;
import com.ats.tankmaintenance.model.Customer;
import com.ats.tankmaintenance.model.CustomerLocationName;
import com.ats.tankmaintenance.model.DateReport;
import com.ats.tankmaintenance.model.DateWorkReport;
import com.ats.tankmaintenance.model.Employee;
import com.ats.tankmaintenance.model.EmployeeReport;
import com.ats.tankmaintenance.model.Info;
import com.ats.tankmaintenance.model.Location;
import com.ats.tankmaintenance.model.MonthReport;
import com.ats.tankmaintenance.model.Payment;
import com.ats.tankmaintenance.model.PaymentDetail;
import com.ats.tankmaintenance.model.Work;
import com.ats.tankmaintenance.model.WorkHistory;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceApi {

    @POST("loginUser")
    Call<Employee> doLogin(@Query("username") String username, @Query("userPass") String userPass);

    @POST("saveCustomer")
    Call<Customer> saveCustomer(@Body Customer customer);

    @GET("getAllCustomerListWithLocName")
    Call<ArrayList<CustomerLocationName>> getAllCustomerList();

    @POST("deleteCustomer")
    Call<Info> deleteCustomer(@Query("custId") int custId);

    @POST("saveLocation")
    Call<Location> saveLocation(@Body Location location);

    @GET("getAllLocationList")
    Call<ArrayList<Location>> getLocationList();

    @POST("deleteLocation")
    Call<Info> deleteLocation(@Query("locationId") int locationId);

    @GET("getAllUserList")
    Call<ArrayList<Employee>> getAllUserList();

    @POST("deleteUser")
    Call<Info> deleteUser(@Query("userId") int userId);

    @POST("saveUser")
    Call<Employee> saveUser(@Body Employee employee);

    @Multipart
    @POST("photoUpload")
    Call<JSONObject> imageUpload(@Part MultipartBody.Part filePath, @Part("imageName") RequestBody name, @Part("type") RequestBody type);

    @POST("getCustomerByLocationId")
    Call<ArrayList<Customer>> getCustomerByLocationId(@Query("areaId") int areaId);

    @POST("saveWork")
    Call<Work> saveWork(@Body Work work);

    @POST("getWorkByStatus")
    Call<ArrayList<WorkHistory>> getWorkByStatus(@Query("status") int status);

    @POST("updateSettingValue")
    Call<Info> updateSettingValue(@Query("fromDate") String fromDate);

    @GET("getSettingValue")
    Call<Info> getSettingValue();

    @POST("getWorkHistoryByCustId")
    Call<ArrayList<WorkHistory>> getWorkHistoryByCustId(@Query("custId") int custId, @Query("status") ArrayList<Integer> status, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getWorkListSchedule")
    Call<ArrayList<WorkHistory>> getWorkListSchedule(@Query("status") int status);

    @POST("deleteAssignedWork")
    Call<Info> deleteAssignedWork(@Query("workId") int workId);

    @GET("getCustomerInfoByAmtDesc")
    Call<ArrayList<Payment>> getCustomerInfoByAmtDesc();

    @POST("getTotalAmountByCustId")
    Call<ArrayList<PaymentDetail>> getTotalAmountByCustId(@Query("custId") int custId);

    @POST("savePayment")
    Call<AddPayment> savePayment(@Body AddPayment addPayment);

    @POST("getCustomerWiseReport")
    Call<ArrayList<Payment>> getCustomerWiseReport(@Query("areaId") int areaId);

    @POST("getMonthWiseReportByDate")
    Call<ArrayList<MonthReport>> getMonthWiseReportByDate(@Query("fromDate") String fromDate,@Query("toDate") String toDate);

      @POST("getEmployeeWiseReport")
    Call<ArrayList<EmployeeReport>> getEmployeeWiseReport(@Query("empId") int empId,@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getDatePaymentWiseReport")
    Call<ArrayList<DateReport>> getDatePaymentWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getDateWorkWiseReport")
    Call<ArrayList<DateWorkReport>> getDateWorkWiseReport(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("updateStatusWork")
    Call<Info> updateStatusWork(@Query("workId") int workId, @Query("status") int status, @Query("nextDate") String nextDate);



}
