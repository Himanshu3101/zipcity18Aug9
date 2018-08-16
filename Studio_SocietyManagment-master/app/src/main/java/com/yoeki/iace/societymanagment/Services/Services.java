package com.yoeki.iace.societymanagment.Services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Services extends AppCompatActivity {

    ProgressDialog PD;
    ArrayList<String> listServices  ;
    List<loginObject> ServiceData;
    AppCompatImageButton bck;
    ServicesRecyclerViewAdapter serviceadapter;
    RecyclerView recyclerView;
    private ArrayList<String> ServicesList;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);
        db = new DBHandler(this);
//        ServicesList = new ArrayList<>();
//        ServicesList.add("M");
        bck = (AppCompatImageButton)findViewById(R.id.Serv_bck);
        recyclerView = findViewById(R.id.services);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        PD = new ProgressDialog(Services.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    public void forService() {
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/ServiceSociety");

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray serviceArray = null;
                try {
                    listServices = new ArrayList<>();
                    serviceArray = response.getJSONArray("listService");
                    ServiceData = new ArrayList<>();
                    for (int i = 0; i < serviceArray.length();) {
                        JSONObject complaintJsonData = serviceArray.getJSONObject(i);
                        loginObject loginObject_recycler = new loginObject();
                        loginObject_recycler.ServiceId = complaintJsonData.getString("ServiceId");
                        loginObject_recycler.ServiceName = complaintJsonData.getString("ServiceName");
                        ServiceData.add(loginObject_recycler);
                        listServices.add(String.valueOf(ServiceData.get(i).ServiceName));

                        String ID = ServiceData.get(i).ServiceId;
                        String Name = ServiceData.get(i).ServiceName;
                        db.saveServices(new loginObject(ID, Name));
                        i++;

                    }
//                    services = new String[listServices.size()];
//                    services = listServices.toArray(services);
                    recycler();
                    PD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(), "Something went Wrong :"+error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        try {
            MyApplication.getInstance().addToReqQueue(req,"BottomService");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void recycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceadapter = new ServicesRecyclerViewAdapter(this, listServices);
        recyclerView.setAdapter(serviceadapter);
    }
}
