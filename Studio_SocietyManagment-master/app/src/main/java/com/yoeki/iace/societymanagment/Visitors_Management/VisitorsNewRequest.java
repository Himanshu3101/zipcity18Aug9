package com.yoeki.iace.societymanagment.Visitors_Management;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VisitorsNewRequest extends Activity {
    AppCompatTextView NvisitorR,N_Flat_location,N_fdate,N_tdate;
    EditText N_Name, N_Enter_number, N_address;
    AppCompatButton Nvisitor_type,BtnN_Flat_location,Btn_N_fdate,Btn_N_tdate,New_submit;

    private int mYear, mMonth, mDay, mHour, mMinute;
    static List<String> NewVisit_FlatList,NewVisit_List;
    static ArrayList<String> NewVisit_FlatListArray,NewVisit_ListArray;
    DBHandler db;
    ProgressDialog PD;

    String date_time, VisitIds, LocatIds, Com_fromDate, Com_toDate;
    int whichdate=0;
    Boolean validation;
    String[] dataFlat,visitFlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitors_newrequest);

        db=new DBHandler(this);
        NvisitorR = (AppCompatTextView)findViewById(R.id.NvisitorR);
        N_Flat_location = (AppCompatTextView)findViewById(R.id.N_Flat_location);
        N_fdate = (AppCompatTextView)findViewById(R.id.N_fdate);
        N_tdate = (AppCompatTextView)findViewById(R.id.N_tdate);

        N_Name = (EditText)findViewById(R.id.N_Name);
        N_Enter_number = (EditText)findViewById(R.id.N_Enter_number);
        N_address = (EditText)findViewById(R.id.N_address);

        Nvisitor_type = (AppCompatButton)findViewById(R.id.Nvisitor_type);
        BtnN_Flat_location = (AppCompatButton)findViewById(R.id.BtnN_Flat_location);
        Btn_N_fdate = (AppCompatButton)findViewById(R.id.Btn_NewVisiter);
        Btn_N_tdate = (AppCompatButton)findViewById(R.id.Btn_N_tdate);
        New_submit = (AppCompatButton)findViewById(R.id.New_submit);

        Nvisitor_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewVisit_ListArray = new ArrayList<String>();
                NewVisit_List = db.getVisitList();
                for (final String link : NewVisit_List) {
                    String log = link;
                    NewVisit_ListArray.add(log);
                }

                visitFlat = NewVisit_ListArray.toArray(new String[0]);

                try {
                    new AlertDialog.Builder(VisitorsNewRequest.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Visitor Type")
                            .setSingleChoiceItems(visitFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    ListView lw = ((AlertDialog)dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    NvisitorR.setText(checkedItem.toString());
                                    try{
                                        VisitIds = String.valueOf(db.getVisitListID(checkedItem.toString()));
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        Btn_N_fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        Btn_N_tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichdate=1;
                datePicker();
            }
        });

        BtnN_Flat_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewVisit_FlatListArray = new ArrayList<String>();
                NewVisit_FlatList = db.getFlatList();
                for (final String link : NewVisit_FlatList) {
                    String log = link;
                    NewVisit_FlatListArray.add(log);
                }
                dataFlat = NewVisit_FlatListArray.toArray(new String[0]);
                try {
                    new AlertDialog.Builder(VisitorsNewRequest.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Location")
                            .setSingleChoiceItems(dataFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    ListView lw = ((AlertDialog)dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    N_Flat_location.setText(checkedItem.toString());

                                    try{
                                        LocatIds = String.valueOf(db.getFlatListID(checkedItem.toString()));
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        New_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    Date fdate,tdate;
                    String[] from = Com_fromDate.split("\\s+");
                    String[] to = Com_toDate.split("\\s+");

                    try {
                        fdate=new SimpleDateFormat("dd-MM-yyyy").parse(from[0]);
                        tdate=new SimpleDateFormat("dd-MM-yyyy").parse(to[0]);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        Date d1 = sdf.parse(from[1]);
                        Date d2 = sdf.parse(to[1]);
                        long elapsed = d2.getTime() - d1.getTime();
                        String timegap = String.valueOf(elapsed);

                        if(fdate.after(tdate)){
                            Toast.makeText(VisitorsNewRequest.this, "From Date not less then by To date", Toast.LENGTH_SHORT).show();
                        }
                        if(fdate.before(tdate)){
//                            Toast.makeText(VisitorsNewRequest.this, "From Date is greater then by To date", Toast.LENGTH_SHORT).show();
                            if (!timegap.contains("-")){
                                try {
                                    forSumitionNewVisitordata();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(VisitorsNewRequest.this, "Pease Enter Correct time", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(fdate.equals(tdate)){
//                            Toast.makeText(VisitorsNewRequest.this, "From Date is equal to To date", Toast.LENGTH_SHORT).show();
                            if (!timegap.contains("-")){
                                try {
                                    forSumitionNewVisitordata();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(VisitorsNewRequest.this, "Pease Enter Correct time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        PD = new ProgressDialog(VisitorsNewRequest.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);
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

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
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
                            N_tdate.setText(Com_toDate);
                            whichdate=0;
                        }else{
                            Com_fromDate = date_time+" "+hourOfDay + ":" + minute;
                            N_fdate.setText(Com_fromDate);
                            whichdate=0;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public boolean validations() {
        validation = true;

        if (NvisitorR.getText().toString().equals("") && N_Flat_location.getText().toString().equals("") &&
        N_fdate.getText().toString().equals("") && N_tdate.getText().toString().equals("") &&
        N_Name.getText().toString().equals("") && N_Enter_number.getText().toString().equals("")&&
        N_address.getText().toString().equals(""))

        {
            validation = false;
            NvisitorR.setError("Select Visitor Type");
            N_Flat_location.setError("Select Flat Location");
            N_fdate.setError("Enter From Date");
            N_tdate.setError("Enter To Date");
            N_Name.setError("Enter Visitor Name");
            N_Enter_number.setError("Enter Visitor Number");
            N_address.setError("Enter Visitor Address");

        } else if (NvisitorR.getText().toString().equals("")) {
            validation = false;
            NvisitorR.setError("Select Visitor Type");
        } else if (N_Flat_location.getText().toString().equals("")) {
            validation = false;
            N_Flat_location.setError("Select Flat Location");
        }else if (N_fdate.getText().toString().equals("")) {
            validation = false;
            N_fdate.setError("Enter From Date");
        }else if (N_tdate.getText().toString().equals("")) {
            validation = false;
            N_tdate.setError("Enter To Date");
        }else if (N_Name.getText().toString().equals("")) {
            validation = false;
            N_Name.setError("Enter Visitor Name");
        }else if (N_Enter_number.getText().toString().equals("")) {
            validation = false;
            N_Enter_number.setError("Enter Visitor Number");
        }else if (N_address.getText().toString().equals("")) {
            validation = false;
            N_address.setError("Enter Visitor Address");
        }
        return validation;
    }

    public void forSumitionNewVisitordata(){
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/InsertVisitors");

        String visit_name = N_Name.getText().toString();
        String visit_number = N_Enter_number.getText().toString();
        String visit_add = N_address.getText().toString();
        String from_dte = N_fdate.getText().toString();
        String to_dte = N_tdate.getText().toString();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("VisitorTypeId",VisitIds);
        params.put("Name",visit_name);
        params.put("ContactNo",visit_number);
        params.put("Address",visit_add);
        params.put("FromDate",from_dte);
        params.put("ToDate",to_dte);
        params.put("userId",UID);
        params.put("UnitMasterDetailId",LocatIds);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                        JSONObject loginData = new JSONObject(String.valueOf(response));
                        String resStatus = loginData.getString("status");
                        if (resStatus.equals("Success")){
                            PD.dismiss();
                            Toast.makeText(VisitorsNewRequest.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),VisitorsManagement.class);
                            startActivity(intent);
                        }else{
                            PD.dismiss();
                            Toast.makeText(VisitorsNewRequest.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                    Toast.makeText(VisitorsNewRequest.this, "Something Went Wrong...Please try after sometime", Toast.LENGTH_SHORT).show();
                    Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}
