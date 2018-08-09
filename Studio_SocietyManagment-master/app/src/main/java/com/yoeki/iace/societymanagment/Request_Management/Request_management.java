package com.yoeki.iace.societymanagment.Request_Management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Adapter.RequestAdapter.RequestRecyclerViewAdapter;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Request_management extends AppCompatActivity {
    RequestRecyclerViewAdapter radapter;
    private ArrayList<String> RequestList;
    RecyclerView requestRecycler;
    String UID;
    AppCompatImageButton bck;
    List<loginObject> RequestBData;
    ProgressDialog PD;
    FloatingActionButton fab;
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_management);
        db = new DBHandler(this);
        requestRecycler = findViewById(R.id.request);
        bck = (AppCompatImageButton)findViewById(R.id.req_bck) ;
         fab = (FloatingActionButton) findViewById(R.id.Rfab);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        PD = new ProgressDialog(Request_management.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forRequestData();

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> UserRoleID = db.getRoleID();
                ArrayList<String> finalRoleIDList = new ArrayList<>();

                for (final String link : UserRoleID) {
                    String log = link;
                    finalRoleIDList.add(log);
                }

                if (finalRoleIDList.contains("1") || finalRoleIDList.contains("3")){
                    Intent intent = new Intent(Request_management.this, RequestCreated.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Request_management.this, "You are not Authorized Member", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    public  void forRequestData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindRequests");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray RequestArray = null;

                RequestList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        try {
//                           Request Details
                            RequestArray = response.getJSONArray("list");
                            RequestBData = new ArrayList<>();
                            for (int i = 0; i < RequestArray.length();) {
                                JSONObject RequestDetailJsonData = RequestArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();
                                loginObject_recycler.ReqTitle = RequestDetailJsonData.getString("title");
                                loginObject_recycler.ReqCreated = RequestDetailJsonData.getString("createdOn");
                                loginObject_recycler.ReqDescription = RequestDetailJsonData.getString("description");
                                loginObject_recycler.ReqUnit = RequestDetailJsonData.getString("UnitNo");
                                loginObject_recycler.ReqAssigen = RequestDetailJsonData.getString("assignedTo");
                                loginObject_recycler.ReqStatus = RequestDetailJsonData.getString("status");
                                loginObject_recycler.ReqType = RequestDetailJsonData.getString("type");
                                loginObject_recycler.ReqID = RequestDetailJsonData.getString("requestId");
                                loginObject_recycler.ReqClosedID = RequestDetailJsonData.getString("UniqueCode");
                                RequestBData.add(loginObject_recycler);

                                String Title = RequestBData.get(i).ReqTitle;
                                String created = RequestBData.get(i).ReqCreated;
                                String desc = RequestBData.get(i).ReqDescription;
                                String unit = RequestBData.get(i).ReqUnit;
                                String assigen = RequestBData.get(i).ReqAssigen;
                                String status = RequestBData.get(i).ReqStatus;
                                String type = RequestBData.get(i).ReqType;
                                String reqID = RequestBData.get(i).ReqID;
                                String reqcloID = RequestBData.get(i).ReqClosedID;

                                String FullName = Title+","+created+","+desc+","+unit+","+assigen+","+status+","+type+","+reqID+","+reqcloID;
                                RequestList.add(FullName);
                                i++;
                            }
                            PD.dismiss();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        PD.dismiss();
                        Toast.makeText(Request_management.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                    Recycler();
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

    public void Recycler(){
        requestRecycler.setLayoutManager(new LinearLayoutManager(this));
        radapter = new RequestRecyclerViewAdapter(this, RequestList);
        requestRecycler.setAdapter(radapter);
    }

}

