package com.yoeki.iace.societymanagment.Visitors_Management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
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

public class VisitorsManagement extends AppCompatActivity implements View.OnClickListener {
    VisitorsRecyclerViewAdapter vadapter;
    private ArrayList<String> VisitorsList;
    private FloatingActionButton fab,openpopup,gotoactivity;
    private Boolean isFabOpen = false;
    private PopupWindow window;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    ProgressDialog PD;
    RecyclerView recyclerView;
    List<loginObject> loginBData;
    String UID,MEM_STATUS = "1",STATUS;
    SwitchCompat switchCompat;
    int i;
    Button Visit_bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitors_management);

        recyclerView = (RecyclerView) findViewById(R.id.request);
//        fab = (FloatingActionButton)findViewById(R.id.fab);
//        openpopup = (FloatingActionButton)findViewById(R.id.openpopup);
        Visit_bck = (Button)findViewById(R.id.Visit_bck);
        gotoactivity = (FloatingActionButton)findViewById(R.id.openactivity);
        switchCompat=(SwitchCompat)findViewById(R.id.HomeNtHome);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        PD = new ProgressDialog(VisitorsManagement.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);
        datafrVisitor();

        Visit_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        gotoactivity.setOnClickListener((View.OnClickListener) this);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String stat = String.valueOf(isChecked);
                if(i==0){
                    forMemStatusChanged();
                }else{
                    Intent intent0=new Intent(VisitorsManagement.this,visitor_notHome.class);
                    startActivity(intent0);
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
//            case R.id.fab:
//                animateFAB();
//                break;
//            case R.id.openpopup:
//              Intent intent0=new Intent(VisitorsManagement.this,visitor_notHome.class);
//              startActivity(intent0);
//              break;
            case R.id.openactivity:
                Intent intent=new Intent(VisitorsManagement.this,VisitorsNewRequest.class);
                startActivity(intent);
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            openpopup.startAnimation(fab_close);
            gotoactivity.startAnimation(fab_close);
            openpopup.setClickable(false);
            gotoactivity.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            openpopup.startAnimation(fab_open);
            gotoactivity.startAnimation(fab_open);
            openpopup.setClickable(true);
            gotoactivity.setClickable(true);
            isFabOpen = true;
        }
    }

    public void datafrVisitor(){
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/BindVisitors");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;

                VisitorsList = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    String MemStatus = loginData.getString("MemberStatus");
                    if(MemStatus.equals("N")){
                        switchCompat.setChecked(false);
                        i = Integer.parseInt("0");
                    }else{
                        switchCompat.setChecked(true);
                        i = Integer.parseInt("1");
                    }

                    if (resStatus.equalsIgnoreCase("Success")) {

//                      Visitor List
                        try {
                            BDetailArray = response.getJSONArray("list");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Visitor_name = BDetailJsonData.getString("Name");
                                loginObject_recycler.Visitor_Address = BDetailJsonData.getString("Address");
                                loginObject_recycler.Visitor_Fromdte = BDetailJsonData.getString("FromDate");
                                loginObject_recycler.Visitor_Todte = BDetailJsonData.getString("ToDate");
                                loginObject_recycler.Visitor_Status = BDetailJsonData.getString("Status");
                                loginObject_recycler.Visitor_Contact = BDetailJsonData.getString("ContactNo");
                                loginObject_recycler.Home_stat = BDetailJsonData.getString("NotHome");
                                loginBData.add(loginObject_recycler);

                                String visitor_name = loginBData.get(i).Visitor_name;
                                String visitor_address = loginBData.get(i).Visitor_Address;
                                String visitor_fromdte = loginBData.get(i).Visitor_Fromdte;
                                String visitor_todte = loginBData.get(i).Visitor_Todte;
                                String visitor_status = loginBData.get(i).Visitor_Status;
                                String visitor_contact = loginBData.get(i).Visitor_Contact;
                                String stat_Home = loginBData.get(i).Home_stat;

                                String VisitorDetails = visitor_name+"~"+visitor_address+"~"+visitor_fromdte+"~"+visitor_todte+"~"+visitor_status+"~"+visitor_contact+"~"+stat_Home;
                                VisitorsList.add(VisitorDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        PD.dismiss();
                        recyclerfrVisitor();
                    }else{
                        PD.dismiss();
                        Toast.makeText(VisitorsManagement.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(VisitorsManagement.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }

    public void recyclerfrVisitor(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vadapter = new VisitorsRecyclerViewAdapter(this, VisitorsList);
        recyclerView.setAdapter(vadapter);
    }

    public void forMemStatusChanged(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/ChangeVisitorStatus");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");
        String valueofStatus;
        if(i==0){
            valueofStatus="H";
        }else{
            valueofStatus="N";
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);
        params.put("FromDate", "");
        params.put("ToDate","");
        params.put("EmergencyContactNo","");
        params.put("MemberStatus",valueofStatus);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if(resStatus.equals("Success")){
                        PD.dismiss();
                        switchCompat.setChecked(true);
                        i=1;

//                        Toast.makeText(VisitorsManagement.this, "Status Changed :- Home", Toast.LENGTH_SHORT).show();
                    }else{
                        PD.dismiss();
                        i=0;
                        switchCompat.setChecked(false);
                        Toast.makeText(VisitorsManagement.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                switchCompat.setChecked(false);
                i=0;
                Toast.makeText(VisitorsManagement.this, "Something went Wrong. Please try after SomeTime", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }

}