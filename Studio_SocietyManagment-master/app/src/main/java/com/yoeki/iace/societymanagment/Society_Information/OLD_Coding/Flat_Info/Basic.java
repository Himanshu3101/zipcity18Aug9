package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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

/**
 * Created by IACE on 11-Jul-18.
 */

public class Basic extends AppCompatActivity {
    AppCompatEditText So_code,So_Name,So_contact_person,So_contact_number,So_address;
    AppCompatImageButton bck;
    List<loginObject> loginBData;
    ProgressDialog PD;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);

        So_code = (AppCompatEditText)findViewById(R.id.S_code);
        So_Name = (AppCompatEditText)findViewById(R.id.S_Name);
        So_contact_person = (AppCompatEditText)findViewById(R.id.contact_person);
        So_contact_number = (AppCompatEditText)findViewById(R.id.contact_number);
        So_address = (AppCompatEditText)findViewById(R.id.address);
        bck = (AppCompatImageButton)findViewById(R.id.Society_back);
        PD = new ProgressDialog(Basic.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Society_dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        forAllData();

    }

    public  void forAllData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindSocietyData");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        String resMessage = loginData.getString("message");
                        try {
//                            Basic Details
                            BDetailArray = response.getJSONArray("list1");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.BDetailCode = BDetailJsonData.getString("Code");
                                loginObject_recycler.BDetailName = BDetailJsonData.getString("Name");
                                loginObject_recycler.BDetailContactPerson1 = BDetailJsonData.getString("ContactPerson1");
                                loginObject_recycler.BDetailMobile = BDetailJsonData.getString("Mobile");
                                loginObject_recycler.BDetailAddress = BDetailJsonData.getString("Address");
                                loginObject_recycler.BDetailPincode = BDetailJsonData.getString("Pincode");
                                loginObject_recycler.BDetailCountryName = BDetailJsonData.getString("CountryName");
                                loginObject_recycler.BDetailStateName = BDetailJsonData.getString("StateName");
                                loginObject_recycler.BDetailCityName = BDetailJsonData.getString("CityName");
                                loginBData.add(loginObject_recycler);

                                String D_BCode = loginBData.get(i).BDetailCode;
                                String D_BName = loginBData.get(i).BDetailName;
                                String D_Contact_person = loginBData.get(i).BDetailContactPerson1;
                                String D_DetailMobile = loginBData.get(i).BDetailMobile;
                                String D_Detail_Address = loginBData.get(i).BDetailAddress;
                                String D_Pin = loginBData.get(i).BDetailPincode;
                                String D_BCountry = loginBData.get(i).BDetailCountryName;
                                String D_BState_Name = loginBData.get(i).BDetailStateName;
                                String D_BCityName = loginBData.get(i).BDetailCityName;

                                So_code.setText(D_BCode);
                                So_Name.setText(D_BName);
                                So_contact_person.setText(D_Contact_person);
                                So_contact_number.setText(D_DetailMobile);
                                So_address.setText(D_Detail_Address);
                                i++;
                            }
                            PD.dismiss();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        PD.dismiss();
                        Toast.makeText(Basic.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Society_dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
