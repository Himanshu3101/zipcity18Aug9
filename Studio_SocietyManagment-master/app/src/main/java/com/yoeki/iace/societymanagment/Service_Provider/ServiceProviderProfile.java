package com.yoeki.iace.societymanagment.Service_Provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.ChangePassword;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceProviderProfile extends AppCompatActivity {

    SrvcPrvdeProfileRecyclerViewAdapter padapter;
    AppCompatTextView profilename,profilecontactno;

    private ArrayList<String> SrvcProfileList;
     AppCompatImageButton srvcprvd_changepassword,Ser_Pro_bck;
     List<loginObject> SrvcPrvdProfileBData;
      RecyclerView SrvcPrvdProfilerecyclerView;
      ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_profile);
        srvcprvd_changepassword = (AppCompatImageButton) findViewById(R.id.srvcprvd_changepassword);
        profilename=(AppCompatTextView)findViewById(R.id.serviceprovider_name);
        profilecontactno=(AppCompatTextView)findViewById(R.id.profilecontactno);
        Ser_Pro_bck=(AppCompatImageButton)findViewById(R.id.Ser_Pro_bck);

        SrvcPrvdProfilerecyclerView = findViewById(R.id.profile_servicetype);

        PD = new ProgressDialog(ServiceProviderProfile.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);


        srvcprvd_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProviderProfile.this, ChangePassword.class);
                intent.putExtra("frompassword","ServceProfile");
                startActivity(intent);
            }
        });

        Ser_Pro_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ServiceProvider.class);
                startActivity(intent);
                finish();
            }
        });

        BindServiceProviderProfileData();
    }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(),ServiceProvider.class);
            startActivity(intent);
            finish();
        }

//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Services");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            String[] serviceData =  serviceName.getText().toString().split(",");
//            for (int i = 0; i < serviceData.length;i++) {
//                try{
//                    ServiceIds = String.valueOf(db.getServiceListID(serviceData[i].toString()));
//
//                }catch(Exception e){e.printStackTrace();}
//                serviceIDS.add(ServiceIds);
//            }
//        }
//    });

        public void BindServiceProviderProfileData() {
            PD.show();
            SrvcProfileList = new ArrayList<>();
            String json_url = (getString(R.string.BASE_URL) + "/BindUserProfile");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String UID = prefs.getString("UserID", " ");
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("UserId",UID);


            JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                    params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray ProfileDetailArray = null;
                    String mssg;
                    SrvcProfileList = new ArrayList<>();
                    try {
                        JSONObject loginData = new JSONObject(String.valueOf(response));
                        String resStatus = loginData.getString("status");
                        mssg = loginData.getString("message");
                        if (resStatus.equalsIgnoreCase("Success")) {
                            try {
//                           Profile Details
                                ProfileDetailArray = response.getJSONArray("listOwner");
                                SrvcPrvdProfileBData = new ArrayList<>();
                                for (int i = 0; i < ProfileDetailArray.length();) {
                                    JSONObject ProfileDetailJsonData = ProfileDetailArray.getJSONObject(i);
                                    loginObject loginObject_recycler = new loginObject();

                                    loginObject_recycler.Profile_Name = ProfileDetailJsonData.getString("UserName");
                                    loginObject_recycler.Profile_Contact = ProfileDetailJsonData.getString("MobileNo");
                                    loginObject_recycler.Profile_ServiceType = ProfileDetailJsonData.getString("UserRoleId");
                                    SrvcPrvdProfileBData.add(loginObject_recycler);

                                    String P_Name = SrvcPrvdProfileBData.get(i).Profile_Name;
                                    String P_Contact = SrvcPrvdProfileBData.get(i).Profile_Contact;
                                    String P_servicetype = SrvcPrvdProfileBData.get(i).Profile_ServiceType;

                                    profilename.setText(P_Name);
                                    profilecontactno.setText(P_Contact);

                                    SrvcProfileList.add(P_servicetype);
                                    i++;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            recycler();
                            PD.dismiss();
                        }else{
                            PD.dismiss();
                            Toast.makeText(ServiceProviderProfile.this, mssg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PD.dismiss();
//                    Toast.makeText(ServiceProviderProfile.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
                    Log.w("error in response", "Error: " + error.getMessage());
                }
            });

            req.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            MyApplication.getInstance().addToReqQueue(req);
        }
        public void recycler(){
            SrvcPrvdProfilerecyclerView.setLayoutManager(new LinearLayoutManager(this));
            padapter = new SrvcPrvdeProfileRecyclerViewAdapter(this, SrvcProfileList);
            SrvcPrvdProfilerecyclerView.setAdapter(padapter
            );
        }
    }


