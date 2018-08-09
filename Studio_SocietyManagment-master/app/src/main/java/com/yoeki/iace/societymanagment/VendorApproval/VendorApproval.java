package com.yoeki.iace.societymanagment.VendorApproval;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Adapter.VendorApprovalAdapter.VendorApprovalRecyclerViewAdapter;
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

public class VendorApproval extends AppCompatActivity {
    VendorApprovalRecyclerViewAdapter vadapter;
    private ArrayList<String> VendorApprovalList;
    RecyclerView vendorApprovalRecycler;
    String UID;
    List<loginObject> VendorApprovalBData;
    ProgressDialog PD;
    DBHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_approval);

        db = new DBHandler(this);
        vendorApprovalRecycler = findViewById(R.id.vendorApproval);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID", " ");

        PD = new ProgressDialog(VendorApproval.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forVendorApprovalData();

    }

    public  void forVendorApprovalData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindVendor");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray VendorApprovalArray = null;

                VendorApprovalList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        try {
//                           Vendor List Details
                            VendorApprovalArray = response.getJSONArray("list");
                            VendorApprovalBData = new ArrayList<>();
                            for (int i = 0; i < VendorApprovalArray.length();) {
                                JSONObject VendorApprovalJsonData = VendorApprovalArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();
                                loginObject_recycler.VendorApproval_Name = VendorApprovalJsonData.getString("Name");
                                loginObject_recycler.VendorApproval_Contact = VendorApprovalJsonData.getString("contactNo");
                                loginObject_recycler.VendorApproval_Verification_type = VendorApprovalJsonData.getString("DocumentName");
                                loginObject_recycler.VendorApproval_Verification_no = VendorApprovalJsonData.getString("VerificationId");
                                loginObject_recycler.VendorApproval_FromDate = VendorApprovalJsonData.getString("FromDate");
                                loginObject_recycler.VendorApproval_ToDate = VendorApprovalJsonData.getString("ToDate");
                                loginObject_recycler.VendorApproval_Servicetype = VendorApprovalJsonData.getString("Service");
                                loginObject_recycler.VendorApproval_type = VendorApprovalJsonData.getString("Type");
                                loginObject_recycler.VendorApproval_typevalue = VendorApprovalJsonData.getString("TypeValue");
//
                                VendorApprovalBData.add(loginObject_recycler);

                                String VendorApprov_name = VendorApprovalBData.get(i).VendorApproval_Name;
                                String VendorApprov_contact = VendorApprovalBData.get(i).VendorApproval_Contact;
                                String VendorApprov_verifi_type = VendorApprovalBData.get(i).VendorApproval_Verification_type;
                                String VendorApprov_verifi_no = VendorApprovalBData.get(i).VendorApproval_Verification_no;
                                String VendorApprov_fromdate = VendorApprovalBData.get(i).VendorApproval_FromDate;
                                String VendorApprov_todate = VendorApprovalBData.get(i).VendorApproval_ToDate;
                                String VendorApprov_servicetype = VendorApprovalBData.get(i).VendorApproval_Servicetype;
                                String VendorApprov_type = VendorApprovalBData.get(i).VendorApproval_type;
                                String VendorApprov_typevalue = VendorApprovalBData.get(i).VendorApproval_typevalue;

                                String FullName = VendorApprov_name+"&"+VendorApprov_contact+"&"+VendorApprov_verifi_type+"&"+VendorApprov_verifi_no+"&"+VendorApprov_fromdate+"&"+VendorApprov_todate+"&"+VendorApprov_servicetype+"&"+VendorApprov_type+"&"+VendorApprov_typevalue;
                                VendorApprovalList.add(FullName);
                                i++;
                            }
                            PD.dismiss();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        PD.dismiss();
                        Toast.makeText(VendorApproval.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                    Recycler();
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

    public void Recycler(){
        vendorApprovalRecycler.setLayoutManager(new LinearLayoutManager(this));
        vadapter = new VendorApprovalRecyclerViewAdapter(this, VendorApprovalList);
        vendorApprovalRecycler.setAdapter(vadapter);
    }


}