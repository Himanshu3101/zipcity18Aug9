package com.yoeki.iace.societymanagment.VendorList;

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
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import com.yoeki.iace.societymanagment.Adapter.VendorListAdapter.VendorListRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorList extends AppCompatActivity {
    VendorListRecyclerViewAdapter vadapter;
    RecyclerView vendorlistRecycler;
    String UID;
    List<loginObject> VendorListBData;
    ProgressDialog PD;
   private ArrayList<String> VendorList;
    DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_list);

        db = new DBHandler(this);
        vendorlistRecycler = findViewById(R.id.vendorList);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID", " ");

        PD = new ProgressDialog(VendorList.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forVendorListData();

    }

        public  void forVendorListData(){
            PD.show();
            String json_url=(getString(R.string.BASE_URL) + "/BindVendorVId");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("UserId",UID);

            JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                    params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray VendorListArray = null;

                    VendorList = new ArrayList<>();
                    try {
                        JSONObject loginData = new JSONObject(String.valueOf(response));
                        String resStatus = loginData.getString("status");
                        if (resStatus.equalsIgnoreCase("Success")) {

                            try {
//                           Vendor List Details
                                VendorListArray = response.getJSONArray("list");
                                VendorListBData = new ArrayList<>();
                                for (int i = 0; i < VendorListArray.length();) {
                                    JSONObject VendorListJsonData = VendorListArray.getJSONObject(i);
                                    loginObject loginObject_recycler = new loginObject();
                                    loginObject_recycler.VendorList_Name = VendorListJsonData.getString("Name");
                                    loginObject_recycler.VendorList_Contact = VendorListJsonData.getString("ContactNo");
                                    loginObject_recycler.VendorList_Verification_type = VendorListJsonData.getString("DocumentName");
                                    loginObject_recycler.VendorList_Verification_no = VendorListJsonData.getString("VerificationId");
                                    loginObject_recycler.VendorList_Verification_by = VendorListJsonData.getString("VerifiedBy");
                                    loginObject_recycler.VendorList_Verification_on = VendorListJsonData.getString("VerifiedOn");
//                                    loginObject_recycler.VendorList_FromDate = VendorListJsonData.getString("type");
//                                    loginObject_recycler.VendorList_ToDate = VendorListJsonData.getString("requestId");
                                    loginObject_recycler.VendorList_Servicetyoe = VendorListJsonData.getString("ServiceType");
//                                    loginObject_recycler.VendorList_type = VendorListJsonData.getString("requestId");
//                                    loginObject_recycler.VendorList_typevalue = VendorListJsonData.getString("UniqueCode");
                                    VendorListBData.add(loginObject_recycler);

                                    String Vendor_name = VendorListBData.get(i).VendorList_Name;
                                    String Vendor_contact = VendorListBData.get(i).VendorList_Contact;
                                    String Vendor_verifi_type = VendorListBData.get(i).VendorList_Verification_type;
                                    String Vendor_verifi_no = VendorListBData.get(i).VendorList_Verification_no;
                                    String Vendor_verifi_by = VendorListBData.get(i).VendorList_Verification_by;
                                    String Vendor_verifi_on = VendorListBData.get(i).VendorList_Verification_on;
//                                    String Vendor_fromdate = VendorListBData.get(i).VendorList_FromDate;
//                                    String Vendor_todate = VendorListBData.get(i).VendorList_ToDate;
                                    String Vendor_servicetype = VendorListBData.get(i).VendorList_Servicetyoe;
//                                    String Vendor_type = VendorListBData.get(i).VendorList_type;
//                                    String Vendor_typevalue = VendorListBData.get(i).VendorList_typevalue;

                                    String FullName = Vendor_name+","+Vendor_contact+","+Vendor_verifi_type+","+Vendor_verifi_no+","+Vendor_verifi_by+","+Vendor_verifi_on+/*","+Vendor_fromdate+","+Vendor_todate+*/","+Vendor_servicetype/*+","+Vendor_type+","+Vendor_typevalue*/;
                                    VendorList.add(FullName);
                                    i++;
                                }
                                PD.dismiss();
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            PD.dismiss();
                            Toast.makeText(VendorList.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
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
            vendorlistRecycler.setLayoutManager(new LinearLayoutManager(this));
            vadapter = new VendorListRecyclerViewAdapter(this, VendorList);
            vendorlistRecycler.setAdapter(vadapter);
        }


    }


