package com.yoeki.iace.societymanagment.Services;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceDetails extends AppCompatActivity {

    ServicesDetailsRecyclerViewAdapter servicesdetailsadapter;
    AppCompatTextView title_serv;
    Button serv_detail_bck;
    private ArrayList<String> Ven_List;
    DBHandler dbHandler;
    ProgressDialog PD;
    String ServiceIds;
    List<loginObject> loginBData;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_details);

        dbHandler = new DBHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.profile);
        serv_detail_bck= (Button) findViewById(R.id.serv_detail_bck);
        title_serv = (AppCompatTextView) findViewById(R.id.title_serv);

        Intent intent = getIntent();
        String ser_Name = intent.getStringExtra("service_select");
        title_serv.setText(ser_Name);
        ServiceIds = String.valueOf(dbHandler.getServiceListID(ser_Name));

        PD = new ProgressDialog(ServiceDetails.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        serv_detail_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Services.class);
                startActivity(intent);
                finish();
            }
        });

        forSelectedService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Services.class);
        startActivity(intent);
        finish();
    }

    public void forSelectedService(){

        PD.show();
        Ven_List = new ArrayList<>();
        String json_url=(getString(R.string.BASE_URL) + "/BindVendorVIdNew");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);
        params.put("ServiceId",ServiceIds);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray ProfileDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

//                           Owner Details
                        try {
                            ProfileDetailArray = response.getJSONArray("list");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < ProfileDetailArray.length();) {
                                JSONObject BDetailJsonData = ProfileDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Vendor_name = BDetailJsonData.getString("Name");
                                loginObject_recycler.Vendor_Contct = BDetailJsonData.getString("ContactNo");
                                loginObject_recycler.Vendor_Creat = BDetailJsonData.getString("CreatedOn");
                                loginObject_recycler.Vendor_Rating = BDetailJsonData.getString("Rating");
                                loginObject_recycler.Vendor_Status = BDetailJsonData.getString("LiveStatus");
                                loginBData.add(loginObject_recycler);

                                String V_name = loginBData.get(i).Vendor_name;
                                String V_cntct = loginBData.get(i).Vendor_Contct;
                                String V_create = loginBData.get(i).Vendor_Creat;
                                String V_rating = loginBData.get(i).Vendor_Rating;
                                String V_status = loginBData.get(i).Vendor_Status;


                                String Fulldetails = V_name+","+V_create+","+V_cntct+","+V_rating+","+V_status;
                                Ven_List.add(Fulldetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        recycler();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(ServiceDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    public void recycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        servicesdetailsadapter = new ServicesDetailsRecyclerViewAdapter(this, Ven_List);
        recyclerView.setAdapter(servicesdetailsadapter);
    }
}
