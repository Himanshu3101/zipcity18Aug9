package com.yoeki.iace.societymanagment.Request_Management;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IACE on 18-Jul-18.
 */

public class Popup_Request extends Activity {
    AppCompatTextView title, closedView;
    AppCompatEditText created, description, closed_ID;
    AppCompatButton accept, closed;
    String state, requestID, cancelID;
    ProgressDialog PD;
    DBHandler db;
    String UID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        db = new DBHandler(this);
        title = (AppCompatTextView) findViewById(R.id.titleofrespo);
        closedView = (AppCompatTextView) findViewById(R.id.closedView);
        closed_ID = (AppCompatEditText) findViewById(R.id.closed_ID);
        created = (AppCompatEditText) findViewById(R.id.createdon);
        description = (AppCompatEditText) findViewById(R.id.description);
        accept = (AppCompatButton) findViewById(R.id.accept);
        closed = (AppCompatButton) findViewById(R.id.cancel);

        PD = new ProgressDialog(Popup_Request.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        Intent intent = getIntent();

        String tit = intent.getStringExtra("Title");
        String creat = intent.getStringExtra("Created");
        String descr = intent.getStringExtra("Description");
        state = intent.getStringExtra("Statuss");
        requestID = intent.getStringExtra("RequestID");
        cancelID = intent.getStringExtra("CancelID");
        title.setText(tit);
        created.setText(creat);
        description.setText(descr);


        List<String> UserRoleID = db.getRoleID();
        ArrayList<String> finalRoleIDList = new ArrayList<>();

        for (final String link : UserRoleID) {
            String log = link;
            finalRoleIDList.add(log);
        }

        if (finalRoleIDList.contains("4")) {
            if (state.equalsIgnoreCase("Created")) {
                accept.setVisibility(View.VISIBLE);
            }else if (state.equalsIgnoreCase("Accepted")) {
                closed.setVisibility(View.VISIBLE);
                accept.setVisibility(View.INVISIBLE);
            }else{
                closed.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
            }
        }else if (finalRoleIDList.contains("3")) {
            if (state.equalsIgnoreCase("Created")) {
                closed_ID.setText(cancelID);
                closed_ID.setVisibility(View.VISIBLE);
                closedView.setVisibility(View.VISIBLE);
                closed.setVisibility(View.VISIBLE);
            }else if (state.equalsIgnoreCase("Accepted")) {
                closed_ID.setText(cancelID);
                closed_ID.setVisibility(View.VISIBLE);
                closedView.setVisibility(View.VISIBLE);
                closed.setVisibility(View.VISIBLE);
                accept.setVisibility(View.INVISIBLE);
            }else{
                closed.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
            }
        }else if (finalRoleIDList.contains("1")) {
            if (state.equalsIgnoreCase("Created")) {
                closed.setVisibility(View.INVISIBLE);
            }else if (state.equalsIgnoreCase("Accepted")) {
                closed.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
            }else{
                closed.setVisibility(View.INVISIBLE);
                accept.setVisibility(View.INVISIBLE);
            }
        }

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foraccept();
                }
            });

        closed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forClosed();
                }
            });

        }

    public  void foraccept(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/ChangeStatus");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Id",requestID);
        params.put("Status","A");
        params.put("Flag","R");
        params.put("userId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("CurrentStatus");
                    if (resStatus.equalsIgnoreCase("Accepted")) {
                        PD.dismiss();
                        Toast.makeText(Popup_Request.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Request_management.class);
                        startActivity(intent);
                    }else{
                        PD.dismiss();
                        Toast.makeText(Popup_Request.this, "Request Not Accepted", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }

    public  void forClosed(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/ChangeStatus");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Id",requestID);
        params.put("Status","R");
        params.put("Flag","R");
        params.put("userId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("CurrentStatus");
                    if (resStatus.equalsIgnoreCase("Closed")) {
                        PD.dismiss();
                        Toast.makeText(Popup_Request.this, "Request Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Request_management.class);
                        startActivity(intent);
                    }else{
                        PD.dismiss();
                        Toast.makeText(Popup_Request.this, "Request Not Closed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}
