package com.yoeki.iace.societymanagment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.yoeki.iace.societymanagment.R.layout.single_bottomsheet;

/**
 * Created by IACE on 10-Jul-18.
 */

public class vendor_Reg extends AppCompatActivity {
    AppCompatEditText Vndr_nme, Vndr_cntct, Vndr_vrfctn_name;
    static AppCompatTextView VerIDName,societyeName,serviceName;
    Button back;
    AppCompatButton submit;
    Boolean validation;
    DBHandler dbHandler;
    ProgressDialog PD;
    ArrayList<String> listServices,serviceIDS,listVerifiID;
    List<loginObject> ServiceData,verifiData;
    static DBHandler db;
    static String ServiceIds = null,VerificationIDS = null,SocietyIDS=null;
    protected String[] services;

    protected ArrayList<CharSequence> selectedServices = new ArrayList<CharSequence>();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_regestration);
        db = new DBHandler(this);
        Vndr_nme = (AppCompatEditText)findViewById(R.id.vndr_nme);
        Vndr_cntct = (AppCompatEditText)findViewById(R.id.vndr_contact);
        Vndr_vrfctn_name = (AppCompatEditText)findViewById(R.id.vndr_verificatioln);
        back = (Button) findViewById(R.id.back);
        submit = (AppCompatButton) findViewById(R.id.Vndr_submt_btn);

        serviceName = (AppCompatTextView)findViewById(R.id.vndr_serviceType);
        societyeName = (AppCompatTextView)findViewById(R.id.vndr_society);
        VerIDName = (AppCompatTextView)findViewById(R.id.vndr_verificationID);

        PD = new ProgressDialog(vendor_Reg.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forService();

        View service =findViewById(R.id.service_type);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Servicepopup.class);
