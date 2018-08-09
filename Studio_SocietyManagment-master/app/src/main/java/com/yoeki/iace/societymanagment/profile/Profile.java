package com.yoeki.iace.societymanagment.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity {
    AppCompatEditText Po_username,Po_phonenumber,Po_emailid,Po_dob,Po_flatno,Po_parking;
    AppCompatImageButton edit_Name,edit_Phone,edit_Email,edit_Dateofbirth,edit_flatno,edit_Parking;
    List<loginObject> loginBData;
    Button Update;
    Boolean validation;

    AppCompatTextView name,role,address,allot_parking,allot_unit;
    AppCompatImageButton add_member,Pro_bck;
    ProfileRecyclerViewAdapter profileadapter;
    private ArrayList<String> Mem_List;
    RecyclerView recyclerView;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

//        Po_username = (AppCompatEditText)findViewById(R.id.gusername);
//        Po_phonenumber = (AppCompatEditText)findViewById(R.id.gphonenumber);
//        Po_emailid = (AppCompatEditText)findViewById(R.id.emailid);
//        Po_dob = (AppCompatEditText)findViewById(R.id.dob);
//        Po_flatno = (AppCompatEditText)findViewById(R.id.flat);
//        Po_parking = (AppCompatEditText)findViewById(R.id.parking);
//        Update = (Button) findViewById(R.id.updte);
//        edit_Name=(AppCompatImageButton)findViewById(R.id.edit_name);
//        edit_Phone=(AppCompatImageButton)findViewById(R.id.edit_phone);
//        edit_Email=(AppCompatImageButton)findViewById(R.id.edit_email);
//        edit_Dateofbirth=(AppCompatImageButton)findViewById(R.id.edit_DOB);
//        edit_flatno=(AppCompatImageButton)findViewById(R.id.edit_flat);
//        edit_Parking=(AppCompatImageButton)findViewById(R.id.edit_parking);

        name = (AppCompatTextView)findViewById(R.id.name);
        role = (AppCompatTextView)findViewById(R.id.role);
        address = (AppCompatTextView)findViewById(R.id.address);
        allot_parking = (AppCompatTextView)findViewById(R.id.allot_parking);
        allot_unit = (AppCompatTextView)findViewById(R.id.allot_unit);
        Pro_bck=(AppCompatImageButton)findViewById(R.id.Pro_bck);
        add_member=(AppCompatImageButton)findViewById(R.id.add_member);
        recyclerView = findViewById(R.id.member_list);

        Pro_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
            }
        });

        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Popup.class);
                startActivity(intent);
            }
        });

        PD = new ProgressDialog(Profile.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        BindUserProfile();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        UID = prefs.getString("UserID"," ");
//
//        PD = new ProgressDialog(Profile.this);
//        PD.setMessage("Loading...");
//        PD.setCancelable(false);




//        edit_Name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Po_username.setText(Po_username.getText().toString());
//                Po_username.setFocusableInTouchMode(true);
//                Po_username.requestFocus();
//
//            }
//        });
//        edit_Phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        edit_Email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        edit_Dateofbirth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        edit_flatno.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        edit_Parking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//
//
//        Update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    validations();
//                    if(validation==true){
//
//                    }
////                        forProfileDataUpdate();
//                }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//
//        });
//        forProfileDataRetrieve();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

   public void BindUserProfile(){
       PD.show();
       Mem_List = new ArrayList<>();
       String json_url=(getString(R.string.BASE_URL) + "/BindUserProfile");

       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       String UID = prefs.getString("UserID"," ");

       HashMap<String, String> params = new HashMap<String, String>();
       params.put("UserId",UID);

       JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
               params), new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               JSONArray ProfileDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

               try {
                   JSONObject loginData = new JSONObject(String.valueOf(response));
                   String resStatus = loginData.getString("status");
                   if (resStatus.equalsIgnoreCase("Success")) {

//                           Owner Details
                       try {
                           ProfileDetailArray = response.getJSONArray("listOwner");
                           loginBData = new ArrayList<>();
                           for (int i = 0; i < ProfileDetailArray.length();) {
                               JSONObject BDetailJsonData = ProfileDetailArray.getJSONObject(i);
                               loginObject loginObject_recycler = new loginObject();
                               loginObject_recycler.Owner_name = BDetailJsonData.getString("UserName");
                               loginBData.add(loginObject_recycler);
                               String O_UserName = loginBData.get(i).Owner_name;
                               name.setText(O_UserName);
                               i++;
                           }
                       }catch(Exception e){
                           e.printStackTrace();
                       }

//                      Assigen Parking
                       try {
                           ProfileDetailArray = response.getJSONArray("listAssignParking");
                           loginBData = new ArrayList<>();
                           for (int i = 0; i < ProfileDetailArray.length();) {
                               JSONObject BDetailJsonData = ProfileDetailArray.getJSONObject(i);
                               loginObject loginObject_recycler = new loginObject();
                               loginObject_recycler.New_Parking_Count = BDetailJsonData.getString("NoOfCount");
                               loginBData.add(loginObject_recycler);
                               String Parking_count = loginBData.get(i).New_Parking_Count;
                               allot_parking.setText(Parking_count);
                               i++;
                           }
                       }catch(Exception e){
                           e.printStackTrace();
                       }

//                      Assigen Unit
                       try {
                           ProfileDetailArray = response.getJSONArray("listAssignUnits");
                           loginBData = new ArrayList<>();
                           for (int i = 0; i < ProfileDetailArray.length();) {
                               JSONObject BDetailJsonData = ProfileDetailArray.getJSONObject(i);
                               loginObject loginObject_recycler = new loginObject();
                               loginObject_recycler.New_Unit_Count = BDetailJsonData.getString("NoOfCount");
                               loginBData.add(loginObject_recycler);
                               String Unit_count = loginBData.get(i).New_Unit_Count;
                               allot_unit.setText(Unit_count);
                               i++;
                           }
                       }catch(Exception e){
                           e.printStackTrace();
                       }

//                     Member Details
                       try {
                           ProfileDetailArray = response.getJSONArray("list");
                           loginBData = new ArrayList<>();
                           for (int i = 0; i < ProfileDetailArray.length();) {
                               JSONObject BDetailJsonData = ProfileDetailArray.getJSONObject(i);
                               loginObject loginObject_recycler = new loginObject();
                               loginObject_recycler.Mem_Name = BDetailJsonData.getString("UserName");
                               loginObject_recycler.Mem_Gender = BDetailJsonData.getString("Gender");
                               loginObject_recycler.Mem_Mobile = BDetailJsonData.getString("MobileNo");
                               loginObject_recycler.Mem_Status = BDetailJsonData.getString("UserStatus");
                               loginBData.add(loginObject_recycler);

                               String M_name = loginBData.get(i).Mem_Name;
                               String M_Gender = loginBData.get(i).Mem_Gender;
                               String M_Mobile = loginBData.get(i).Mem_Mobile;
                               String M_Status = loginBData.get(i).Mem_Status;

                               String MemberDetais = M_name+","+M_Gender+","+M_Mobile+","+M_Status;
                               Mem_List.add(MemberDetais);
                               i++;
                           }
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                        recycler();
                       PD.dismiss();
                   }else{
                       PD.dismiss();
                       Toast.makeText(Profile.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.w("error in response", "Error: " + error.getMessage());
           }
       });
       MyApplication.getInstance().addToReqQueue(req);
    }

    public void recycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileadapter = new ProfileRecyclerViewAdapter(this, Mem_List);
        recyclerView.setAdapter(profileadapter);
    }
}
