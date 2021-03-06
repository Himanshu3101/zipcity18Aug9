package com.yoeki.iace.societymanagment.RequestNew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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


public class RequestManagement extends Fragment {

    RequestManagementRecyclerViewAdapter radapter;
    private ArrayList<String> RequestList;
    RecyclerView requestRecycler;
    FloatingActionButton fab;
    ProgressDialog PD;
    DBHandler db;
    List<loginObject> loginBData;
    Spinner srch_req_by_type;
    static List<String> requestList;
    static ArrayList<String> RequestListArray;
    ArrayAdapter<String> reqt_lst_Name;
    AppCompatTextView requesttype;
    String Requestids;
    int S = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        db = new DBHandler(getActivity());
        requestRecycler = getView().findViewById(R.id.request);
        fab = (FloatingActionButton) getView().findViewById(R.id.Rfab);
        requesttype = (AppCompatTextView)getView().findViewById(R.id.requesttype);
        srch_req_by_type = (Spinner)getView().findViewById(R.id.srch_req_by_type);


        RequestListArray = new ArrayList<>();
        PD = new ProgressDialog(getActivity());
        PD.setMessage("Loading...");
        PD.setCancelable(false);


        try {
            requestList = db.getReqList();
            for (final String list : requestList) {
                String log = list;
                RequestListArray.add(log);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        reqt_lst_Name = new ArrayAdapter<String>(getContext(), R.layout.spinner, RequestListArray);
        reqt_lst_Name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srch_req_by_type.setAdapter(reqt_lst_Name);
        reqt_lst_Name.insert("--Search by Type--", 0);

        srch_req_by_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String client_Selection = adapterView.getItemAtPosition(position).toString();
                String check_client_nme = "--Select Request Type--";

                if(!client_Selection.equals("--Search by Type--")){

                    for (int i = 0; i < RequestListArray.size(); i++) {

                        String listName = String.valueOf(RequestListArray.get(i));

                        if (listName.equals(client_Selection)) {
                            try{
//                                requesttype.setText(listName);
                                Requestids = String.valueOf(db.getReqListID(listName));
                                S=1;

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    datafrrequest();
                }else{
//                    requesttype.setText("--Search by Type--");
                    datafrrequest();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), RequestManagement_Created.class);
                startActivity(intent);
            }
        });
        datafrrequest();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.requestmanagement, container, false);
    }

    public void datafrrequest(){
        PD.show();
        String  json_url;
        HashMap<String, String> params;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String UID = prefs.getString("UserID"," ");

        if(S==1){
            json_url = (getString(R.string.BASE_URL) + "/FilterUnClosedRequset");
            params = new HashMap<String, String>();
            params.put("UserId",UID);
            params.put("ComplaintTypeId",Requestids);
            S=0;
        }else{
            json_url = (getString(R.string.BASE_URL) + "/BindUnClosedRequests");
            params = new HashMap<String, String>();
            params.put("userId",UID);
        }

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;


                RequestList = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");

                    if (resStatus.equalsIgnoreCase("Success")) {

//                      Request List
                        try {
                            BDetailArray = response.getJSONArray("list");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.req_title = BDetailJsonData.getString("title");
                                loginObject_recycler.req_type = BDetailJsonData.getString("type");
                                loginObject_recycler.req_vndr_nme = BDetailJsonData.getString("VendorName");
                                loginObject_recycler.req_unit = BDetailJsonData.getString("UnitNo");
                                loginObject_recycler.req_creat_on = BDetailJsonData.getString("createdOn");
                                loginObject_recycler.req_creat_by = BDetailJsonData.getString("CreatedBy");
                                loginObject_recycler.req_req_nu = BDetailJsonData.getString("RequestNo");
                                loginObject_recycler.req_status = BDetailJsonData.getString("status");
                                loginObject_recycler.req_fromdte = BDetailJsonData.getString("FromDate");
                                loginObject_recycler.req_todte = BDetailJsonData.getString("ToDate");
                                loginObject_recycler.req_desc = BDetailJsonData.getString("description");
                                loginObject_recycler.req_vendr_mobile = BDetailJsonData.getString("VendorContactNo");
                                loginObject_recycler.req_uniq_code = BDetailJsonData.getString("UniqueCode");
                                loginObject_recycler.req_Id = BDetailJsonData.getString("requestId");
                                loginBData.add(loginObject_recycler);

                                String Req_name = loginBData.get(i).req_title;
                                String Req_type = loginBData.get(i).req_type;
                                String Req_vndrNme = loginBData.get(i).req_vndr_nme;
                                String Req_unit = loginBData.get(i).req_unit;
                                String Req_createdOn = loginBData.get(i).req_creat_on;
                                String Req_createdBy = loginBData.get(i).req_creat_by;
                                String Req_requestNo = loginBData.get(i).req_req_nu;
                                String Req_status = loginBData.get(i).req_status;
                                String Req_fromdte = loginBData.get(i).req_fromdte;
                                String Req_todte = loginBData.get(i).req_todte;
                                String Req_desc = loginBData.get(i).req_desc;
                                String Req_vendr_mobileNo = loginBData.get(i).req_vendr_mobile;
                                String Req_uniq = loginBData.get(i).req_uniq_code;
                                String Req_ID = loginBData.get(i).req_Id;

                                String RequestDetails = Req_name+"~"+Req_type+"~"+Req_vndrNme+"~"+Req_unit+"~"+Req_createdOn+"~"+Req_createdBy+"~"+Req_requestNo
                                        +"~"+Req_status+"~"+Req_fromdte+"~"+Req_todte+"~"+Req_desc+"~"+Req_vendr_mobileNo+"~"+Req_uniq+"~"+Req_ID;

                                RequestList.add(RequestDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        PD.dismiss();
                        recyclerfrVisitor();
                    }else{
                        PD.dismiss();
                        Toast.makeText(getContext(), "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }

    public void recyclerfrVisitor(){

        requestRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        radapter = new RequestManagementRecyclerViewAdapter(  getActivity(), RequestList);
        requestRecycler.setAdapter(radapter);
    }
}