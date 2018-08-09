package com.yoeki.iace.societymanagment.Complaint_management;

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

public class ComplaintCreated extends AppCompatActivity {
    AppCompatButton C_submit;
    AppCompatEditText C_description,C_complaintTitle;
    static AppCompatTextView C_location,C_requesttype;
    AppCompatImageButton C_back;
    ProgressDialog PD;
    static DBHandler db;
    Boolean validation;
    static String C_Flatids = null,C_Reqids = null;
    static List<String> C_FlatList,C_requestList;
    static ArrayList<String> C_FlatListArray,C_SocietyReqArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_created_old);
        db = new DBHandler(this);
        C_description  = (AppCompatEditText)findViewById(R.id.C_description);
        C_complaintTitle = (AppCompatEditText)findViewById(R.id.C_complaintTitle);
        C_location = (AppCompatTextView)findViewById(R.id.C_location);
        C_requesttype = (AppCompatTextView)findViewById(R.id.C_requesttype);

        C_submit = (AppCompatButton)findViewById(R.id.C_submit);
        C_back = (AppCompatImageButton)findViewById(R.id.C_back) ;

        PD = new ProgressDialog(ComplaintCreated.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        View C_request_type =findViewById(R.id.C_request_type);
        C_request_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment Com_request_frag = new Com_Request();
                Com_request_frag.show(getSupportFragmentManager(),Com_request_frag.getTag());
            }
        });

        View C_Flat =findViewById(R.id.C_Flat);
        C_Flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment Com_flat_frag = new Com_FlatLocation();
                Com_flat_frag.show(getSupportFragmentManager(), Com_flat_frag.getTag());
            }
        });

        C_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    try {
                        forComsubmit();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        C_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Complaint_management.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Complaint_management.class);
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
        if (C_requesttype.getText().toString().equals("") && C_location.getText().toString().equals("") &&
                C_complaintTitle.getText().toString().equals("") && C_description.getText().toString().equals("")) {
            validation = false;
            C_requesttype.setError("Select Request");
            C_location.setError("Select Location");
            C_complaintTitle.setError("Enter Title of Complaint");
            C_description.setError("Some Description");
        } else if (C_requesttype.getText().toString().equals("")) {
            validation = false;
            C_requesttype.setError("Select Request");
        } else if (C_location.getText().toString().equals("")) {
            validation = false;
            C_location.setError("Select Location");
        }else if (C_complaintTitle.getText().toString().equals("")) {
            validation = false;
            C_complaintTitle.setError("Enter Title of Complaint");
        }else if (C_description.getText().toString().equals("")) {
            validation = false;
            C_description.setError("Some Description");
        }
        return validation;
    }

    @SuppressLint("ValidFragment")
    public static class Com_FlatLocation extends BottomSheetDialogFragment {
        ProgressDialog PD;
        ListView C_societyFLatList;
        TextView header;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            C_FlatListArray = new ArrayList<>();
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
            C_societyFLatList = (ListView) contentView.findViewById(R.id.service_data);
            header = (TextView) contentView.findViewById(R.id.HeaderBottom);
            header.setText("Flat Location");
            forService();

        }

        public void forService() {
            PD.show();
            C_FlatList = db.getFlatList();
            for (final String link : C_FlatList) {
                String log =  link;
                C_FlatListArray.add(log);
            }
            PD.dismiss();
            C_societyFLatList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,C_FlatListArray));

            C_societyFLatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String VerName = C_FlatListArray.get(position).toString();
                    try{
                        C_Flatids = String.valueOf(db.getFlatListID(VerName));}catch(Exception e){e.printStackTrace();}
                    C_location.setText(VerName);
                        dismiss();
                }
            });
        }
    }

    @SuppressLint("ValidFragment")
    public static class Com_Request extends BottomSheetDialogFragment {
        ProgressDialog PD;
        ListView C_RequestList;
        TextView header;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            C_SocietyReqArray = new ArrayList<>();
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
            C_RequestList = (ListView) contentView.findViewById(R.id.service_data);
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
            C_requestList = db.getReqList();
            for (final String list : C_requestList) {
                String log =  list;
                C_SocietyReqArray.add(log);
            }
            PD.dismiss();
            C_RequestList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,C_SocietyReqArray));

            C_RequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String VerName = C_SocietyReqArray.get(position).toString();
                    try{
                        C_Reqids = String.valueOf(db.getReqListID(VerName));}catch(Exception e){}
                    C_requesttype.setText(VerName);
                    dismiss();
                }
            });
        }
    }

    public  void forComsubmit(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/InsertComplaints");
        final String Title = C_complaintTitle.getText().toString();
        final String Description = C_description.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("CreatedBy",UID);
        params.put("ComplainType",C_Reqids);
        params.put("Title",Title);
        params.put("Description",Description);
        params.put("UnitMasterDetailId",C_Flatids);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        C_description.setText("");
                        C_complaintTitle.setText("");
                        C_location.setText("");
                        C_requesttype.setText("");
                        Toast.makeText(ComplaintCreated.this, "Complaint Created Successfully...", Toast.LENGTH_SHORT).show();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(ComplaintCreated.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(ComplaintCreated.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}
