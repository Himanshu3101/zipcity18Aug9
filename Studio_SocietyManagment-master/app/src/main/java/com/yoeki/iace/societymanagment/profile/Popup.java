package com.yoeki.iace.societymanagment.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
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
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IACE on 01-Aug-18.
 */

public class Popup extends AppCompatActivity {
    AppCompatImageView imageView;
    AppCompatImageButton chngepic;
    static AppCompatTextView Member_gender;
    AppCompatEditText Mem_username,Mem_contact;
    static ArrayList<String> GenderArray;
    AppCompatButton Mem_gender,mem_submit;
    int GET_FROM_GALLERY;
    Bitmap bitmapP = null;
    RoundImage roundImage1;
    String ba1;
    Boolean validation;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepopup);

        imageView = (AppCompatImageView)findViewById(R.id.usr_logo);
        chngepic = (AppCompatImageButton)findViewById(R.id.chngepic);
        Mem_username = (AppCompatEditText)findViewById(R.id.Mem_username);
        Mem_contact = (AppCompatEditText)findViewById(R.id.Mem_contact);
        Member_gender = (AppCompatTextView)findViewById(R.id.Member_gender);
        Mem_gender = (AppCompatButton)findViewById(R.id.Mem_gender);
        mem_submit = (AppCompatButton)findViewById(R.id.mem_submit);

        PD = new ProgressDialog(Popup.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        chngepic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        Mem_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BottomSheetDialogFragment Mem_gender = new mem_gender();
                    Mem_gender.show(getSupportFragmentManager(), Mem_gender.getTag());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        mem_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
                if (validation == true) {
                    try {
                        forMemDetailsubmit();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Profile.class);
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
                    imageView.setImageDrawable(roundImage1);
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

    @SuppressLint("ValidFragment")
    public static class mem_gender extends BottomSheetDialogFragment {
        ProgressDialog PD;
        ListView societyFLatList;
        TextView header;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            GenderArray = new ArrayList<>();
            GenderArray.add("Male");
            GenderArray.add("Fe-Male");
            GenderArray.add("Other");
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
            header.setText("Select Gender");
            forService();

        }

        public void forService() {
            societyFLatList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_item,GenderArray));

            societyFLatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String VerName = GenderArray.get(position).toString();
                    Member_gender.setText(VerName);
                    dismiss();
                }
            });
        }
    }

    public boolean validations() {
        validation = true;
        if (Mem_username.getText().toString().equals("") && Mem_contact.getText().toString().equals("") &&
                Member_gender.getText().toString().equals("")) {
            validation = false;
            Mem_username.setError("Enter Member name");
            Mem_contact.setError("Enter Coontact");
            Member_gender.setError("Select Gender");
        } else if (Mem_username.getText().toString().equals("")) {
            validation = false;
            Mem_username.setError("Enter Member name");
        } else if (Mem_contact.getText().toString().equals("")) {
            validation = false;
            Mem_contact.setError("Enter Coontact");
        }else if (Member_gender.getText().toString().equals("")) {
            validation = false;
            Member_gender.setError("Select Gender");
        }
        return validation;
    }

    public void forMemDetailsubmit(){
        PD.show();
        String json_url=(getString(R.string.BASE_URL) + "/InsertUserProfile");

        final String Mem_name = Mem_username.getText().toString();
        final String Mem_Contact = Mem_contact.getText().toString();
        final String Mem_Gender = Member_gender.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserName",Mem_name);
        params.put("MobileNo",Mem_Contact);
        params.put("Gender",Mem_Gender);
        params.put("UserLoginId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        Toast.makeText(Popup.this, "Request Created Successfully...", Toast.LENGTH_SHORT).show();
                        PD.dismiss();
                        Intent intent = new Intent(Popup.this,Profile.class);
                        startActivity(intent);
                        finish();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Popup.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(Popup.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }
}
