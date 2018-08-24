package com.yoeki.iace.societymanagment;

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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.Circular.Circular;
import com.yoeki.iace.societymanagment.ComplaintNew.ComplaintManagementTab;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.Directory.Directory;
import com.yoeki.iace.societymanagment.Helpline.HelplineNo;
import com.yoeki.iace.societymanagment.Notification.Notification;
import com.yoeki.iace.societymanagment.Recharge.RechargeTab;
import com.yoeki.iace.societymanagment.RequestNew.RequestManagementTab;
import com.yoeki.iace.societymanagment.Rules.Rules;
import com.yoeki.iace.societymanagment.Services.Services;
import com.yoeki.iace.societymanagment.Society_Information.New.Society_Info;
import com.yoeki.iace.societymanagment.Visitors_Management.VisitorsManagement;
import com.yoeki.iace.societymanagment.profile.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home_Page extends AppCompatActivity {
TextView login_username;
    //    RecyclerView MenuRecycle;
//    private RecyclerView.Adapter Home_menu_adptr;
    String foepswdLayout,pswd;
    Button menu,notified,profile;
    NavigationView navigation;
    DrawerLayout drawer;
    List<String> UserRoleID;
    ArrayList<String> HomePageList;
    //    ArrayList<Drawable> SocietyIcon;
    ArrayList<String> finalRoleIDList;
    DBHandler db;
    String filepathN = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.notify.txt";
    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.autologin.txt";
    ProgressDialog PD;
    private ArrayList<String> Mem_List;
    List<loginObject> loginBData;

    GridView HomePagegrid;
    String[] SocietyName = {
            "Complaint",
            "Request",
            "Visitors ",
            "Services",
            "Payment",
            "Circular",
//            "Complaint/Request",
//
    } ;
    int[] SocietyIcon = {
            R.drawable.complaintmanagementicon,
            R.drawable.request_management_icon,
            R.drawable.visitorsmanagement_icon,
            R.drawable.societ_mgmnt,
            R.drawable.recharge_icon,
//            R.drawable.gallery,
            R.drawable.news,
//            R.drawable.regularpasses_icon,
//            R.drawable.tenant_icon,


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        login_username=(TextView)findViewById(R.id.loginusername);

        foepswdLayout = getIntent().getStringExtra("chngepswd");
        String oldpsswd = getIntent().getStringExtra("oldpsswd");
        try {
            if (foepswdLayout.equals("1")) {
                Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
                intent.putExtra("firstTIme_Member", "firstTme_Member");
                intent.putExtra("old_frst_pswd", oldpsswd);
                startActivity(intent);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        HomePagegrid=(GridView)findViewById(R.id.Homepage_gridview);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView) findViewById(R.id.nav_view);
        db = new DBHandler(this);
        menu = (Button)findViewById(R.id.menu);
        notified = (Button)findViewById(R.id.notify);
        profile = (Button)findViewById(R.id.profile);



        PD = new ProgressDialog(Home_Page.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);
        getUsername();

//        MenuRecycle = findViewById(R.id.recycler_view);

        HomePagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = null;
                if(position == 0){
                    myIntent = new Intent(view.getContext(), ComplaintManagementTab.class);
                }
                if(position == 1){
                    myIntent = new Intent(view.getContext(), RequestManagementTab.class);
                }
                if(position ==2){
                    myIntent = new Intent(view.getContext(), VisitorsManagement.class);
                }
                if(position ==3){
                    myIntent = new Intent(view.getContext(), Services.class);
                }
                if(position ==4){
                    myIntent = new Intent(view.getContext(), RechargeTab.class);
                }
                if(position ==5){
                    myIntent = new Intent(view.getContext(), Circular.class);
                }
//                if(position ==6){
//                    myIntent = new Intent(view.getContext(), VendorApproval.class);
//                }
                startActivity(myIntent);
                finish();

            }
        });

//        finalRoleIDList = new ArrayList<>();
//        bindRole();

