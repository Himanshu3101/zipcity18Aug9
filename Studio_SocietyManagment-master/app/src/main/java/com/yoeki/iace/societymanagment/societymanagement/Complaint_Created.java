package com.yoeki.iace.societymanagment.societymanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Complaint_Created extends Activity {
    AppCompatTextView Complaint_Type, Complaint_Location;
    EditText C_Title, C_Description;
    AppCompatButton C_ComplaintType, C_FlatLocation, Submit;
    DBHandler db;
    ProgressDialog PD;
    static List<String> FlatList, complaintList;
    static ArrayList<String> FlatListArray, ComplaintReqArray;
    String[] dataFlat, ComplaintFlat;
    String ComplaintIds, LocatIds;
    Boolean validation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_created);

        this.setFinishOnTouchOutside(true);
        db = new DBHandler(this);
        Complaint_Type = (AppCompatTextView) findViewById(R.id.complaint);
        Complaint_Location = (AppCompatTextView) findViewById(R.id.location);

        C_Title = (EditText) findViewById(R.id.ctitle);
        C_Description = (EditText) findViewById(R.id.cdesc);

        C_ComplaintType = (AppCompatButton) findViewById(R.id.complainttype);
        C_FlatLocation = (AppCompatButton) findViewById(R.id.complaintlocation);
        Submit = (AppCompatButton) findViewById(R.id.csubmit);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        C_ComplaintType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.show();
                complaintList = new ArrayList<String>();
                ComplaintReqArray = new ArrayList<String>();
                complaintList = db.getComplaintList();
                for (final String list : complaintList) {
                    String log = list;
                    ComplaintReqArray.add(log);

                }
                ComplaintFlat = ComplaintReqArray.toArray(new String[0]);

                try {
                    new AlertDialog.Builder(Complaint_Created.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Complaint Type")
                            .setSingleChoiceItems(ComplaintFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    ListView lw = ((AlertDialog) dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    Complaint_Type.setText(checkedItem.toString());
                                    PD.dismiss();
                                    try {
                                        ComplaintIds = String.valueOf(db.getComplaintListID(checkedItem.toString()));
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
        C_FlatLocation.setOnClickListener(new View.OnClickListener() {
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
                    new AlertDialog.Builder(Complaint_Created.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Location")
                            .setSingleChoiceItems(dataFlat, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    ListView lw = ((AlertDialog)dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    Complaint_Location.setText(checkedItem.toString());
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
                    forSumitionNewComplaintdata();
                }

            }
        });
    }
    public boolean validations() {
        validation = true;

        if (Complaint_Type.getText().toString().equals("") && Complaint_Location.getText().toString().equals("") &&
                C_Title.getText().toString().equals("") && C_Description.getText().toString().equals(""))

        {
            validation = false;
            Complaint_Type.setError("Select Complaint Type");
            Complaint_Location.setError("Select Flat Location");
            C_Title.setError("Enter Title");
            C_Description.setError("Enter Description");


        } else if (Complaint_Type.getText().toString().equals("")) {
            validation = false;
            Complaint_Type.setError("Select Complaint Type");
        } else if (Complaint_Location.getText().toString().equals("")) {
            validation = false;
            Complaint_Location.setError("Select Flat Location");
        } else if (C_Title.getText().toString().equals("")) {
            validation = false;
            C_Title.setError("Enter Title");
//        }else if (C_Description.getText().toString().equals("")) {
//            validation = false;
//            C_Description.setError("Enter Description");
        }
        return validation;
    }
    public void forSumitionNewComplaintdata(){
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/InsertComplaints");

        String Complaint_title = C_Title.getText().toString();
        String Complaint_descr = C_Description.getText().toString();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("CreatedBy",UID);
        params.put("ComplainType",ComplaintIds);
        params.put("Title",Complaint_title);
        params.put("Description",Complaint_descr);
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
                        Toast.makeText(Complaint_Created.this, "Complaint Submitted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),SocietyManagement.class);
                        startActivity(intent);
                    }else{
                        PD.dismiss();
                        Toast.makeText(Complaint_Created.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(Complaint_Created.this, "Something Went Wrong...Please try after sometime", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
}


