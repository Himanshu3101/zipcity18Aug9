package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.GeneralLiftRecyclerAdapter;
import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.ServiceLiftRecyclerAdapter;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lift_Info extends AppCompatActivity{
    String UID;
    AppCompatImageButton bck;
    List<loginObject> LiftBData;
    ProgressDialog PD;
    RecyclerView GeneralRecycler,ServiceRecycler;
    GeneralLiftRecyclerAdapter gadapter;
    ServiceLiftRecyclerAdapter sadapter;
    private ArrayList<String> Generallift;
    private ArrayList<String> Servicelift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lift__info);

        GeneralRecycler = findViewById(R.id.generallift);
        ServiceRecycler = findViewById(R.id.servicelift);
        bck = (AppCompatImageButton)findViewById(R.id.Soc_lift_bck) ;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        PD = new ProgressDialog(Lift_Info.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forLiftData();

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

    public  void forLiftData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindSocietyData");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray LftDetailArray = null;
                Generallift = new ArrayList<>();
                Servicelift = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        String resMessage = loginData.getString("message");
                        try {
//                           Lift Details
                            LftDetailArray = response.getJSONArray("list6");
                            LiftBData = new ArrayList<>();
                            for (int i = 0; i < LftDetailArray.length();) {
                                JSONObject LiftDetailJsonData = LftDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.LiftCode = LiftDetailJsonData.getString("Id");
                                loginObject_recycler.LiftName = LiftDetailJsonData.getString("Name");
                                loginObject_recycler.LiftType = LiftDetailJsonData.getString("LiftType");
                                LiftBData.add(loginObject_recycler);

                                String Name = LiftBData.get(i).LiftName;
                                String Lift_type = LiftBData.get(i).LiftType;
                                String FullName = Name+","+Lift_type;

                                if(Lift_type.equalsIgnoreCase("General Lift")){
                                    Generallift.add(FullName);
                                }else{
                                    Servicelift.add(FullName);
                                }
                                i++;
                            }
                            PD.dismiss();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        PD.dismiss();
                        Toast.makeText(Lift_Info.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
        GeneralRecycler.setLayoutManager(new LinearLayoutManager(this));
        gadapter = new GeneralLiftRecyclerAdapter(this, Generallift);
        GeneralRecycler.setAdapter(gadapter);

        ServiceRecycler.setLayoutManager(new LinearLayoutManager(this));
        sadapter = new ServiceLiftRecyclerAdapter(this, Servicelift);
        ServiceRecycler.setAdapter(sadapter);
    }
}
