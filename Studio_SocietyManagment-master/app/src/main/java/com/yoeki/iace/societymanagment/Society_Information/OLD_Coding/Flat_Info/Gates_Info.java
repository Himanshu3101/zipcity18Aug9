package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.GatesRecyclerViewAdapter;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

;

public class Gates_Info extends AppCompatActivity {
    String UID;
    ProgressDialog PD;
    GatesRecyclerViewAdapter adapter;
    RecyclerView gatesRecycle;
    private ArrayList<String> GatesList;
    AppCompatImageButton bck;
    AppCompatTextView TotalGate;
    List<loginObject> GateBData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gates__info);

        gatesRecycle = findViewById(R.id.gatesinfo);
        TotalGate = findViewById(R.id.totalGate);
        bck = (AppCompatImageButton)findViewById(R.id.Soc_gat_Bck) ;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        PD = new ProgressDialog(Gates_Info.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forGateData();

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Society_dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Society_dashboard.class);
        startActivity(intent);
        finish();
    }

    public  void forGateData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindSocietyData");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray GateDetailArray = null;
                GatesList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           Gate Details
                            GateDetailArray = response.getJSONArray("list5");
                            GateBData = new ArrayList<>();
                            for (int i = 0; i < GateDetailArray.length();) {
                                JSONObject GateDetailJsonData = GateDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.GateID = GateDetailJsonData.getString("Id");
                                loginObject_recycler.GateName = GateDetailJsonData.getString("Name");
                                GateBData.add(loginObject_recycler);

                                String Name = GateBData.get(i).GateName;
                                GatesList.add(Name);
                                i++;
                            }
                            PD.dismiss();
                            Recycler();
                            String sizeofGate = String.valueOf(GatesList.size());
                            TotalGate.setText(sizeofGate);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        PD.dismiss();
                        Toast.makeText(Gates_Info.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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

    public void Recycler(){
        gatesRecycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GatesRecyclerViewAdapter(this, GatesList);
        gatesRecycle.setAdapter(adapter);
    }
}
