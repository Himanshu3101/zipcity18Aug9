package com.yoeki.iace.societymanagment.Circular;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class Circular extends AppCompatActivity {
    List<loginObject> CircularBData;

    CircularRecyclerViewAdapter cadapter;
    private ArrayList<String> CircularList;
    RecyclerView CircularrecyclerView;
    AppCompatImageButton Circ_bck;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular);

        CircularrecyclerView = findViewById(R.id.circular);
        Circ_bck = findViewById(R.id.Circ_bck);

        Circ_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        PD = new ProgressDialog(Circular.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        BindCircularData();

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

        public void BindCircularData() {
            PD.show();
            CircularList = new ArrayList<>();
            String json_url = (getString(R.string.BASE_URL) + "/BindCircular");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String UID = prefs.getString("UserID", " ");
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId",UID);

            JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                    params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray CircularDetailArray = null;
                    CircularList = new ArrayList<>();
                    try {
                        JSONObject loginData = new JSONObject(String.valueOf(response));
                        String resStatus = loginData.getString("status");
                        if (resStatus.equalsIgnoreCase("Success")) {
                            try {
//                           circular Details
                                CircularDetailArray = response.getJSONArray("list");
                                CircularBData = new ArrayList<>();
                                for (int i = 0; i < CircularDetailArray.length();) {
                                    JSONObject CircularDetailJsonData = CircularDetailArray.getJSONObject(i);
                                    loginObject loginObject_recycler = new loginObject();

                                    loginObject_recycler.Cir_title = CircularDetailJsonData.getString("title");
                                    loginObject_recycler.Cir_fdatetime = CircularDetailJsonData.getString("Fromdate");
                                    loginObject_recycler.Cir_tdatetime = CircularDetailJsonData.getString("TODATE");
                                    loginObject_recycler.Cir_description = CircularDetailJsonData.getString("Description");
                                    CircularBData.add(loginObject_recycler);

                                    String Circular_title = CircularBData.get(i).Cir_title;
                                    String Circular_fdate = CircularBData.get(i).Cir_fdatetime;
                                    String Circular_tdate = CircularBData.get(i).Cir_tdatetime;
                                    String Circular_description = CircularBData.get(i).Cir_description;

                                    String CircularDetails = Circular_title+"&"+Circular_fdate+"&"+Circular_tdate+"&"+Circular_description;
                                    CircularList.add(CircularDetails);
                                    i++;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            recycler();
                            PD.dismiss();
                        }else{
                            PD.dismiss();
                            Toast.makeText(Circular.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
            CircularrecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cadapter = new CircularRecyclerViewAdapter(this, CircularList);
            CircularrecyclerView.setAdapter(cadapter);
        }
    }




