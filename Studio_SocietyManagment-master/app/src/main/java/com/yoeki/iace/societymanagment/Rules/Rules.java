package com.yoeki.iace.societymanagment.Rules;

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
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rules extends AppCompatActivity {
    RulesRecyclerViewAdapter radapter;
    AppCompatImageButton r_back;
    private ArrayList<String> RulesList;
    List<loginObject> RulesBData;
    RecyclerView RulesrecyclerView;
    ProgressDialog PD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule);

      RulesrecyclerView = findViewById(R.id.rules);
        r_back = (AppCompatImageButton)findViewById(R.id.r_back) ;

        PD = new ProgressDialog(Rules.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        r_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        BindRulesData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    public void BindRulesData() {
        PD.show();
        RulesList = new ArrayList<>();
        String json_url = (getString(R.string.BASE_URL) + "/BindRules");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");
        HashMap<String, String> params = new HashMap<String, String>();


        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray RulesDetailArray = null;
                RulesList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           rules Details
                            RulesDetailArray = response.getJSONArray("list");
                            RulesBData = new ArrayList<>();
                            for (int i = 0; i < RulesDetailArray.length();) {
                                JSONObject RulesDetailJsonData = RulesDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Rules_title = RulesDetailJsonData.getString("Title");
                                loginObject_recycler.Rules_description = RulesDetailJsonData.getString("Description");
                                RulesBData.add(loginObject_recycler);

                                String R_title = RulesBData.get(i).Rules_title;
                                String R_desc = RulesBData.get(i).Rules_description;

                                String RuleDetails = R_title+"&"+R_desc;
                                RulesList.add(RuleDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        recycler();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Rules.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
        RulesrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        radapter = new RulesRecyclerViewAdapter(this, RulesList);
        RulesrecyclerView.setAdapter(radapter);
    }
}