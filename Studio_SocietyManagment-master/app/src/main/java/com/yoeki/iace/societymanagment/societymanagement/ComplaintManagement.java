package com.yoeki.iace.societymanagment.societymanagement;

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


public class ComplaintManagement extends Fragment {
    ComplaintManagementRecyclerViewAdapter cadapter;
    private ArrayList<String> ComplaintList;
    private FloatingActionButton fab;
    ProgressDialog PD;
    String Complaintids;
    DBHandler db;
    int StatID=0;
    ArrayAdapter<String> compl_lst_Name;
    Spinner srch_com_by_type;
    AppCompatTextView complainttype;
    RecyclerView recyclerView;
    List<loginObject> loginBData;
    static List<String> complainttList;
    static ArrayList<String> ComplaintListArray;

    private boolean _isOpened;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        db = new DBHandler(getActivity());
        recyclerView = getView().findViewById(R.id.complaint);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        complainttype = (AppCompatTextView)getView().findViewById(R.id.complainttype);
        srch_com_by_type = (Spinner)getView().findViewById(R.id.srch_com_by_type);

//        srch_com_by_type.setBackgroundResource(R.drawable.popupbackground);
//        srch_com_by_type.setForegroundTintList( );


        ComplaintListArray = new ArrayList<>();
        PD = new ProgressDialog(getActivity());
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        try {
            complainttList = db.getComplaintList();
            for (final String list : complainttList) {
                String log = list;
                ComplaintListArray.add(log);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        compl_lst_Name =new ArrayAdapter<String>(getContext(),R.layout.spinner, ComplaintListArray);
        compl_lst_Name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srch_com_by_type.setAdapter(compl_lst_Name);
        compl_lst_Name.insert("--Search by Type--", 0);

        srch_com_by_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String client_Selection = adapterView.getItemAtPosition(position).toString();
                String check_client_nme = "--Select Request Type--";

                if(!client_Selection.equals("--Search by Type--")){

                    for (int i = 0; i < ComplaintListArray.size(); i++) {

                        String listName = String.valueOf(ComplaintListArray.get(i));

                        if (listName.equals(client_Selection)) {
                            try{
//                                complainttype.setText(listName);
                                Complaintids = String.valueOf(db.getComplaintListID(listName));
                                StatID=1;

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    datafrcomplaint();
                }else{
                    datafrcomplaint();
//                    complainttype.setText("--Search by Type--");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), Complaint_Created.class);
                startActivity(intent);
            }
        });
            datafrcomplaint();
//        ComplaintList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.complaintmanagement, container, false);
    }

    public void datafrcomplaint(){
        PD.show();
        String  json_url;
        HashMap<String, String> params;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String UID = prefs.getString("UserID"," ");

        if(StatID==1){
            json_url = (getString(R.string.BASE_URL) + "/FilterComplaint");
            params = new HashMap<String, String>();
            params.put("UserId",UID);
            params.put("ComplaintTypeId",Complaintids);
            StatID=0;
        }else{
            json_url = (getString(R.string.BASE_URL) + "/BindComplaints");
            params = new HashMap<String, String>();
            params.put("userId",UID);
        }

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;


                ComplaintList = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    String mess = loginData.getString("message");

                    if (resStatus.equalsIgnoreCase("Success")) {

//                      Request List
                        try {
                            BDetailArray = response.getJSONArray("list");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.com_title = BDetailJsonData.getString("title");
                                loginObject_recycler.com_creat_on = BDetailJsonData.getString("createdOn");
                                loginObject_recycler.com_creat_by = BDetailJsonData.getString("CreatedBy");
                                loginObject_recycler.com_unit = BDetailJsonData.getString("UnitNo");
                                loginObject_recycler.com_complaintNo = BDetailJsonData.getString("ComplainNo");
                                loginObject_recycler.com_desc = BDetailJsonData.getString("description");
                                loginObject_recycler.com_status = BDetailJsonData.getString("status");
                                loginObject_recycler.com_vndr_nme = BDetailJsonData.getString("assignedTo");
                                loginObject_recycler.com_type = BDetailJsonData.getString("type");
                                loginObject_recycler.com_UniqueCode = BDetailJsonData.getString("UniqueCode");
                                loginObject_recycler.com_ComplaintCode = BDetailJsonData.getString("compliantId");
                                loginBData.add(loginObject_recycler);

                                String Com_name = loginBData.get(i).com_title;
                                String Com_createdOn = loginBData.get(i).com_creat_on;
                                String Com_createdBy = loginBData.get(i).com_creat_by;
                                String Com_unit = loginBData.get(i).com_unit;
                                String Com_Complaint = loginBData.get(i).com_complaintNo;
                                String Com_desc = loginBData.get(i).com_desc;
                                String Com_status = loginBData.get(i).com_status;
                                String Com_vndr_nme = loginBData.get(i).com_vndr_nme;
                                String Com_type = loginBData.get(i).com_type;
                                String Com_Unique = loginBData.get(i).com_UniqueCode;
                                String Com_ComID = loginBData.get(i).com_ComplaintCode;

                                String RequestDetails = Com_name+"~"+Com_createdOn+"~"+Com_createdBy+"~"+Com_unit+"~"+Com_Complaint+"~"+Com_desc+"~"+Com_status
                                        +"~"+Com_vndr_nme+"~"+Com_type+"~"+Com_Unique+"~"+Com_ComID;
                                ComplaintList.add(RequestDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        PD.dismiss();
                        recyclerfrVisitor();
                    }else{
                        PD.dismiss();
                        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getContext(), "Something went Wrong "+error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }

    public void recyclerfrVisitor(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cadapter = new ComplaintManagementRecyclerViewAdapter(  getActivity(), ComplaintList);
        recyclerView.setAdapter(cadapter);
    }
}