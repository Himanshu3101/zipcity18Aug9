package com.yoeki.iace.societymanagment.Helpline;

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

public class HelplineNo extends AppCompatActivity {
    ProgressDialog PD;

    HelplineRecyclerViewAdapter hadapter;
    private ArrayList<String> HelplineList;
    RecyclerView HelplinerecyclerView;
    AppCompatImageButton bck;
    DBHandler db;
    List<loginObject> HelplineBData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpline_no);

        db = new DBHandler(this);

        bck = (AppCompatImageButton)findViewById(R.id.back);
        HelplinerecyclerView = findViewById(R.id.helpline);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        PD = new ProgressDialog(HelplineNo.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forHelplineContact();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    public void forHelplineContact() {
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindHelpline");

        HashMap<String, String> params = new HashMap<String, String>();
        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray HelplineArray = null;
                HelplineList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           helpline Details
                            HelplineArray = response.getJSONArray("list");
                            HelplineBData = new ArrayList<>();
                            for (int i = 0; i < HelplineArray.length();) {
                                JSONObject HelplineJsonData = HelplineArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Helpline_Name = HelplineJsonData.getString("Name");
                                loginObject_recycler.Helpline_Contact = HelplineJsonData.getString("ContactNo");
                                HelplineBData.add(loginObject_recycler);

                                String H_Name = HelplineBData.get(i).Helpline_Name;
                                String H_contact = HelplineBData.get(i).Helpline_Contact;

                                String HelplineData = H_Name+","+H_contact;
                                HelplineList.add(HelplineData);
                                i++;
                            }
                            PD.dismiss();
                          recycler();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        PD.dismiss();
                        Toast.makeText(HelplineNo.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
        HelplinerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hadapter = new HelplineRecyclerViewAdapter(this, HelplineList);
        HelplinerecyclerView.setAdapter(hadapter);
    }
}