//                startActivity(intent);
                servicepopup();
            }
        });

        View society =findViewById(R.id.btnfrSociety);
        society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BottomSheetDialogFragment bottomSheetDialogFragment = new societyService();
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        View verfctnID =findViewById(R.id.btnverification);
        verfctnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BottomSheetDialogFragment verificationIdFragment = new verificaionID();
                    verificationIdFragment.show(getSupportFragmentManager(), verificationIdFragment.getTag());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    try {
                        forsubmit();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public String getEditTextString(AppCompatEditText et){
        return et.getText().toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void servicepopup(){
        boolean[] checkedServices = new boolean[services.length];
        serviceIDS = new ArrayList<>();
        int count = services.length;
        for(int i = 0; i < count; i++)
            checkedServices[i] = selectedServices.contains(services[i]);
        DialogInterface.OnMultiChoiceClickListener serviceDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                    selectedServices.add(services[which]);
                else
                    selectedServices.remove(services[which]);
                onChangeSelectedServices();
            }

        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Services");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] serviceData =  serviceName.getText().toString().split(",");
                for (int i = 0; i < serviceData.length;i++) {
                    try{
                        ServiceIds = String.valueOf(db.getServiceListID(serviceData[i].toString()));

                    }catch(Exception e){e.printStackTrace();}
                    serviceIDS.add(ServiceIds);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });

        builder.setMultiChoiceItems(services, checkedServices, serviceDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void onChangeSelectedServices() {
        StringBuilder stringBuilder = new StringBuilder();
        for(CharSequence services : selectedServices)
            stringBuilder.append(services + ",");
        serviceName.setText(stringBuilder.toString());
    }

    public boolean validations() {
        validation = true;
        if (Vndr_nme.getText().toString().equals("") && Vndr_cntct.getText().toString().equals("") &&
                Vndr_vrfctn_name.getText().toString().equals("") /*&& serviceName.getText().toString().equals("")*/
                && societyeName.getText().toString().equals("") && VerIDName.getText().toString().equals("")) {
            validation = false;
            Vndr_nme.setError("Enter Name");
            Vndr_cntct.setError("Enter Contact No.");
            Vndr_vrfctn_name.setError("Enter Verification No.");
           /* serviceName.setError("Select Service");*/
            societyeName.setError("Select Society Name");
            VerIDName.setError("Select Verification ID");
        } else if (Vndr_nme.getText().toString().equals("")) {
            validation = false;
            Vndr_nme.setError("Enter Name");
        } else if (Vndr_cntct.getText().toString().equals("")) {
            validation = false;
            Vndr_cntct.setError("Enter Contact No.");
        }else if (Vndr_vrfctn_name.getText().toString().equals("")) {
            validation = false;
            Vndr_vrfctn_name.setError("Enter Verification No.");
        }/*else if (serviceName.getText().toString().equals("")) {
            validation = false;
            serviceName.setError("Select Service");
        }*/else if (societyeName.getText().toString().equals("")) {
            validation = false;
            societyeName.setError("Select Society Name");
        }else if (VerIDName.getText().toString().equals("")) {
            validation = false;
            VerIDName.setError("Select Verification ID");
        }
        return validation;
    }

    @SuppressLint("ValidFragment")
    public static class verificaionID extends BottomSheetDialogFragment {
        ProgressDialog PD;
        String json_url;
        List<loginObject> VerIDData;
        ListView VrfctnIDList;
        TextView header;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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
            View contentView = View.inflate(getContext(), single_bottomsheet, null);
            dialog.setContentView(contentView);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior != null && behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
            }
            header = (TextView) contentView.findViewById(R.id.HeaderBottom);
            VrfctnIDList = (ListView) contentView.findViewById(R.id.service_data);
            header.setText("Identity Proof");
            forService();
        }

        public void forService() {
            PD.show();
            json_url = (getString(R.string.BASE_URL) + "/ServiceSociety");

            JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final ArrayList<String> listVerID;

//                JSONObject report_jsonobject = null;
                    JSONArray VerIDArray = null;
                    try {
                        listVerID = new ArrayList<>();
                        VerIDArray = response.getJSONArray("listDocType");
                        VerIDData = new ArrayList<>();
                        for (int i = 0; i < VerIDArray.length();) {
                            JSONObject VerificationJsonData = VerIDArray.getJSONObject(i);
                            loginObject loginObject_recycler = new loginObject();
                            loginObject_recycler.VerCode = VerificationJsonData.getString("Code");
                            loginObject_recycler.VerName = VerificationJsonData.getString("Name");
                            VerIDData.add(loginObject_recycler);
                            listVerID.add(String.valueOf(VerIDData.get(i).VerName));

                            String ID = VerIDData.get(i).VerCode;
                            String Name = VerIDData.get(i).VerName;
                            db.VerificationList(new loginObject(ID, Name));
                            i++;
                        }
                        VrfctnIDList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,listVerID));

                        VrfctnIDList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String VerName = listVerID.get(position).toString();

                                try{
                                    VerificationIDS = String.valueOf(db.getVerificationListID(VerName.toString()));
                                }catch(Exception e){e.printStackTrace();}

                                VerIDName.setText(VerName);
                                dismiss();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PD.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PD.dismiss();
                    Toast.makeText(getActivity(), "Server_Error", Toast.LENGTH_SHORT).show();
                    Log.w("error in response", "Error: " + error.getMessage());
                }
            });

            req.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            try {
                MyApplication.getInstance().addToReqQueue(req,"BottomService");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ValidFragment")
    public static class societyService extends BottomSheetDialogFragment {
        ProgressDialog PD;
        String json_url;
        List<loginObject> SocietyData;
        ListView societyList;
        TextView header;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
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
            View contentView = View.inflate(getContext(), single_bottomsheet, null);
            dialog.setContentView(contentView);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior != null && behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
            }
            societyList = (ListView) contentView.findViewById(R.id.service_data);
            header = (TextView) contentView.findViewById(R.id.HeaderBottom);
            header.setText("Society");
            forService();

        }

        public void forService() {
            PD.show();
            json_url = (getString(R.string.BASE_URL) + "/ServiceSociety");

            JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final ArrayList<String> listSociety;

//                JSONObject report_jsonobject = null;
                    JSONArray serviceArray = null;
                    try {
                        listSociety = new ArrayList<>();
                        serviceArray = response.getJSONArray("listSociety");
                        SocietyData = new ArrayList<>();
                        for (int i = 0; i < serviceArray.length();) {
                            JSONObject complaintJsonData = serviceArray.getJSONObject(i);
                            loginObject loginObject_recycler = new loginObject();
                            loginObject_recycler.societyCode = complaintJsonData.getString("Code");
                            loginObject_recycler.SocietyeName = complaintJsonData.getString("Name");
                            SocietyData.add(loginObject_recycler);
                            listSociety.add(String.valueOf(SocietyData.get(i).SocietyeName));

                            String ID = SocietyData.get(i).societyCode;
                            String Name = SocietyData.get(i).SocietyeName;
                            db.SocietyList(new loginObject(ID, Name));
                            i++;


                        }
                        societyList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,listSociety));

                        societyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String SocietyName = listSociety.get(position).toString();

                                try{
                                    SocietyIDS = String.valueOf(db.getSocietyListID(SocietyName.toString()));
                                }catch(Exception e){e.printStackTrace();}


                                societyeName.setText(SocietyName);
                                dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PD.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w("error in response", "Error: " + error.getMessage());
                }
            });


            req.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            try {
                MyApplication.getInstance().addToReqQueue(req,"BottomService");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void forService() {
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/ServiceSociety");

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray serviceArray = null;

                try {
                    listServices = new ArrayList<>();
                    serviceArray = response.getJSONArray("listService");
                    ServiceData = new ArrayList<>();
                    for (int i = 0; i < serviceArray.length();) {
                        JSONObject complaintJsonData = serviceArray.getJSONObject(i);
                        loginObject loginObject_recycler = new loginObject();
                        loginObject_recycler.ServiceId = complaintJsonData.getString("ServiceId");
                        loginObject_recycler.ServiceName = complaintJsonData.getString("ServiceName");
                        ServiceData.add(loginObject_recycler);
                        listServices.add(String.valueOf(ServiceData.get(i).ServiceName));

                        String ID = ServiceData.get(i).ServiceId;
                        String Name = ServiceData.get(i).ServiceName;
                        db.saveServices(new loginObject(ID, Name));
                        i++;
                    }
                    services = new String[listServices.size()];
                    services = listServices.toArray(services);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(vendor_Reg.this, "Server_Error", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        try {
            MyApplication.getInstance().addToReqQueue(req,"BottomService");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void forsubmit(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/InsertVendor");

        final String Vndr_Name = getEditTextString(Vndr_nme);
        final String vndr_cntct = getEditTextString(Vndr_cntct);
        final String vndr_vrfctn_name = getEditTextString(Vndr_vrfctn_name);

        final String ServiceName = serviceName.getText().toString();
        final String SocietyeName = societyeName.getText().toString();
        final String verIDName = VerIDName.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Name",Vndr_Name);
        params.put("ServiceType", String.valueOf(serviceIDS));
        params.put("Society",SocietyIDS);
        params.put("ContactNo",vndr_cntct);
        params.put("VerificationId",vndr_vrfctn_name);
        params.put("VerificationType",VerificationIDS);


        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String msg = loginData.getString("status");
                    if (msg.equalsIgnoreCase("Success")) {
                        PD.dismiss();
//                        passMessage = loginData.getString("message");
                        Vndr_nme.setText("");
                        Vndr_cntct.setText("");
                        Vndr_vrfctn_name.setText("");
                        VerIDName.setText("");
                        societyeName.setText("");
                        serviceName.setText("");
                        Toast.makeText(vendor_Reg.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        PD.dismiss();
                        Toast.makeText(vendor_Reg.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(vendor_Reg.this, "Server_Error", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
}



