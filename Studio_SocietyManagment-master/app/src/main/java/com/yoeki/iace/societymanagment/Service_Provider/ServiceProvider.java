package com.yoeki.iace.societymanagment.Service_Provider;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.ChangePassword;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.Helpline.HelplineNo;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;
import com.yoeki.iace.societymanagment.Rules.Rules;
import com.yoeki.iace.societymanagment.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ServiceProvider extends AppCompatActivity {
    ServiceProviderRecyclerViewAdapter sadapter;

    private ArrayList<String> ServiceproviderList;
    Button menu,Ser_notify,profile;
    RecyclerView recyclerView;
    NavigationView navigation;
    String foepswdLayout;
    DrawerLayout drawer;
    ProgressDialog PD;
    private ArrayList<String> RequestList;
    List<loginObject> loginBData;
    DBHandler db;
    String filepathN = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.notify.txt";
    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.autologin.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider);


        foepswdLayout = getIntent().getStringExtra("chngepswd");
        String oldpsswd = getIntent().getStringExtra("oldpsswd");
        try {
            if (foepswdLayout.equals("1")) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                intent.putExtra("firstTIme_Member", "secondTme_Member");
                intent.putExtra("old_frst_pswd", oldpsswd);
                startActivity(intent);
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        db = new DBHandler(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView) findViewById(R.id.nav_view);
        menu = (Button)findViewById(R.id.menu);
        Ser_notify = (Button)findViewById(R.id.Ser_notify);
        profile = (Button)findViewById(R.id.Ser_profile);
        recyclerView = findViewById(R.id.serviceprovider);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        ServiceproviderList = new ArrayList<>();

        datafrrequest();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServiceProviderProfile.class);
                startActivity(intent);
            }
        });

        Ser_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.yoeki.iace.societymanagment.Notification.Notification.class);
                intent.putExtra("fromNotify","Serv");
                startActivity(intent);
            }
        });

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.helpline:
                        Intent intent=new Intent(ServiceProvider.this,HelplineNo.class);
                        intent.putExtra("fromHelpline","Serv");
                        startActivity(intent);
                        break;
                    case R.id.rules:
                        Intent intent1=new Intent(ServiceProvider.this,Rules.class);
                        intent1.putExtra("fromRules","Serv");
                        startActivity(intent1);
                        break;
                    case R.id.share:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Zipcity App");
                        String sAux = "\nLet me recommend you this application\n\n";
                        sAux = sAux + "https://play.google.com/store/apps?hl=en\n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "choose one"));
                        break;
                    case R.id.logout:
                        drawer.openDrawer(Gravity.START);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceProvider.this);
                        builder.setCancelable(false);
                        builder.setMessage("Do you want to Log-Out?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteall();

                                try {
                                    dlteusernameandpwd();
                                    deleteNotofy();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(ServiceProvider.this,login.class);
                                startActivity(intent);
//                    stopService(view);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if user select "No", just cancel this dialog and continue with app
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(ServiceProvider.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Log-Out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteall();

                    try {
                        dlteusernameandpwd();
                        deleteNotofy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ServiceProvider.this,login.class);
                    startActivity(intent);
//                    stopService(view);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void dlteusernameandpwd() throws IOException {
        File file = new File(filepath);
        file.delete();
    }

    public void deleteNotofy() throws IOException {
        File file = new File(filepathN);
        file.delete();
    }

    public void datafrrequest(){
        PD.show();
        String  json_url;
        HashMap<String, String> params;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID"," ");

            json_url = (getString(R.string.BASE_URL) + "/BindRequests");
            params = new HashMap<String, String>();
            params.put("userId",UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;
                String mes;

                RequestList = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    mes = loginData.getString("message");
                    if (resStatus.equalsIgnoreCase("Success")) {

//                      Request List
                        try {
                            BDetailArray = response.getJSONArray("list");
                            loginBData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length();) {
                                JSONObject BDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.req_title = BDetailJsonData.getString("title");
                                loginObject_recycler.req_type = BDetailJsonData.getString("type");
                                loginObject_recycler.req_vndr_nme = BDetailJsonData.getString("VendorName");
                                loginObject_recycler.req_unit = BDetailJsonData.getString("UnitNo");
                                loginObject_recycler.req_creat_on = BDetailJsonData.getString("createdOn");
                                loginObject_recycler.req_creat_by = BDetailJsonData.getString("CreatedBy");
                                loginObject_recycler.req_req_nu = BDetailJsonData.getString("RequestNo");
                                loginObject_recycler.req_req_id = BDetailJsonData.getString("requestId");
                                loginObject_recycler.req_status = BDetailJsonData.getString("status");
                                loginObject_recycler.req_fromdte = BDetailJsonData.getString("FromDate");
                                loginObject_recycler.req_todte = BDetailJsonData.getString("ToDate");
                                loginObject_recycler.req_desc = BDetailJsonData.getString("description");
                                loginObject_recycler.req_vendr_mobile = BDetailJsonData.getString("VendorContactNo");
                                loginObject_recycler.req_uniq_code = BDetailJsonData.getString("UniqueCode");
                                loginBData.add(loginObject_recycler);

                                String Req_name = loginBData.get(i).req_title;
                                String Req_type = loginBData.get(i).req_type;
                                String Req_vndrNme = loginBData.get(i).req_vndr_nme;
                                String Req_unit = loginBData.get(i).req_unit;
                                String Req_createdOn = loginBData.get(i).req_creat_on;
                                String Req_createdBy = loginBData.get(i).req_creat_by;
                                String Req_requestNo = loginBData.get(i).req_req_nu;
                                String Req_status = loginBData.get(i).req_status;
                                String Req_fromdte = loginBData.get(i).req_fromdte;
                                String Req_todte = loginBData.get(i).req_todte;
                                String Req_desc = loginBData.get(i).req_desc;
                                String Req_vendr_mobileNo = loginBData.get(i).req_vendr_mobile;
                                String Req_uniq = loginBData.get(i).req_uniq_code;
                                String Req_ID = loginBData.get(i).req_req_id;

                                String RequestDetails = Req_name+"~"+Req_type+"~"+Req_vndrNme+"~"+Req_unit+"~"+Req_createdOn+"~"+Req_createdBy+"~"+Req_requestNo
                                        +"~"+Req_status+"~"+Req_fromdte+"~"+Req_todte+"~"+Req_desc+"~"+Req_vendr_mobileNo+"~"+Req_uniq+"~"+Req_ID;

                                RequestList.add(RequestDetails);
                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        PD.dismiss();
                        recyclerfrVisitor();
                    }else{
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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

    public void recyclerfrVisitor(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sadapter = new ServiceProviderRecyclerViewAdapter(this, RequestList);
        recyclerView.setAdapter(sadapter);
    }
}
