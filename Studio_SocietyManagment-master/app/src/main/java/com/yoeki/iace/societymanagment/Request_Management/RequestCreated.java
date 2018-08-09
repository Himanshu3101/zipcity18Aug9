package com.yoeki.iace.societymanagment.Request_Management;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestCreated extends AppCompatActivity {
    static String Flatids = null;
    static String Requestids = null;
    Boolean validation;
    static List<String> FlatList,requestList;
    ProgressDialog PD;
    static ArrayList<String> FlatListArray,SocietyReqArray;
    static DBHandler db;
    AppCompatImageButton bck;
    AppCompatButton submit_request_repo;
    AppCompatEditText ReqRes_description, ReqRes_title;
    static AppCompatTextView ReqRes_location,requesttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_created);
        db = new DBHandler(this);
        submit_request_repo = (AppCompatButton)findViewById(R.id.submit_request_repo);
        ReqRes_description = (AppCompatEditText)findViewById(R.id.ReqRes_description);
        ReqRes_title = (AppCompatEditText)findViewById(R.id.ReqRes_complaint);
        ReqRes_location = (AppCompatTextView)findViewById(R.id.ReqRes_location);
        requesttype = (AppCompatTextView)findViewById(R.id.requesttype);
        bck = (AppCompatImageButton)findViewById(R.id.back_reqRes) ;

        PD = new ProgressDialog(RequestCreated.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        View ReqResRequest_type =findViewById(R.id.ReqResRequest_type);
        ReqResRequest_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment request_frag = new Request();
                request_frag.show(getSupportFragmentManager(),request_frag.getTag());
            }
        });

        View ReqResFlat =findViewById(R.id.ReqResFlat);
        ReqResFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BottomSheetDialogFragment flat_frag = new FlatLocation();
                    flat_frag.show(getSupportFragmentManager(), flat_frag.getTag());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        submit_request_repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    try {
                        forReqsubmit();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Request_management.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Request_management.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public boolean validations() {
        validation = true;
        if (ReqRes_description.getText().toString().equals("") && ReqRes_title.getText().toString().equals("") &&
                ReqRes_location.getText().toString().equals("") && requesttype.getText().toString().equals("")) {
            validation = false;
            ReqRes_description.setError("Enter Description");
            ReqRes_title.setError("Enter Complaint Title");
            ReqRes_location.setError("Enter Flat Location");
            requesttype.setError("Select Request Type");
        } else if (ReqRes_description.getText().toString().equals("")) {
            validation = false;
            ReqRes_description.setError("Enter Description");
        } else if (ReqRes_title.getText().toString().equals("")) {
            validation = false;
            ReqRes_title.setError("Enter Complaint Title");
        }else if (ReqRes_location.getText().toString().equals("")) {
            validation = false;
            ReqRes_location.setError("Enter Flat Location");
        }else if (requesttype.getText().toString().equals("")) {
            validation = false;
            requesttype.setError("Select Request Type");
        }
        return validation;
    }

    @SuppressLint("ValidFragment")
    public static class FlatLocation extends BottomSheetDialogFragment {
        ProgressDialog PD;
        ListView societyFLatList;
        TextView header;
        String[] SepFlatList;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FlatListArray = new ArrayList<>();
            PD = new ProgressDialog(getContext());
            PD.setMessage("Loading...");
            PD.setCancelable(false);
        }

        private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };

        @SuppressLint("RestrictedApi")
        @Override
        public void setupDialog(Dialog dialog, int style) {
            super.setupDialog(dialog, style);
            View contentView = View.inflate(getContext(), R.layout.single_bottomsheet, null);
            dialog.setContentView(contentView);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior != null && behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
            }
            societyFLatList = (ListView) contentView.findViewById(R.id.service_data);
            header = (TextView) contentView.findViewById(R.id.HeaderBottom);
            header.setText("Flat Location");
            forService();

        }

        public void forService() {
            PD.show();
            FlatList = db.getFlatList();
            for (final String link : FlatList) {
                String log =  link;
                FlatListArray.add(log);
            }
            PD.dismiss();
            societyFLatList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,FlatListArray));

            societyFLatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String VerName = FlatListArray.get(position).toString();
                    try{
                    Flatids = String.valueOf(db.getFlatListID(VerName));}catch(Exception e){e.printStackTrace();}
                    ReqRes_location.setText(VerName);
                    dismiss();
                }
            });
        }
    }

    @SuppressLint("ValidFragment")
    public static class Request extends BottomSheetDialogFragment {
        ProgressDialog PD;
        ListView societyRequestList;
        TextView header;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SocietyReqArray = new ArrayList<>();
            PD = new ProgressDialog(getContext());
            PD.setMessage("Loading...");
            PD.setCancelable(false);
        }

        private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };

        @SuppressLint("RestrictedApi")
        @Override
        public void setupDialog(Dialog dialog, int style) {
            super.setupDialog(dialog, style);
            View contentView = View.inflate(getContext(), R.layout.single_bottomsheet, null);
            dialog.setContentView(contentView);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior != null && behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
            }
            societyRequestList = (ListView) contentView.findViewById(R.id.service_data);
            header = (TextView) contentView.findViewById(R.id.HeaderBottom);
            header.setText("Request Type");
            try {
                forService();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public void forService() {
            PD.show();
            requestList = db.getReqList();
            for (final String list : requestList) {
                String log =  list;
                SocietyReqArray.add(log);
            }
            PD.dismiss();
            societyRequestList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,SocietyReqArray));

            societyRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String VerName = SocietyReqArray.get(position).toString();
                    try{
                        Requestids = String.valueOf(db.getReqListID(VerName));}catch(Exception e){}
                    requesttype.setText(VerName);
                    dismiss();
                }
            });
        }
    }

    public  void forReqsubmit(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/InsertRequest");
        final String Title = ReqRes_title.getText().toString();
        final String Description = ReqRes_description.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("RequestType",Requestids);
        params.put("UnitMasterDetailId",Flatids);
        params.put("Title",Title);
        params.put("Description",Description);
        params.put("CreatedBy",UID);
        params.put("VendorId","0");

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        ReqRes_location.setText("");
                        requesttype.setText("");
                        ReqRes_title.setText("");
                        ReqRes_description.setText("");
                        Toast.makeText(RequestCreated.this, "Request Created Successfully...", Toast.LENGTH_SHORT).show();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(RequestCreated.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(RequestCreated.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}
