package com.yoeki.iace.societymanagment.Service_Provider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IACE on 16-Aug-18.
 */

public class service_closed extends Activity {
    EditText Ser_Prov_Closed_submit_code;
    Button Ser_Prov_Closed_submit;
    ProgressDialog PD;
    String UID,state,idReques,coeVerif;
    private ArrayList<String> RequestList;
    Boolean validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_prov_service_closed);

        Ser_Prov_Closed_submit_code = (EditText)findViewById(R.id.Ser_Prov_Closed_submit_code);
        Ser_Prov_Closed_submit = (Button)findViewById(R.id.Ser_Prov_Closed_submit);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        Intent intent= getIntent();
        state = intent.getStringExtra("Status_send");
        idReques = intent.getStringExtra("id_request");
        coeVerif = intent.getStringExtra("code");

        Ser_Prov_Closed_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    if (Ser_Prov_Closed_submit_code.getText().toString().equals(coeVerif)){
                        try {
                            datafrClosedByService();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(service_closed.this, "Verification Code is Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean validations() {
        validation = true;
            if (Ser_Prov_Closed_submit_code.getText().toString().equals("") || Ser_Prov_Closed_submit_code.getText().toString().equals(null)){
                validation = false;
                Ser_Prov_Closed_submit_code.setError("Enter Verification Code");

            }
        return validation;
    }

    public void datafrClosedByService(){
        PD.show();
        String  json_url;
        HashMap<String, String> params;
        json_url=(getString(R.string.BASE_URL) + "/ChangeStatus");
        params = new HashMap<String, String>();
        params.put("userId",UID);
        params.put("Flag","R");
        params.put("Id",idReques);
        params.put("Status",state);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;

                RequestList = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    String mess = loginData.getString("message");
                    if (resStatus.equals("Success")){
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Request Closed Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(),ServiceProvider.class);
                        startActivity(intent);
                    }else{
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Something Went Wrong - "+mess, Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(), "Something went Wrong :"+error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
}
