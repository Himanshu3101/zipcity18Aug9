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

public class Alloted_Parking extends Activity {
    AllotedParkingRecyclerViewAdapter padapter;
    private ArrayList<String> ParkingList;
    List<loginObject> ParkingBData;
    RecyclerView ParkingrecyclerView;
    ProgressDialog PD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alloted__parking);

        this.setFinishOnTouchOutside(true);
        ParkingrecyclerView = findViewById(R.id.allotedparking);

        PD = new ProgressDialog(Alloted_Parking.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        BindAllotedParkingData();

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

    public void BindAllotedParkingData() {
        PD.show();
        ParkingList = new ArrayList<>();
        String json_url = (getString(R.string.BASE_URL) + "/BindUserProfile");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray  ParkingDetailArray = null;
                ParkingList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           parking Details
                            ParkingDetailArray = response.getJSONArray("listAssignParking");
                            ParkingBData = new ArrayList<>();
                            for (int i = 0; i < ParkingDetailArray.length();) {
                                JSONObject ParkingDetailJsonData = ParkingDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Parking_no = ParkingDetailJsonData.getString("NoOfCount");

                                ParkingBData.add(loginObject_recycler);

                                String Parking_No = ParkingBData.get(i).Parking_no;

                                String ParkingDetails = Parking_No;
                                ParkingList.add(ParkingDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        recycler();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Alloted_Parking.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
        ParkingrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        padapter = new AllotedParkingRecyclerViewAdapter(this, ParkingList);
        ParkingrecyclerView.setAdapter(padapter);
    }
}



