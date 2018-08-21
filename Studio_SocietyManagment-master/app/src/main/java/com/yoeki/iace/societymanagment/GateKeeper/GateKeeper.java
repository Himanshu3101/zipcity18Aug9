package com.yoeki.iace.societymanagment.GateKeeper;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.ChangePassword;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.Helpline.HelplineNo;
import com.yoeki.iace.societymanagment.Home_Page;
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

public class GateKeeper extends AppCompatActivity {
    GateKeeperRecyclerViewAdapter gadapter;

    private ArrayList<String> GateKeeperList;
    Button menu, notified, profile;
    NavigationView navigation;
    DrawerLayout drawer;
    ProgressDialog PD;
    List<loginObject> gatekeeperData;
    RecyclerView gatkeeper;
    DBHandler db;
    String UID,foepswdLayout;
    String filepathN = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.notify.txt";
    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.autologin.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getekeeperlist);

        foepswdLayout = getIntent().getStringExtra("chngepswd");
        String oldpsswd = getIntent().getStringExtra("oldpsswd");
        try {
            if (foepswdLayout.equals("1")) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                intent.putExtra("firstTIme_Member", "Gatekeeper");
                intent.putExtra("old_frst_pswd", oldpsswd);
                startActivity(intent);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView) findViewById(R.id.nav_view);
        menu = (Button) findViewById(R.id.menu);
        notified = (Button) findViewById(R.id.gate_notify);
        profile = (Button) findViewById(R.id.profile);
        db = new DBHandler(this);

        gatkeeper = (RecyclerView) findViewById(R.id.VisitorsList);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        PD = new ProgressDialog(GateKeeper.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        forGateKeeperData();

        notified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.yoeki.iace.societymanagment.Notification.Notification.class);
                intent.putExtra("fromNotify","Gate");
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GateKeeperProfile.class);
                startActivity(intent);
            }
        });


        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.helpline:
                        Intent intent=new Intent(GateKeeper.this,HelplineNo.class);
                        intent.putExtra("fromHelpline","GateKeeper");
                        startActivity(intent);
                        break;
                    case R.id.rules:
                        Intent intent1=new Intent(GateKeeper.this,Rules.class);
                        intent1.putExtra("fromRules","GateKeeper");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(GateKeeper.this);
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
                                Intent intent = new Intent(GateKeeper.this,login.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(GateKeeper.this);
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
                    Intent intent = new Intent(GateKeeper.this,login.class);
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

    public void forGateKeeperData() {
        PD.show();
        String json_url = (getString(R.string.BASE_URL) + "/BindVisitors");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String UID = prefs.getString("UserID", " ");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", UID);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;


                GateKeeperList = new ArrayList<String>();
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");

                    if (resStatus.equalsIgnoreCase("Success")) {

//                      Request List
                        try {
                            BDetailArray = response.getJSONArray("list");
                            gatekeeperData = new ArrayList<>();
                            for (int i = 0; i < BDetailArray.length(); ) {
                                JSONObject GateDetailJsonData = BDetailArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();

                                loginObject_recycler.GK_Name = GateDetailJsonData.getString("Name");
                                loginObject_recycler.GK_Address = GateDetailJsonData.getString("Address");
                                loginObject_recycler.GK_Fdate = GateDetailJsonData.getString("FromDate");
                                loginObject_recycler.GK_Tdate = GateDetailJsonData.getString("ToDate");
                                loginObject_recycler.GK_Status = GateDetailJsonData.getString("Status");
                                loginObject_recycler.GK_ContactNo = GateDetailJsonData.getString("ContactNo");

                                gatekeeperData.add(loginObject_recycler);

                                String G_name = gatekeeperData.get(i).GK_Name;
                                String G_Address = gatekeeperData.get(i).GK_Address;
                                String G_fdate = gatekeeperData.get(i).GK_Fdate;
                                String G_tdate = gatekeeperData.get(i).GK_Tdate;
                                String G_status = gatekeeperData.get(i).GK_Status;
                                String G_contactnot = gatekeeperData.get(i).GK_ContactNo;


                                String GatekeeperDetails = G_name + "~" + G_Address + "~" + G_fdate + "~" + G_tdate + "~" + G_status + "~" + G_contactnot;
                                GateKeeperList.add(GatekeeperDetails);
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PD.dismiss();
                        recyclerfrVisitor();
                    } else {
                        PD.dismiss();
                        Toast.makeText(GateKeeper.this, "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(GateKeeper.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });
        MyApplication.getInstance().addToReqQueue(req);
    }

    public void recyclerfrVisitor() {
        gatkeeper.setLayoutManager(new LinearLayoutManager(GateKeeper.this));
        gadapter = new GateKeeperRecyclerViewAdapter(GateKeeper.this,GateKeeperList);
        gatkeeper.setAdapter(gadapter);
    }
}