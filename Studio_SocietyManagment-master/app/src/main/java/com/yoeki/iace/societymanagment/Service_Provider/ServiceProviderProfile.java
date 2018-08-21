package com.yoeki.iace.societymanagment.Service_Provider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ServiceProviderProfile extends AppCompatActivity {

    SrvcPrvdeProfileRecyclerViewAdapter padapter;
    AppCompatTextView profilename,profilecontactno;
//    AppCompatImageButton addservicetype;
    int GET_FROM_GALLERY;
    private ArrayList<String> SrvcProfileList;
    Button srvcprvd_changepassword,Ser_Pro_bck;
    List<loginObject> SrvcPrvdProfileBData;
    RecyclerView SrvcPrvdProfilerecyclerView;
    ProgressDialog PD;
    AppCompatImageButton chngepic;
    protected String[] services;
    Bitmap bitmapP = null;
    String ba1;
    RoundImage roundImage1;
    AppCompatImageView Serv_prof;
    protected ArrayList<CharSequence> selectedServices = new ArrayList<CharSequence>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_profile);
        srvcprvd_changepassword = ( Button) findViewById(R.id.srvcprvd_changepassword);
        profilename=(AppCompatTextView)findViewById(R.id.serviceprovider_name);
        profilecontactno=(AppCompatTextView)findViewById(R.id.profilecontactno);
        Ser_Pro_bck=(Button)findViewById(R.id.Ser_Pro_bck);
        chngepic = (AppCompatImageButton)findViewById(R.id.Ser_chngepic);
        Serv_prof = (AppCompatImageView)findViewById(R.id.Serv_prof);
//        addservicetype=(AppCompatImageButton)findViewById(R.id.addservicetype);

        SrvcPrvdProfilerecyclerView = findViewById(R.id.profile_servicetype);

        PD = new ProgressDialog(ServiceProviderProfile.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);


        srvcprvd_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProviderProfile.this, ChangePassword.class);
                intent.putExtra("frompassword","ServceProfile");
                startActivity(intent);
            }
        });

        chngepic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        Ser_Pro_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ServiceProvider.class);
                startActivity(intent);
                finish();
            }
        });

        BindServiceProviderProfileData();

//        addservicetype.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ServiceTypePopup();
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),ServiceProvider.class);
        startActivity(intent);
        finish();
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
                    Serv_prof.setImageDrawable(roundImage1);
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




    public void ServiceTypePopup(){
        boolean[] checkedServices = new boolean[services.length];
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

            }

        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Services");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


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

    public void BindServiceProviderProfileData() {
        PD.show();
        SrvcProfileList = new ArrayList<>();
        String json_url = (getString(R.string.BASE_URL) + "/BindUserProfile");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);


        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray ProfileDetailArray = null;
                String mssg;
                SrvcProfileList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    mssg = loginData.getString("message");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           Profile Details
                            ProfileDetailArray = response.getJSONArray("listOwner");
                            SrvcPrvdProfileBData = new ArrayList<>();
                            for (int i = 0; i < ProfileDetailArray.length();) {
                                JSONObject ProfileDetailJsonData = ProfileDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Profile_Name = ProfileDetailJsonData.getString("UserName");
                                loginObject_recycler.Profile_Contact = ProfileDetailJsonData.getString("MobileNo");

                                SrvcPrvdProfileBData.add(loginObject_recycler);

                                String P_Name = SrvcPrvdProfileBData.get(i).Profile_Name;
                                String P_Contact = SrvcPrvdProfileBData.get(i).Profile_Contact;
                                profilename.setText(P_Name);
                                profilecontactno.setText(P_Contact);

                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        try {
//                           service types Details
                            ProfileDetailArray = response.getJSONArray("listVendorService");
                            SrvcPrvdProfileBData = new ArrayList<>();
                            for (int i = 0; i < ProfileDetailArray.length();) {
                                JSONObject ProfileDetailJsonData = ProfileDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Profile_ServiceType = ProfileDetailJsonData.getString("Rolee");
                                SrvcPrvdProfileBData.add(loginObject_recycler);

                                String P_servicetype = SrvcPrvdProfileBData.get(i).Profile_ServiceType;

                                SrvcProfileList.add(P_servicetype);
                                i++;
                                services = new String[SrvcProfileList.size()];
                                services = SrvcProfileList.toArray(services);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        recycler();
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(ServiceProviderProfile.this, mssg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
//                    Toast.makeText(ServiceProviderProfile.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }
    public void recycler(){
        SrvcPrvdProfilerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        padapter = new SrvcPrvdeProfileRecyclerViewAdapter(this, SrvcProfileList);
        SrvcPrvdProfilerecyclerView.setAdapter(padapter
        );
    }
}


