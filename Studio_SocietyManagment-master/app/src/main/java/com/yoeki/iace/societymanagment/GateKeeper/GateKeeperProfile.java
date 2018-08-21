package com.yoeki.iace.societymanagment.GateKeeper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.ChangePassword;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;
import com.yoeki.iace.societymanagment.profile.RoundImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GateKeeperProfile extends AppCompatActivity {
    List<loginObject> loginBData;
    ProgressDialog PD;
    String UID;
    int GET_FROM_GALLERY;
    Bitmap bitmapP = null;
    String ba1;
    RoundImage roundImage1;
    AppCompatButton Submit;
    AppCompatImageButton gatekeeper_back,gate_changepassword;
    AppCompatEditText GateK_name/*,GateK_role*/,GateK_contactno;
    AppCompatImageView Gate_usr_logo;
    AppCompatImageButton Gate_chngepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gate_keeper_profile);
        GateK_name = (AppCompatEditText)findViewById(R.id.gatekeeper_name);
//        GateK_role = (AppCompatEditText)findViewById(R.id.gatekeeper_role);
        GateK_contactno = (AppCompatEditText)findViewById(R.id.gatekeeper_mobile);
        Submit=(AppCompatButton)findViewById(R.id.gatekeeper_submit);
        gatekeeper_back=(AppCompatImageButton)findViewById(R.id.gatekeeper_back);
        gate_changepassword=(AppCompatImageButton)findViewById(R.id.gate_changepassword);
        Gate_usr_logo = (AppCompatImageView)findViewById(R.id.Gate_usr_logo);
        Gate_chngepic = (AppCompatImageButton)findViewById(R.id.Gate_chngepic);

        PD = new ProgressDialog(GateKeeperProfile.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UID = prefs.getString("UserID"," ");

        BindGateKeeperProfile();

        gatekeeper_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GateKeeperProfile.this,GateKeeper.class);
                startActivity(intent);
            }
        });
        gate_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GateKeeperProfile.this,ChangePassword.class);
                intent.putExtra("frompassword","GateProfile");
                startActivity(intent);
            }
        });

        Gate_chngepic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

    public  void BindGateKeeperProfile(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/BindUserProfile");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray GPDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        String resMessage = loginData.getString("message");
                        try {
//                           Gate keeper profile
                            GPDetailArray = response.getJSONArray("listOwner");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < GPDetailArray.length();) {
                                JSONObject GPDetailJsonData = GPDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.GK_name = GPDetailJsonData.getString("UserName");
//                                loginObject_recycler.GK_role = GPDetailJsonData.getString("UserloginId");
                                loginObject_recycler.GK_contactno = GPDetailJsonData.getString("MobileNo");

                                loginBData.add(loginObject_recycler);

                                String G_Name = loginBData.get(i).GK_name;
                                String G_Role = loginBData.get(i).GK_role;
                                String G_Contactno = loginBData.get(i).GK_contactno;


                                GateK_name.setText(G_Name);
//                                GateK_role.setText(G_Role);
                                GateK_contactno.setText(G_Contactno);

                                i++;
                            }
                            PD.dismiss();
                        }catch(Exception e){
                            e.printStackTrace();
                            PD.dismiss();
                            Toast.makeText(GateKeeperProfile.this, "Please try after some time...", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        PD.dismiss();
                        Toast.makeText(GateKeeperProfile.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(GateKeeperProfile.this, "Server_Error -"+error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),GateKeeper.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            int width=200;
            int height=200;
            try {
                bitmapP = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmapP = Bitmap.createScaledBitmap(bitmapP, width,height, true);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapP.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte [] ba = bao.toByteArray();
                ba1= Base64.encodeToString(ba,Base64.DEFAULT);
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    byte[] decodedString = Base64.decode(ba1, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    roundImage1 = new RoundImage(decodedByte);
                    Gate_usr_logo.setImageDrawable(roundImage1);
//                    HttpClient httpclient = new DefaultHttpClient();
////                    HttpPost httppost = new HttpPost("http://192.168.10.19:"+getString(R.string.port)+"/AccessControl/webresources/WebServices/changeImage?code=5");
//                    HttpPost httppost = new HttpPost("http://"+ip+".novusapl-online.com:"+getString(R.string.port)+"/AccessControl/webresources/WebServices/changeImage?code="+Sresult+"");
//                    httppost.setEntity(new StringEntity(ba1));
//                    HttpResponse response = httpclient.execute(httppost);
//                    if((response.getStatusLine().getStatusCode()==200)||(response.getStatusLine().getStatusCode()==201)){
//                        String server_response = EntityUtils.toString(response.getEntity());
//                        if(server_response.equals("1")){
//                            Toast.makeText(this, "Pic Updated...", Toast.LENGTH_SHORT).show();
//                        }else{Toast.makeText(this, "Pic not Update, Please Try After Sometime", Toast.LENGTH_SHORT).show();}
//                        Log.i("Server response", server_response );
//                    } else {
//                        Log.i("Server response", "Failed to get server response" );
//                        Toast.makeText(this, "Try After Sometime.....", Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



