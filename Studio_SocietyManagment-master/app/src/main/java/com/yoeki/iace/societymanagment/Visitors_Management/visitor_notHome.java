package com.yoeki.iace.societymanagment.Visitors_Management;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class visitor_notHome extends Activity  {
    ProgressDialog PD;
    static DBHandler db;
    Boolean validation;
    static ArrayList<String> NV_FlatListArray;
    EditText Visit_emergencyNo;
//    static AppCompatTextView visit_location;
    AppCompatTextView Nvisit_fdate;
    AppCompatTextView Nvisit_tdate;;
    AppCompatButton BNvisit_fdate,BNvisit_tdate,NewVisit_BtnSubmit;
    static List<String> NV_FlatList;
    String date_time, Com_fromDate, Com_toDate;
    int whichdate=0;

    Calendar myCalendar;
    static String ServiceIds = null;
    ArrayList<String> flatIDS;
    protected String[] flats;
    protected ArrayList<CharSequence> selectedflat = new ArrayList<CharSequence>();
//DatePickerDialog datePickerDialog;

    int fd = 0,td = 0;
    String fromDateAndTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_not_home);

        this.setFinishOnTouchOutside(true);
        db = new DBHandler(this);
        Visit_emergencyNo = (EditText) findViewById(R.id.Visit_emergencyNo);
//        visit_location = (AppCompatTextView) findViewById(R.id.visit_location);
        Nvisit_fdate = (AppCompatTextView) findViewById(R.id.Nvisit_fdate);
        Nvisit_tdate = (AppCompatTextView) findViewById(R.id.Nvisit_tdate);
//        NV_flat = (AppCompatButton)findViewById(R.id.NV_flat);
        BNvisit_fdate = (AppCompatButton) findViewById(R.id.BNvisit_fdate);
        BNvisit_tdate = (AppCompatButton) findViewById(R.id.BNvisit_tdate);
        NewVisit_BtnSubmit = (AppCompatButton) findViewById(R.id.NewVisit_BtnSubmit);

        PD = new ProgressDialog(visitor_notHome.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        myCalendar = Calendar.getInstance();
//        forService();
        NewVisit_BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    Date fdate;
                    Date tdate;
                    String[] from = Com_fromDate.split("\\s+");
                    String[] to = Com_toDate.split("\\s+");

                    try {
                        fdate=new SimpleDateFormat("dd/MM/yyyy").parse(from[0]);
                        tdate=new SimpleDateFormat("dd/MM/yyyy").parse(to[0]);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        Date d1 = sdf.parse(from[1]);
                        Date d2 = sdf.parse(to[1]);
                        long elapsed = d2.getTime() - d1.getTime();
                        String timegap = String.valueOf(elapsed);

                        if(fdate.after(tdate)){
                            Toast.makeText(visitor_notHome.this, "From Date not less then by To date", Toast.LENGTH_SHORT).show();
                        }else if(fdate.before(tdate)){
//                            Toast.makeText(VisitorsNewRequest.this, "From Date is greater then by To date", Toast.LENGTH_SHORT).show();
                            forsubmit();
                        }else if(fdate.equals(tdate)){
                            if (timegap.contains("-")){
                                Toast.makeText(visitor_notHome.this, "Pease Enter Correct time", Toast.LENGTH_SHORT).show();
                            }else if(timegap.equals("0")){
                                Toast.makeText(visitor_notHome.this, "Pease Enter Correct time", Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                    forsubmit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        BNvisit_tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                whichdate=1;
            }
        });

        BNvisit_fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
    }

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        //*************Call Time Picker Here ********************
                        tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        if (whichdate == 1) {
                            Com_toDate = date_time+" "+hourOfDay + ":" + minute;
                            Nvisit_tdate.setText(Com_toDate);
                            whichdate=0;
                        }else{
                            Com_fromDate = date_time+" "+hourOfDay + ":" + minute;
                            Nvisit_fdate.setText(Com_fromDate);
                            whichdate=0;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),VisitorsManagement.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public boolean validations() {
        validation = true;
        if (Visit_emergencyNo.getText().toString().equals("") /*&& visit_location.getText().toString().equals("")*/ &&
                Nvisit_fdate.getText().toString().equals("") && Nvisit_tdate.getText().toString().equals("")) {
            validation = false;
            Visit_emergencyNo.setError("Enter Emergency No.");
//            visit_location.setError("Select Flat Location");
            Nvisit_fdate.setError("Enter From Date");
            Nvisit_tdate.setError("Enter To Date");
        } else if (Visit_emergencyNo.getText().toString().equals("")/*|| Visit_emergencyNo.length()>12*/) {
            validation = false;
            Visit_emergencyNo.setError("Enter Emergency No. & Valid Number");
        }/* else if (visit_location.getText().toString().equals("")) {
            validation = false;
            visit_location.setError("Select Flat Location");
        }*/else if (Nvisit_fdate.getText().toString().equals("")) {
            validation = false;
            Nvisit_fdate.setError("Enter From Date");
        }else if (Nvisit_tdate.getText().toString().equals("")) {
            validation = false;
            Nvisit_tdate.setError("Enter To Date");
        }
        return validation;
    }

    public void forService() {
        NV_FlatList = db.getFlatList();
        NV_FlatListArray = new ArrayList<String>();
        for (final String link : NV_FlatList) {
            String log =  link;
            NV_FlatListArray.add(log);
        }
                String VerName = NV_FlatListArray.toString();
//                visit_location.setText(VerName);
    }

    public void forsubmit(){
        PD.show();
        String FullFromDte = null,FullToDte = null;
        String json_url=(getString(R.string.BASE_URL) + "/ChangeVisitorStatus");

        final String Vndr_EM_Contact = Visit_emergencyNo.getText().toString();
        final String vndr_visitFromdate = Nvisit_fdate.getText().toString();
        final String vndr_visitTodate = Nvisit_tdate.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");


        String[] dt = vndr_visitFromdate.split("/");
        String mnth = dt[0];
        if(mnth.length()==1){
            String month = "0"+mnth;
            FullFromDte = month+"/"+dt[1]+"/"+dt[2];
        }else{
            FullFromDte = vndr_visitFromdate;
        }

        String[] Tot = vndr_visitTodate.split("/");
        String mnthTo = Tot[0];
        if(mnthTo.length()==1){
            String monthl = "0"+mnthTo;
            FullToDte = monthl+"/"+Tot[1]+"/"+Tot[2];
        }else{
            FullToDte = vndr_visitTodate;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);
        params.put("FromDate", FullFromDte);
        params.put("ToDate",FullToDte);
        params.put("EmergencyContactNo",Vndr_EM_Contact);
        params.put("MemberStatus","N");

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray BDetailArray = null;

                        JSONObject loginData = new JSONObject(String.valueOf(response));
                        String resStatus = loginData.getString("status");
                        if(resStatus.equalsIgnoreCase("Success")) {
                            PD.dismiss();
                            Toast.makeText(visitor_notHome.this, "Status Changed Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), VisitorsManagement.class);
                            startActivity(intent);
                            finish();
                        }else{
                            PD.dismiss();
                            Toast.makeText(visitor_notHome.this, "Please try after sometime", Toast.LENGTH_SHORT).show();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(visitor_notHome.this, "Something went Wrong.", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
}