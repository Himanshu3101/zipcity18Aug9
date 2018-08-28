package com.yoeki.iace.societymanagment.Directory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IACE on 22-Aug-18.
 */

public class Directory extends AppCompatActivity {
    AppCompatTextView dire_unit;
    AppCompatButton Btn_dire_unit,dir_submit,dir_bck;
    ProgressDialog PD;
    private ArrayList<String> UnitList,profession_list;
    DBHandler db;
    List<loginObject> UnitBData;
    static List<String> UNitList;
    static ArrayList<String> UnitReqArray;
    String[] Unit_list,profess_list;
    String UnitIds, selectedRadioButtonText,selectedRadioButtonTenant_stat,UnitBHK,unit_Status,tenant_Status;
    Boolean validation;
    RadioGroup Unit_stat,Tenant_stat;
    RadioButton unit_stat_Rent,unit_stat_owned;
    LinearLayout directory_tenant;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);

        db = new DBHandler(this);
        dire_unit = (AppCompatTextView) findViewById(R.id.dire_unit);
        Btn_dire_unit = (AppCompatButton) findViewById(R.id.Btn_dire_unit);
        dir_submit=(AppCompatButton)findViewById(R.id.dir_submit);
        dir_bck=(AppCompatButton)findViewById(R.id.dir_bck);
        Unit_stat = (RadioGroup) findViewById(R.id.unit_stat);
        Tenant_stat = (RadioGroup) findViewById(R.id.Tenant_stat);

        unit_stat_Rent = (RadioButton) findViewById(R.id.unit_stat_Rent);
        unit_stat_owned = (RadioButton) findViewById(R.id.unit_stat_owned);
        directory_tenant = (LinearLayout) findViewById(R.id.directory_tenant);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        BindRulesData();

        dir_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        dir_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Directory.this,DirectoryProfession.class);
                startActivity(intent);
            }
        });

        unit_stat_Rent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(unit_stat_Rent.isChecked()){
                    directory_tenant.setVisibility(View.VISIBLE);
                }else{
                    directory_tenant.setVisibility(View.INVISIBLE);
                }
            }
        });

        Btn_dire_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UNitList = new ArrayList<String>();
                UnitReqArray = new ArrayList<String>();
                UNitList = db.getUnit_List();
                for (final String list : UNitList) {
                    String log = list;
                    UnitReqArray.add(log);

                }
                Unit_list = UnitReqArray.toArray(new String[0]);
                if(Unit_list.length!=0) {
                    try {
                        new AlertDialog.Builder(Directory.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                                .setTitle("Select Unit Type")
                                .setSingleChoiceItems(Unit_list, 0, null)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                        ListView lw = ((AlertDialog) dialog).getListView();
                                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                        dire_unit.setText(checkedItem.toString());
                                        UnitBHK = checkedItem.toString();
                                        try {
                                            UnitIds = String.valueOf(db.getUnit_ListID(UnitBHK));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Directory.this, "Somthing went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dir_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_Method();
                    validation();
                if (validation == true) {
                    try {
                        forDirectory();
                    }catch(Exception e){
                        e.printStackTrace();
                        PD.dismiss();
                    }
                }
            }
        });
    }

    public boolean validation(){
        validation = true;
        if (dire_unit.getText().toString().equals("") || dire_unit.getText().toString().equals("null")) {
            validation = false;
            dire_unit.setError("Select Unit");
        }
        return validation;
    }

    public void radioButton_Method(){

        int selectedRadioButtonUnit = Unit_stat.getCheckedRadioButtonId();
        // If nothing is selected from Radio Group, then it return -1
        if (selectedRadioButtonUnit != -1) {
            RadioButton selectedRadioButton_Unit = (RadioButton) findViewById(selectedRadioButtonUnit);
            selectedRadioButtonText = selectedRadioButton_Unit.getText().toString();
            if(selectedRadioButtonText.equals("Self Owned")){
                selectedRadioButtonText = "O";
//                directory_tenant.setVisibility(View.GONE);
            }else{
                selectedRadioButtonText = "T";
//                directory_tenant.setVisibility(View.VISIBLE);
            }
        }else{
            selectedRadioButtonText = "N";
//            directory_tenant.setVisibility(View.GONE);
//            dire_unit.setText("Nothing selected from Radio Group.");
        }



        int selectedRadioButtonTenant = Tenant_stat.getCheckedRadioButtonId();
        // If nothing is selected from Radio Group, then it return -1
        if (selectedRadioButtonTenant != -1) {
            RadioButton selectedRadioButton_tenant = (RadioButton) findViewById(selectedRadioButtonTenant);
            selectedRadioButtonTenant_stat= selectedRadioButton_tenant.getText().toString();
            if(selectedRadioButtonTenant_stat.equals("")){
                selectedRadioButtonTenant_stat = "N";
            }else{
                selectedRadioButtonTenant_stat = "R";
            }
        }else{
            selectedRadioButtonTenant_stat = "";
//            dire_unit.setText("Nothing selected from Radio Group.");
        }

    }

    public void BindRulesData() {
        PD.show();
        String json_url = (getString(R.string.BASE_URL) + "/BindUnitType");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray UnitDetailArray = null;
                UnitList = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {
                        try {
//                           unit  Details
                            UnitDetailArray = response.getJSONArray("listUnitType");
                            UnitBData = new ArrayList<>();
                            for (int i = 0; i < UnitDetailArray.length();) {
                                JSONObject UnitDetailJsonData = UnitDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.Dir_UnitID = UnitDetailJsonData.getString("ProGroupId");
                                loginObject_recycler.Dir_Unit_Name = UnitDetailJsonData.getString("Name");
                                UnitBData.add(loginObject_recycler);

                                String U_Id = UnitBData.get(i).Dir_UnitID;
                                String U_Name = UnitBData.get(i).Dir_Unit_Name;
                                db.saveUnit(new loginObject(U_Id, U_Name));
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Directory.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(Directory.this, "Server_Error -"+error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }

    public void forDirectory() {
        PD.show();
//        UnitList = new ArrayList<>();
        String json_url = (getString(R.string.BASE_URL) + "/SearchUserDirectory");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");

        HashMap<String, String> params = new HashMap<String, String>();
            params.put("UserId",UID);
            params.put("UserType",selectedRadioButtonText);
            params.put("OccupancyStatus",selectedRadioButtonTenant_stat);
            params.put("ProGroupId",UnitIds);

        if(selectedRadioButtonTenant_stat.equals("R")){
            tenant_Status = "Available";
        }else{
            tenant_Status = "Not Available";
        }

        if(selectedRadioButtonText.equals("O")){
            unit_Status = "Self Owned";
        }else{
            unit_Status = "For Rent";
        }

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray UnitProfessionArray = null;
                UnitList = new ArrayList<>();
                profession_list = new ArrayList<>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("Success")) {

                        try {
//                           Profession Details
                            UnitProfessionArray = response.getJSONArray("listProfession");
                            UnitBData = new ArrayList<>();
                            try {
                                for (int i = 0; i < UnitProfessionArray.length(); ) {
                                    JSONObject UnitDetailJsonData = UnitProfessionArray.getJSONObject(i);
                                    loginObject loginObject_recycler = new loginObject();
                                    loginObject_recycler.Dir_ProfessionID = UnitDetailJsonData.getString("ProfessionId");
                                    loginObject_recycler.Dir_Profession_Name = UnitDetailJsonData.getString("Profession");
                                    UnitBData.add(loginObject_recycler);
                                    String U_Id = UnitBData.get(i).Dir_ProfessionID;
                                    String U_Name = UnitBData.get(i).Dir_Profession_Name;
                                    db.saveProfessioln(new loginObject(U_Id, U_Name));
                                    i++;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }


                        try {
//                           profession by  Details
                            UnitProfessionArray = response.getJSONArray("listSearchUserDirectory");
                            UnitBData = new ArrayList<>();
                            try {
                                for (int i = 0; i < UnitProfessionArray.length(); ) {
                                    JSONObject UnitDetailJsonData = UnitProfessionArray.getJSONObject(i);
                                    loginObject loginObject_recycler = new loginObject();
                                    loginObject_recycler.Dir_Profession = UnitDetailJsonData.getString("Profession");
                                    loginObject_recycler.Dir_Profession_UserName = UnitDetailJsonData.getString("UserName");
                                    loginObject_recycler.Dir_Profession_Locat = UnitDetailJsonData.getString("Location");
                                    loginObject_recycler.Dir_Profession_Mobile = UnitDetailJsonData.getString("MobileNo");

                                    UnitBData.add(loginObject_recycler);
                                    String Member_profession = UnitBData.get(i).Dir_Profession;
                                    String Member_name = UnitBData.get(i).Dir_Profession_UserName;
                                    String Member_location = UnitBData.get(i).Dir_Profession_Locat;
                                    String Member_mobile = UnitBData.get(i).Dir_Profession_Mobile;

                                    db.saveUserProfession_details(new loginObject(Member_profession, Member_name, Member_location, Member_mobile));

                                    String profess_data = Member_profession + "$" + Member_name + "$" + Member_location + "$" + Member_mobile;
                                    profession_list.add(profess_data);
                                    i++;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        PD.dismiss();

                        if(profession_list.size()==0) {
                            Toast.makeText(Directory.this, "Sorry, No Data Found", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(Directory.this, DirectoryProfession.class);
                            intent.putExtra("BHK type",UnitBHK);
                            intent.putExtra("Unit Stat",unit_Status);
                            intent.putExtra("Tenant Stat",tenant_Status);
                            intent.putStringArrayListExtra("Member Details", profession_list);
                            startActivity(intent);
                            finish();
                        }

                    }else{
                        PD.dismiss();
                        Toast.makeText(Directory.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(Directory.this, "Server_Error -"+error, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

}