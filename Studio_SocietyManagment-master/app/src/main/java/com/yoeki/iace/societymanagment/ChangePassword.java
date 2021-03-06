package com.yoeki.iace.societymanagment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.GateKeeper.GateKeeper;
import com.yoeki.iace.societymanagment.GateKeeper.GateKeeperProfile;
import com.yoeki.iace.societymanagment.Service_Provider.ServiceProvider;
import com.yoeki.iace.societymanagment.Service_Provider.ServiceProviderProfile;
import com.yoeki.iace.societymanagment.profile.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ChangePassword extends Activity {

    AppCompatImageButton addservicetype;
    List<loginObject> SrvcPrvdProfileBData;
    ProgressDialog PD;
    EditText oldpass,newpass,confpass;
    DBHandler dbHandler;
    AppCompatButton Submit;
    Boolean validation;
    String tochangePassword,first_pswd_lyout,home_pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        this.setFinishOnTouchOutside(true);
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        confpass = (EditText) findViewById(R.id.confpass);
        Submit = (AppCompatButton) findViewById(R.id.submit);
        dbHandler = new DBHandler(this);

        this.setFinishOnTouchOutside(true);
        PD = new ProgressDialog(ChangePassword.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        Intent intent= getIntent();
        tochangePassword = intent.getStringExtra("frompassword");
        try {
            if (tochangePassword.equals(null)) {
                tochangePassword = "empty";
            }
        }catch (Exception e){
            tochangePassword = "empty";
        }

        try {
            first_pswd_lyout = intent.getStringExtra("firstTIme_Member");
            if(first_pswd_lyout.equals(null)){
                first_pswd_lyout = "empty";
            }
        }catch (Exception e){
            first_pswd_lyout = "empty";
        }






        if(first_pswd_lyout.equals("firstTme_Member")){
            home_pswd = intent.getStringExtra("old_frst_pswd");
            oldpass.setText(home_pswd);
            oldpass.setEnabled(false);
        }else if(first_pswd_lyout.equals("secondTme_Member")){
            home_pswd = intent.getStringExtra("old_frst_pswd");
            oldpass.setText(home_pswd);
            oldpass.setEnabled(false);
        }else if(first_pswd_lyout.equals("Gatekeeper")){
            home_pswd = intent.getStringExtra("old_frst_pswd");
            oldpass.setText(home_pswd);
            oldpass.setEnabled(false);
        }

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    try {
                        final String New_Pass =  newpass.getText().toString();
                        final String Conf_Pass =  confpass.getText().toString();
                        if (New_Pass.equals(Conf_Pass)) {
                           ChangePasswordData();
                        } else {
                            Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public boolean validations() {
        validation = true;
        if (oldpass.getText().toString().equals("") && newpass.getText().toString().equals("") &&
                confpass.getText().toString().equals("")) {
            validation = false;
            oldpass.setError("Enter old Password");
            newpass.setError("Enter new Password");
            confpass.setError("Enter Confirm Password");

        } else if (oldpass.getText().toString().equals("")) {
            validation = false;
            oldpass.setError("Enter old password");
        } else if (newpass.getText().toString().equals("")) {
            validation = false;
            newpass.setError("Enter new password");
        } else if (confpass.getText().toString().equals("")) {
            validation = false;
            confpass.setError("Enter Confirm password");
        }
        return validation;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(tochangePassword.equals("Profile")){
            Intent intent1 =new Intent(getApplicationContext(), Profile.class);
            startActivity(intent1);
        }else if (tochangePassword.equals("ServceProfile")){
            Intent intent1 =new Intent(getApplicationContext(), ServiceProviderProfile.class);
            startActivity(intent1);
        }else if(tochangePassword.equals("GateProfile")){
            Intent intent1 =new Intent(getApplicationContext(), GateKeeperProfile.class);
            startActivity(intent1);
        }else if(first_pswd_lyout.equals("firstTme_Member")){
            Intent intent1 =new Intent(getApplicationContext(), Home_Page.class);
            startActivity(intent1);
        }else if(first_pswd_lyout.equals("secondTme_Member")){
            Intent intent1 =new Intent(getApplicationContext(), ServiceProvider.class);
            startActivity(intent1);
        }else if(first_pswd_lyout.equals("Gatekeeper")){
            Intent intent1 =new Intent(getApplicationContext(), GateKeeper.class);
            startActivity(intent1);
        }
        finish();
    }


    public void ChangePasswordData(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/ChangePassword");

        final String Old_Pass =  oldpass.getText().toString();
        final String New_Pass =  newpass.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("OldPassword",Old_Pass);
        params.put("NewPassword",New_Pass);
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String msg = loginData.getString("status");
                    String msg1 = loginData.getString("message");
                    if (msg.equalsIgnoreCase("Success")) {
                        PD.dismiss();
//                        passMessage = loginData.getString("message");
                        oldpass.setText("");
                        newpass.setText("");
                        Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(ChangePassword.this, Profile.class);
//                        startActivity(intent);
                        if(tochangePassword.equals("Profile")){
                            Intent intent1 =new Intent(getApplicationContext(), Profile.class);
                            intent1.putExtra("frompassword","Profile");
                            startActivity(intent1);
                        }else if (tochangePassword.equals("ServceProfile")){
                            Intent intent1 =new Intent(getApplicationContext(), ServiceProviderProfile.class);
                            intent1.putExtra("frompassword","ServceProfile");
                            startActivity(intent1);
                        }else if(tochangePassword.equals("GateProfile")){
                            Intent intent1 =new Intent(getApplicationContext(), GateKeeperProfile.class);
                            intent1.putExtra("frompassword","GateProfile");
                            startActivity(intent1);

                        }else if(first_pswd_lyout.equals("firstTme_Member")){
                            Intent intent1 =new Intent(getApplicationContext(), Home_Page.class);
                            startActivity(intent1);
                        }
                        finish();


                    }else{
                        PD.dismiss();
                        Toast.makeText(ChangePassword.this, "Problem Occured - "+msg1, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(ChangePassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}



