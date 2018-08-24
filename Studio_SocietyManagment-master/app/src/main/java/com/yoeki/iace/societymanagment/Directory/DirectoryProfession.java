package com.yoeki.iace.societymanagment.Directory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectoryProfession extends AppCompatActivity {

    ProgressDialog PD;
    List<loginObject> DirectoryProData;
    Button bck;
    DirectoryProRecyclerViewAdapter DirectoryProadapter;
    RecyclerView DirectoryProrecyclerView;
    private ArrayList<String> DirectoryProServicesList;
    DBHandler db;
    String groupid,usertypeid,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directoryprofession);
        db = new DBHandler(this);

        bck = (Button)findViewById(R.id.profession_bck);
        DirectoryProrecyclerView = findViewById(R.id.directoryprofession);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

        PD = new ProgressDialog(DirectoryProfession.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forProfession();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }

    public void forProfession() {
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/SearchUserDirectory");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);
//        params.put("ProGroupId",groupid);
//        params.put("UserTypeId",usertypeid);
//        params.put("OccupancyStatusId",status);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray ProfessionArray = null;
                try {
                    DirectoryProServicesList = new ArrayList<>();
                    ProfessionArray = response.getJSONArray("listSearchUserDirectory");
                    DirectoryProData = new ArrayList<>();
                    for (int i = 0; i < ProfessionArray.length();) {
                        JSONObject ProfessionJsonData = ProfessionArray.getJSONObject(i);
                        loginObject loginObject_recycler = new loginObject();
                        loginObject_recycler.DP_Unit_Typee = ProfessionJsonData.getString("Name");
                        loginObject_recycler.DP_Occupancy_Status = ProfessionJsonData.getString("OccupancyStatuss");
                        loginObject_recycler.DP_User_Type = ProfessionJsonData.getString("UserTypee");
                        loginObject_recycler.DP_Profession = ProfessionJsonData.getString("Profession");
                        loginObject_recycler.DP_Location = ProfessionJsonData.getString("Location");
                        loginObject_recycler.DP_Mobile = ProfessionJsonData.getString("MobileNo");
                        loginObject_recycler.DP_Username = ProfessionJsonData.getString("UserName");

                        DirectoryProData.add(loginObject_recycler);


                        String DP_UNIT = DirectoryProData.get(i).DP_Unit_Typee;
                        String DP_OCCUPANCY = DirectoryProData.get(i).DP_Occupancy_Status;
                        String DP_USER = DirectoryProData.get(i).DP_User_Type;
                        String DP_PROFESSION = DirectoryProData.get(i).DP_Profession;
                        String DP_LOCATION = DirectoryProData.get(i).DP_Location;
                        String DP_MOBILE = DirectoryProData.get(i).DP_Mobile;
                        String DP_USERNAME = DirectoryProData.get(i).DP_Username;

//                        String ProfessionDetails = DP_UNIT+"&"+DP_OCCUPANCY+"&"+DP_USER+"&"+DP_PROFESSION+"&"+DP_LOCATION+"&"+DP_MOBILE+"&"+DP_USERNAME;
                        DirectoryProServicesList.add(DP_PROFESSION);
//                        db.saveServices(new loginObject(ID, Name));
                        i++;

                    }
//                    services = new String[listServices.size()];
//                    services = listServices.toArray(services);
                    recycler();
                    PD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PD.dismiss();
            }
        }, new Response.ErrorListener() {
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

        try {
            MyApplication.getInstance().addToReqQueue(req,"BottomService");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void recycler(){
        DirectoryProrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DirectoryProadapter = new DirectoryProRecyclerViewAdapter(this, DirectoryProServicesList);
        DirectoryProrecyclerView.setAdapter(DirectoryProadapter);
    }
}
