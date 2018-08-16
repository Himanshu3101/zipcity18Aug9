package com.yoeki.iace.societymanagment.GateKeeper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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

public class GateKeeperProfile extends AppCompatActivity {
    List<loginObject> loginBData;
    ProgressDialog PD;
    String UID;
    AppCompatButton Submit;
    AppCompatImageButton gatekeeper_back;
    AppCompatEditText GateK_name,GateK_role,GateK_contactno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gate_keeper_profile);
        GateK_name = (AppCompatEditText)findViewById(R.id.gatekeeper_name);
        GateK_role = (AppCompatEditText)findViewById(R.id.gatekeeper_role);
        GateK_contactno = (AppCompatEditText)findViewById(R.id.gatekeeper_mobile);
        Submit=(AppCompatButton)findViewById(R.id.gatekeeper_submit);
gatekeeper_back=(AppCompatImageButton)findViewById(R.id.gatekeeper_back);
        PD = new ProgressDialog(GateKeeperProfile.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        BindGateKeeperProfile();

        gatekeeper_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GateKeeperProfile.this,GateKeeper.class);
                startActivity(intent);
            }
        });
    }

    public  void BindGateKeeperProfile(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindUserProfile");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray GPDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        String resMessage = loginData.getString("message");
                        try {
//                           Gate keeper profile
                            GPDetailArray = response.getJSONArray("listOwner");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < GPDetailArray.length();) {
                                JSONObject GPDetailJsonData = GPDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.GK_name = GPDetailJsonData.getString("UserName");
                                loginObject_recycler.GK_role = GPDetailJsonData.getString("UserRoleId");
                                loginObject_recycler.GK_contactno = GPDetailJsonData.getString("MobileNo");

                                loginBData.add(loginObject_recycler);

                                String G_Name = loginBData.get(i).GK_name;
                                String G_Role = loginBData.get(i).GK_role;
                                String G_Contactno = loginBData.get(i).GK_contactno;


                                GateK_name.setText(G_Name);
                                GateK_role.setText(G_Role);
                                GateK_contactno.setText(G_Contactno);

                                i++;
                            }
                            PD.dismiss();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        PD.dismiss();
                        Toast.makeText(GateKeeperProfile.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getApplicationContext(),GateKeeper.class);
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



