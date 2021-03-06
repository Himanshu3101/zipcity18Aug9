package com.yoeki.iace.societymanagment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by IACE on 10-Jul-18.
 */

public class password_recovery extends AppCompatActivity {
    AppCompatEditText forget_Email;
    AppCompatButton register;
    Button back;
    Boolean validation;
    ProgressDialog PD;
    String resStatusForgetPassword,passMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pswd);

        forget_Email= (AppCompatEditText)findViewById(R.id.enter_email);
        back = (Button)findViewById(R.id.back_pass);
        register = (AppCompatButton)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    try {
                        forRegister();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
            }
        });

        PD = new ProgressDialog(password_recovery.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);
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

    public boolean validations() {
        validation = true;
        if (forget_Email.getText().toString().equals("")) {
            validation = false;
            forget_Email.setError("Enter Email");
        } else if (forget_Email.getText().toString().equals("")) {
            validation = false;
            forget_Email.setError("Enter Email");
        }
        return validation;
    }

    public  void forRegister(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/ForgetPassword");
        final String Email = forget_Email.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Email",Email);


        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray complaintArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    resStatusForgetPassword = loginData.getString("status");
                    if (resStatusForgetPassword.equalsIgnoreCase("Success")) {
                        PD.dismiss();
                        passMessage = loginData.getString("message");
                        Toast.makeText(password_recovery.this, passMessage, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),login.class);
                        startActivity(intent);
                        finish();
                    }else{
                        PD.dismiss();
                        Toast.makeText(password_recovery.this, resStatusForgetPassword, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(password_recovery.this, "Server_Error -"+error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
}