//        if(finalRoleIDList.contains("6")) {
//            HomePageList = new ArrayList<>();
////            HomePageList.add("Dashboard");
//            HomePageList.add("Society Information");
//            HomePageList.add("Complaint Management");
//            HomePageList.add("Profile");
//            SocietyIcon = new int[]{R.drawable.society_management_icon, R.drawable.complaintmanagementicon/*, R.drawable.profileicon*/};
//        }
//
//        if(finalRoleIDList.contains("5")) {
//            HomePageList = new ArrayList<>();
////            HomePageList.add("Dashboard");
//            HomePageList.add("Society Information");
//            HomePageList.add("Request Management");
//            HomePageList.add("Complaint Management");
//            HomePageList.add("Visitors Management");
//            HomePageList.add("Profile");
//            HomePageList.add("Vendor List");
//            HomePageList.add("Vendor Approval");
//            HomePageList.add("Regular Passes");
//
//            SocietyIcon = new int[]{R.drawable.society_management_icon,
//                    R.drawable.request_management_icon,
//                    R.drawable.complaintmanagementicon,
//                    R.drawable.visitorsmanagement_icon,
////                    R.drawable.profileicon,
//                    R.drawable.vendorlist_icon,
//                    R.drawable.vendor_approval_icon,
//                    R.drawable.regularpasses_icon};
//        }
//
//        if(finalRoleIDList.contains("4")) {
//            HomePageList = new ArrayList<>();
////            HomePageList.add("Dashboard");
//            HomePageList.add("Society Information");
//            HomePageList.add("Request Management");
//            HomePageList.add("Profile");
//            try {
//                menu.findViewById(R.id.gallery).setVisibility(View.GONE);
//                menu.findViewById(R.id.helpline).setVisibility(View.GONE);
//                menu.findViewById(R.id.directory).setVisibility(View.GONE);
//                menu.findViewById(R.id.circular).setVisibility(View.GONE);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            SocietyIcon = new int[]{R.drawable.society_management_icon,
//                    R.drawable.request_management_icon};
////                    R.drawable.profileicon};
//
//        }
//
//        if(finalRoleIDList.contains("3")) {
//            HomePageList = new ArrayList<>();
////            HomePageList.add("Dashboard");
//            HomePageList.add("Society Information");
//            HomePageList.add("Request Management");
//            HomePageList.add("Complaint Management");
//            HomePageList.add("Visitors Management");
//            HomePageList.add("Profile");
//            HomePageList.add("Vendor List");
//            HomePageList.add("Tenant");
//
//            SocietyIcon = new int[]{R.drawable.society_management_icon,
//                    R.drawable.request_management_icon,
//                    R.drawable.complaintmanagementicon,
//                    R.drawable.visitorsmanagement_icon,
////                    R.drawable.profileicon,
//                    R.drawable.vendorlist_icon,
//                    R.drawable.tenant_icon};
//        }
//
//        if(finalRoleIDList.contains("2")) {
//            HomePageList = new ArrayList<>();
////            HomePageList.add("Dashboard");
//            HomePageList.add("Society Information");
//            HomePageList.add("Request Management");
//            HomePageList.add("Complaint Management");
//            HomePageList.add("Visitors Management");
//            HomePageList.add("Profile");
//            HomePageList.add("Vendor List");
//            HomePageList.add("Vendor Approval");
//            HomePageList.add("Regular Passes");
//            HomePageList.add("Tenant");
//
//            SocietyIcon = new int[]{R.drawable.society_management_icon,
//                    R.drawable.request_management_icon,
//                    R.drawable.complaintmanagementicon,
//                    R.drawable.visitorsmanagement_icon,
////                    R.drawable.profileicon,
//                    R.drawable.vendorlist_icon,
//                    R.drawable.vendor_approval_icon,
//                    R.drawable.regularpasses_icon,
//                    R.drawable.tenant_icon};
//        }
//
//        if(finalRoleIDList.contains("1")) {
//            HomePageList = new ArrayList<>();
////            HomePageList.add("Dashboard");
//            HomePageList.add("Society Information");
//            HomePageList.add("Request Management");
//            HomePageList.add("Complaint Management");
//            HomePageList.add("Visitors Management");
//            HomePageList.add("Profile");
//            HomePageList.add("Vendor List");
//            HomePageList.add("Vendor Approval");
//            HomePageList.add("Regular Passes");
//            HomePageList.add("Tenant");
//
//            SocietyIcon = new int[]{R.drawable.society_management_icon,
//            R.drawable.request_management_icon,
//            R.drawable.complaintmanagementicon,
//            R.drawable.visitorsmanagement_icon,
////            R.drawable.profileicon,
//            R.drawable.vendorlist_icon,
//            R.drawable.vendor_approval_icon,
//            R.drawable.regularpasses_icon,
//            R.drawable.tenant_icon};
//        }
//g
        CustomGridHomePage adapter = new CustomGridHomePage(Home_Page.this, /*HomePageList*/SocietyName, SocietyIcon);
        HomePagegrid.setAdapter(adapter);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        notified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.putExtra("fromNotify","Home");
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
            }
        });

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.societyInfo:
                        Intent intent=new Intent(Home_Page.this,Society_Info.class);
                        startActivity(intent);
                        break;
                    case R.id.helpline:
                        Intent intent2=new Intent(Home_Page.this,HelplineNo.class);
                        intent2.putExtra("fromHelpline","Home");
                        startActivity(intent2);
                        break;
                    case R.id.rules:
                        Intent intent1=new Intent(Home_Page.this,Rules.class);
                        intent1.putExtra("fromRules","Home");
                        startActivity(intent1);
                        break;
                    case R.id.directory:
                        Intent inten=new Intent(Home_Page.this,Directory.class);
                        //                        intent.putExtra("fromRules","Home");
                        startActivity(inten);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home_Page.this);
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
                                Intent intent = new Intent(Home_Page.this,login.class);
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

    public void bindRole() {
        UserRoleID = db.getRoleID();

        for (final String link : UserRoleID) {
            String log =  link;
            finalRoleIDList.add(log);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(Home_Page.this);
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
                    Intent intent = new Intent(Home_Page.this,login.class);
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

    public void getUsername(){
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
                String mes;
                JSONArray ProfileDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");
                    mes = loginData.getString("message");
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
                                login_username.setText(O_UserName);

                                i++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        PD.dismiss();
                    }else{
                        PD.dismiss();
                        Toast.makeText(Home_Page.this, mes, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
//               Toast.makeText(Profile.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        MyApplication.getInstance().addToReqQueue(req);
    }



//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//            /*if (id == R.id.gallery) {
//
//            } else if (id == R.id.circular) {
//
//            } else*/ if (id == R.id.directory) {
//
//            } else if (id == R.id.helpline) {
//                Intent intent = new Intent(getApplicationContext(),HelplineNo.class);
//                startActivity(intent);
//            } else if (id == R.id.send) {
//
//            } else if (id == R.id.rules) {
//
//            } else if (id == R.id.share) {
//
//            } else if (id == R.id.logout) {
//                drawer.openDrawer(Gravity.START);
//                AlertDialog.Builder builder = new AlertDialog.Builder(Home_Page.this);
//                builder.setCancelable(false);
//                builder.setMessage("Do you want to Log-Out?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        db.deleteall();
//                        Intent intent = new Intent(Home_Page.this,login.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //if user select "No", just cancel this dialog and continue with app
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
////        }
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
