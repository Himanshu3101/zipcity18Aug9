package com.yoeki.iace.societymanagment.Directory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
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

/**
 * Created by IACE on 22-Aug-18.
 */

public class Directory extends AppCompatActivity {
    AppCompatTextView dire_unit;
    AppCompatButton Btn_dire_unit,dir_submit;
    ProgressDialog PD;
    private ArrayList<String> UnitList;
    DBHandler db;
    List<loginObject> UnitBData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);

        Btn_dire_unit = (AppCompatButton) findViewById(R.id.Btn_dire_unit);
        dir_submit=(AppCompatButton)findViewById(R.id.dir_submit);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

BindRulesData();

dir_submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(Directory.this,DirectoryProfession.class);
        startActivity(intent);
    }
});


    }
    public void BindRulesData() {
        PD.show();
        UnitList = new ArrayList<>();
        String json_url = (getString(R.string.BASE_URL) + "/BindUnitType");

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
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           unit  Details
                            UnitDetailArray = response.getJSONArray("listUnitType");
                            UnitBData = new ArrayList<>();
                            for (int i = 0; i < UnitDetailArray.length();) {
                                JSONObject UnitDetailJsonData = UnitDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.UnitID = UnitDetailJsonData.getString("ProGroupId");
                                loginObject_recycler.Unit_Name = UnitDetailJsonData.getString("Name");
                                UnitBData.add(loginObject_recycler);

                                String U_Id = UnitBData.get(i).UnitID;
                                String U_Name = UnitBData.get(i).Unit_Name;

                                String UnitDetails = U_Id+"&"+U_Name;
                                UnitList.add(UnitDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Directory.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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