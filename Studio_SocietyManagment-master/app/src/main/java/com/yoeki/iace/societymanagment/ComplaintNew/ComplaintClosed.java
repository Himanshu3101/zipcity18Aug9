package com.yoeki.iace.societymanagment.ComplaintNew;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ComplaintClosed extends Activity {
    EditText Closed_code;
    Button com_changed;
    ProgressDialog PD;
    String Compl_ID,StatusCompl,UCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_closed);

        this.setFinishOnTouchOutside(true);
        Intent intent= getIntent();
        UCode = intent.getStringExtra("Unique");
        Compl_ID = intent.getStringExtra("Comp_ID");
        StatusCompl  = intent.getStringExtra("stat");

        Closed_code = (EditText)findViewById(R.id.Closed_code);
        com_changed = (Button)findViewById(R.id.com_changed);
        Closed_code.setText(UCode);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        com_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StatusCompl.equals("Closed")) {
                    Toast.makeText(ComplaintClosed.this, "Already Closed", Toast.LENGTH_SHORT).show();
                }else{
                    if(UCode.equals("N/A")){
                        Toast.makeText(ComplaintClosed.this, "Already Closed ", Toast.LENGTH_SHORT).show();
                    }else{
                        statusChanged();
                    }
                }
            }
        });
    }

    public void statusChanged(){
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/ChangeStatus");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId",UID);
        params.put("Flag","C");
        params.put("Status","R");
        params.put("Id",Compl_ID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");

                    if (resStatus.equalsIgnoreCase("Success")) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Complaint Successfully Closed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(),ComplaintManagementTab.class);
                        startActivity(intent);
                        finish();
                    }else{
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
}
