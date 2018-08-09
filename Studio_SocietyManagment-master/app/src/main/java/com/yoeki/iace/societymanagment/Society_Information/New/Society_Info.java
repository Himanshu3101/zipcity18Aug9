package com.yoeki.iace.societymanagment.Society_Information.New;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IACE on 31-Jul-18.
 */

public class Society_Info extends AppCompatActivity {
    ImageButton Soc_bck;
    ProgressDialog PD;
    String UID;
    AppCompatTextView S_code,So_Name,Lift_G_No,Lift_S_No,PARKING_C_No,PARKING_S_No;
    List<loginObject> loginBData;
    GridView SocietyInfogrid;
    GridView Facilitygrid;

   ArrayList <String> UnitTypeInfo;
    ArrayList <String> FacilityInfogrid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.society_info);

        Soc_bck = (ImageButton)findViewById(R.id.New_Soc_bck);

        S_code = (AppCompatTextView)findViewById(R.id.S_code);
        So_Name = (AppCompatTextView)findViewById(R.id.So_Name);
        Lift_G_No = (AppCompatTextView)findViewById(R.id.Lift_G_No);
        Lift_S_No = (AppCompatTextView)findViewById(R.id.Lift_S_No);
        PARKING_C_No = (AppCompatTextView)findViewById(R.id.PARKING_C_No);
        PARKING_S_No = (AppCompatTextView)findViewById(R.id.PARKING_S_No);

        Soc_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        PD = new ProgressDialog(Society_Info.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        forAllData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public  void forAllData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindSocietyDataNew");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;
                UnitTypeInfo = new ArrayList<String>();
                FacilityInfogrid = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
//                            Basic Details
                        try {
                            BDetailArray = response.getJSONArray("list1");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Society_code = BDetailJsonData.getString("Code");
                                loginObject_recycler.Society_name = BDetailJsonData.getString("Name");

                                loginBData.add(loginObject_recycler);

                                String D_BCode = loginBData.get(i).Society_code;
                                String D_BName = loginBData.get(i).Society_name;

                                S_code.setText(D_BCode);
                                So_Name.setText(D_BName);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
//                            Lift Details
                        try {
                            BDetailArray = response.getJSONArray("list3");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Lift_Type = BDetailJsonData.getString("Type");
                                loginObject_recycler.Lift_Count = BDetailJsonData.getString("Count");

                                loginBData.add(loginObject_recycler);

                                String L_Type = loginBData.get(i).Lift_Type;
                                String L_Count = loginBData.get(i).Lift_Count;

                                if (L_Type.equals("General Lift")) {
                                    Lift_G_No.setText(L_Count);
                                }else{
                                    Lift_S_No.setText(L_Count);
                                }

                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
//                            Parking Details
                        try {
                            BDetailArray = response.getJSONArray("list4");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Parking_Type = BDetailJsonData.getString("Type");
                                loginObject_recycler.Parking_Count = BDetailJsonData.getString("Count");

                                loginBData.add(loginObject_recycler);

                                String L_Type = loginBData.get(i).Parking_Type;
                                String L_Count = loginBData.get(i).Parking_Count;

                                if (L_Type.equals("Comman")) {
                                    PARKING_C_No.setText(L_Count);
                                }else{
                                    PARKING_S_No.setText(L_Count);
                                }

                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        // Unit Details
                        try {
                            BDetailArray = response.getJSONArray("list2");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();
                                loginObject_recycler.Unit_Type = BDetailJsonData.getString("Type");
                                loginObject_recycler.Unit_Count = BDetailJsonData.getString("Count");
                                loginBData.add(loginObject_recycler);

                                String L_Type = loginBData.get(i).Unit_Type;
                                String L_Count = loginBData.get(i).Unit_Count;

                                String FullyUnit = L_Type+","+L_Count;
                                UnitTypeInfo.add(FullyUnit);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        // Facility Details
                        try {
                            BDetailArray = response.getJSONArray("listFacilityDetails");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();
                                loginObject_recycler.Facility_Type = BDetailJsonData.getString("Type");
                                loginObject_recycler.Facility_Count = BDetailJsonData.getString("Count");
                                loginBData.add(loginObject_recycler);

                                String L_Type = loginBData.get(i).Facility_Type;
                                String L_Count = loginBData.get(i).Facility_Count;

                                String FullyUnit = L_Type+","+L_Count;
                                FacilityInfogrid.add(FullyUnit);
                                i++;
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        PD.dismiss();
                        recycler();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Society_Info.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(Society_Info.this, "Server_Error", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }

    public void recycler(){

       try {
            CustomGridSocietyInfo adapter = new CustomGridSocietyInfo(Society_Info.this, UnitTypeInfo);
            SocietyInfogrid = (GridView) findViewById(R.id.Society_gridview);
            SocietyInfogrid.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }

         try {
            CustomGridFacilityInfo adapter0 = new CustomGridFacilityInfo(Society_Info.this, FacilityInfogrid);
            Facilitygrid=(GridView)findViewById(R.id.Facility_gridview);
            Facilitygrid.setAdapter(adapter0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
