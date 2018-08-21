package com.yoeki.iace.societymanagment.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Alloted_Unit extends Activity {
    AllotedUnitRecyclerViewAdapter uadapter;
    private ArrayList<String> UnitList;
    List<loginObject>  UnitBData;
    RecyclerView UnitrecyclerView;
    ProgressDialog PD;
    private ArrayList<String> ChargesList;
    public static final String MY_PREFS_NAME = "ChargesList";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alloted__unit);
        this.setFinishOnTouchOutside(true);

        UnitrecyclerView = findViewById(R.id.allotedunit);


        PD = new ProgressDialog(Alloted_Unit.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        BindAllotedUnitData();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Profile.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void BindAllotedUnitData() {
        PD.show();
        UnitList = new ArrayList<>();
        String json_url = (getString(R.string.BASE_URL) + "/BindUserProfile");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray UnitDetailArray = null;
                UnitList = new ArrayList<>();
                ChargesList=new ArrayList<>();

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        //  Alloted unit Details
                        try {
                            UnitDetailArray = response.getJSONArray("listAssignUnits");
                            UnitBData = new ArrayList<>();
                            for (int i = 0; i < UnitDetailArray.length();) {
                                JSONObject UnitDetailJsonData = UnitDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Unit_no = UnitDetailJsonData.getString("NoOfCount");
                                loginObject_recycler.Unit_status = UnitDetailJsonData.getString("UnitStatus");

                                UnitBData.add(loginObject_recycler);
                                String U_no = UnitBData.get(i).Unit_no;
                                String U_status = UnitBData.get(i).Unit_status;

                                String UnitDetails = U_no +"&"+U_status;
                                UnitList.add(UnitDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
//charges details
                        try {
                            UnitDetailArray = response.getJSONArray("listUnitWiseChargesDetail");
                            UnitBData = new ArrayList<>();
                            for (int i = 0; i < UnitDetailArray.length();) {
                                JSONObject ChargesDetailJsonData = UnitDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Charges_Type = ChargesDetailJsonData.getString("ChargeName");
                                loginObject_recycler.Charges_Amount = ChargesDetailJsonData.getString("Value");

                                UnitBData.add(loginObject_recycler);
                                String CH_TYPE = UnitBData.get(i).Charges_Type;
                                String CH_AMOUNT = UnitBData.get(i).Charges_Amount;

                                String ChargesDetails = CH_TYPE+"&"+CH_AMOUNT;
                                ChargesList.add(ChargesDetails);
                                i++;
                            }




                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        recycler();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Alloted_Unit.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    PD.dismiss();
                    Toast.makeText(Alloted_Unit.this, "Please try after some time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Log.w("error in response", "Error: " + error.getMessage());
                Toast.makeText(Alloted_Unit.this, "Server_Error -"+error, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
                finish();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }

    public void recycler(){
        try {
            UnitrecyclerView.setLayoutManager(new LinearLayoutManager(this));
            uadapter = new AllotedUnitRecyclerViewAdapter(this, ChargesList,UnitList);
            UnitrecyclerView.setAdapter(uadapter);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}




