package com.yoeki.iace.societymanagment.societymanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
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

public class RequestManagement_Created extends Activity {
    AppCompatTextView Request_Type, Flat_Location, R_fdate, R_tdate;
    EditText R_Title, R_Description;
    AppCompatButton R_RequestType, R_FlatLocation, Submit, Btn_ReqRequest, Btn_R_tdate;
    DBHandler db;
    int whichdate=0;
    ProgressDialog PD;
    static List<String> FlatList, requestList;
    static ArrayList<String> FlatListArray, RequestReqArray;
    String[] dataFlat, RequestFlat;
    String RequestIds, LocatIds;
    Boolean validation;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date_time, Com_fromDate, Com_toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_management__created);

        this.setFinishOnTouchOutside(true);
        db = new DBHandler(this);
        Request_Type = (AppCompatTextView) findViewById(R.id.request);
        Flat_Location = (AppCompatTextView) findViewById(R.id.location);
        R_fdate = (AppCompatTextView) findViewById(R.id.R_fdate);
        R_tdate = (AppCompatTextView) findViewById(R.id.R_tdate);

        R_Title = (EditText) findViewById(R.id.rtitle);
        R_Description = (EditText) findViewById(R.id.rdesc);

        R_RequestType = (AppCompatButton) findViewById(R.id.rtype);
        R_FlatLocation = (AppCompatButton) findViewById(R.id.location_type);
        Btn_ReqRequest = (AppCompatButton) findViewById(R.id.Btn_ReqRequest);
        Btn_R_tdate = (AppCompatButton) findViewById(R.id.Btn_R_tdate);
        Submit = (AppCompatButton) findViewById(R.id.submit);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        R_RequestType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.show();
                requestList = new ArrayList<String>();
                RequestReqArray = new ArrayList<String>();
                requestList = db.getReqList();
                for (final String list : requestList) {
                    String log = list;
                    RequestReqArray.add(log);

                }
                RequestFlat = RequestReqArray.toArray(new String[0]);

                try {
                    new AlertDialog.Builder(RequestManagement_Created.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Request Type")
                            .setSingleChoiceItems(RequestFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    ListView lw = ((AlertDialog) dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    Request_Type.setText(checkedItem.toString());
                                    PD.dismiss();
                                    try {
                                        RequestIds = String.valueOf(db.getReqListID(checkedItem.toString()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        R_FlatLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.show();
                FlatList = new ArrayList<String>();
                FlatListArray = new ArrayList<String>();
                FlatList = db.getFlatList();
                for (final String link : FlatList) {
                    String log = link;
                    FlatListArray.add(log);
                }
                dataFlat = FlatListArray.toArray(new String[0]);
                try {
                    new AlertDialog.Builder(RequestManagement_Created.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Location")
                            .setSingleChoiceItems(dataFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    ListView lw = ((AlertDialog)dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    Flat_Location.setText(checkedItem.toString());
                                    PD.dismiss();

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

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();

                if (validation == true) {
                    Date fdate,tdate;
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
                            Toast.makeText(RequestManagement_Created.this, "From Date not less then by To date", Toast.LENGTH_SHORT).show();
                        }
                        if(fdate.before(tdate)){
//                            Toast.makeText(VisitorsNewRequest.this, "From Date is greater then by To date", Toast.LENGTH_SHORT).show();
                            if (!timegap.contains("-")){
                                try {
                                    forSumitionNewRequestdata();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(RequestManagement_Created.this, "Pease Enter Correct time", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(fdate.equals(tdate)){
//                            Toast.makeText(VisitorsNewRequest.this, "From Date is equal to To date", Toast.LENGTH_SHORT).show();
                            if (!timegap.contains("-")){
                                try {
                                    forSumitionNewRequestdata();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(RequestManagement_Created.this, "Please Enter Correct time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Btn_ReqRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        Btn_R_tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                whichdate=1;
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
                            R_tdate.setText(Com_toDate);
                            whichdate=0;
                        }else{
                            Com_fromDate = date_time+" "+hourOfDay + ":" + minute;
                            R_fdate.setText(Com_fromDate);
                            whichdate=0;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public boolean validations() {
        validation = true;

        if (Request_Type.getText().toString().equals("") && Flat_Location.getText().toString().equals("") &&
                R_Title.getText().toString().equals("") && R_Description.getText().toString().equals("") &&
                R_tdate.getText().toString().equals("") && R_fdate.getText().toString().equals(""))

        {
            validation = false;
            Request_Type.setError("Select Request Type");
            Flat_Location.setError("Select Flat Location");
            R_Title.setError("Enter Title");
            R_Description.setError("Enter Description");
            R_tdate.setError("Enter Date & TIme");
            R_fdate.setError("Enter Date & TIme");


        } else if (Request_Type.getText().toString().equals("")) {
            validation = false;
            Request_Type.setError("Select Request Type");
        } else if (Flat_Location.getText().toString().equals("")) {
            validation = false;
            Flat_Location.setError("Select Flat Location");
        }else if (R_Title.getText().toString().equals("")) {
            validation = false;
            R_Title.setError("Enter Title");
        }else if (R_Description.getText().toString().equals("")) {
            validation = false;
            R_Description.setError("Enter Description");
        }else if (R_fdate.getText().toString().equals("")) {
            validation = false;
            R_fdate.setError("Enter Date & TIme");
        }else if (R_tdate.getText().toString().equals("")) {
            validation = false;
            R_tdate.setError("Enter Date & TIme");
        }
        return validation;
    }


    public void forSumitionNewRequestdata(){
        PD.show();
        String FullFromDte = null,FullToDte = null;
        String  json_url = (getString(R.string.BASE_URL) + "/InsertRequest");

        String Request_title = R_Title.getText().toString();
        String Request_descr = R_Description.getText().toString();

        String fromdte = R_fdate.getText().toString();
        String todte = R_tdate.getText().toString();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        String[] dt = fromdte.split("/");
        String mnth = dt[0];
        if(mnth.length()==1){
            String month = "0"+mnth;
            FullFromDte = month+"/"+dt[1]+"/"+dt[2];
        }else{
            FullFromDte = fromdte;
        }

        String[] Tot = todte.split("/");
        String mnthTo = Tot[0];
        if(mnthTo.length()==1){
            String monthl = "0"+mnthTo;
            FullToDte = monthl+"/"+Tot[1]+"/"+Tot[2];
        }else{
            FullToDte = todte;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("CreatedBy",UID);
        params.put("RequestType",RequestIds);
        params.put("Title",Request_title);
        params.put("Description",Request_descr);
        params.put("VendorId", "0");
        params.put("UnitMasterDetailId",LocatIds);
        params.put("FromDate", FullFromDte);
        params.put("ToDate", FullToDte);


        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equals("Success")){
                        PD.dismiss();
                        Toast.makeText(RequestManagement_Created.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),SocietyManagement.class);
                        startActivity(intent);
                    }else{
                        PD.dismiss();
                        Toast.makeText(RequestManagement_Created.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(RequestManagement_Created.this, "Something Went Wrong...Please try after sometime", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}


