package com.yoeki.iace.societymanagment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.Firebase.MyFirebaseInstanceID;
import com.yoeki.iace.societymanagment.Firebase.SharedPreferenceUtils;
import com.yoeki.iace.societymanagment.GateKeeper.GateKeeper;
import com.yoeki.iace.societymanagment.Service_Provider.ServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.simbio.encryption.Encryption;

public class login extends AppCompatActivity {
    AppCompatEditText Uname,Pswd;
    ProgressDialog PD;
    DBHandler db;
    AppCompatButton Forget_pswd,Vendor_regs;
    AppCompatButton Login;
    Boolean conn,validation,encrypted = false;
    String unme,pwd;
    String forchangepswd = "1";

    private MyFirebaseInstanceID mService;
    private boolean mBounded;
    private static final String TAG = "Login";

    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.autologin.txt";


    String json_url,resStatus,resMessage,resUserID,resUserName,resUserRole,tokenR,UserWiseId;
    List<loginObject> loginComplaintData,requestComplaintData,rejectionListData,bindUserData,userWiseRoleIdData,visitorData;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final ServiceConnection mConnection = new ServiceConnection() {
        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<MyFirebaseInstanceID>) service).getService();
            mBounded = true;
            Toast.makeText(login.this, "onServiceConnected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Toast.makeText(login.this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directory();
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        // when logout the user then this code is used for clear the login screen
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);
        setContentView(R.layout.activity_login);
        db = new DBHandler(this);


        Uname = (AppCompatEditText)findViewById(R.id.uname);
        Pswd = (AppCompatEditText)findViewById(R.id.pswd);
        Forget_pswd = (AppCompatButton)findViewById(R.id.forget_pswd);
        Login = (AppCompatButton)findViewById(R.id.log_in);
        Vendor_regs = (AppCompatButton) findViewById(R.id.vndr_rgstn);

//        Uname.setText("arun@gmail.com");
//        Pswd.setText("SveLJf");

//        Uname.setText("9013688825");
//        Pswd.setText("000000");

        PD = new ProgressDialog(login.this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        Uname.setText("");
        Pswd.setText("");
        autologin();
        if(encrypted==true) {
            forlogin();
            forchangepswd = "0";
        }

        Forget_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,password_recovery.class);
                startActivity(intent);
            }
        });

        Vendor_regs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,vendor_Reg.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNetworkAvailable();
                if (conn == true) {
                    validations();
                    if (validation == true) {
                        try {
                            forlogin();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Login.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(login.this, Home_Page.class);
                startActivity(intent);
                return false;
            }
        });

        getDeviceToken();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
    }

    public String getDeviceToken() {
        tokenR = SharedPreferenceUtils.getInstance(this).getStringValue(getString(R.string.firebase_cloud_messaging_token), "").equals("") ?
                FirebaseInstanceId.getInstance().getToken() : SharedPreferenceUtils.getInstance(this).getStringValue(getString(R.string.firebase_cloud_messaging_token), "");
//        return SharedPreferenceUtils.getInstance(this).getStringValue(getString(R.string.firebase_cloud_messaging_token), "").equals("") ? FirebaseInstanceId.getInstance().getToken() : SharedPreferenceUtils.getInstance(this).getStringValue(getString(R.string.firebase_cloud_messaging_token), "");

        return tokenR;
    }

    private boolean isNetworkAvailable() {
        conn = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            Toast.makeText(login.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            conn = false;
        }
        return conn;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public boolean validations() {
        validation = true;
        if (Uname.getText().toString().equals("") && Pswd.getText().toString().equals("")) {
            validation = false;
            Uname.setError("Enter Username");
            Pswd.setError("Enter Password");
        } else if (Uname.getText().toString().equals("")) {
            validation = false;
            Uname.setError("Enter Username");
        } else if (Pswd.getText().toString().equals("")) {
            validation = false;
            Pswd.setError("Enter Password");
        }
        return validation;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getEditTextString(AppCompatEditText et){
        return et.getText().toString();
    }

    public  void forlogin() {
        Toast.makeText(this, tokenR, Toast.LENGTH_SHORT).show();
        PD.show();
        json_url = (getString(R.string.BASE_URL) + "/LoginUser");
        final String userName = getEditTextString(Uname);
        final String password = getEditTextString(Pswd);

//        if (tokenR.equals(null) || tokenR.equals("") || tokenR.equals(" ")) {
//            PD.dismiss();
//            Toast.makeText(this, "Try Again...", Toast.LENGTH_SHORT).show();
//        } else {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("emailId", userName);
        params.put("password", password);
        params.put("IMEI", "5678");
        params.put("Token", tokenR);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray complaintArray = null, requestTypeArray = null, rejectionListArray = null, bindVisitorArray = null, bindUserArray = null, userWiseRoleArray = null;

                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    resStatus = loginData.getString("status");
                    if (resStatus.equalsIgnoreCase("success")) {
                        getDeviceToken();
                        db.deleteall();

                        resMessage = loginData.getString("status");
                        resUserID = loginData.getString("userId");
                        resUserName = loginData.getString("userName");
                        resUserRole = loginData.getString("userName");


                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("UserID", resUserID);
                        edit.commit();


                        try {
//                            Complaint List
                            ArrayList<String> listVerID = new ArrayList<>();
                            complaintArray = response.getJSONArray("list");
                            loginComplaintData = new ArrayList<>();
                            for (int i = 0; i < complaintArray.length(); ) {
                                JSONObject complaintJsonData = complaintArray.getJSONObject(i);
                                loginObject loginObject_recycler = new loginObject();
                                loginObject_recycler.ComplaintTypeId = complaintJsonData.getString("ComplaintTypeId");
                                loginObject_recycler.ComplaintTypeName = complaintJsonData.getString("ComplaintTypeName");
                                loginComplaintData.add(loginObject_recycler);

                                String ID = loginComplaintData.get(i).ComplaintTypeId;
                                String Name = loginComplaintData.get(i).ComplaintTypeName;
                                db.saveComplaint(new loginObject(ID, Name));
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
//                            Request List
                            requestTypeArray = response.getJSONArray("list1");
                            requestComplaintData = new ArrayList<>();
                            for (int i = 0; i < requestTypeArray.length(); ) {
                                JSONObject requestTypeJsonData = requestTypeArray.getJSONObject(i);
                                loginObject RequestObject_recycler = new loginObject();
                                RequestObject_recycler.RequestTypeId = requestTypeJsonData.getString("RequestTypeId");
                                RequestObject_recycler.RequestTypeName = requestTypeJsonData.getString("RequestTypeName");
                                requestComplaintData.add(RequestObject_recycler);

                                String ID = requestComplaintData.get(i).RequestTypeId;
                                String Name = requestComplaintData.get(i).RequestTypeName;
                                db.saveRequest(new loginObject(ID, Name));
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
//                            Rejection List
                            rejectionListArray = response.getJSONArray("rejectionList");
                            rejectionListData = new ArrayList<>();
                            for (int i = 0; i < rejectionListArray.length(); ) {
                                JSONObject rejectionJsonData = rejectionListArray.getJSONObject(i);
                                loginObject rejectionObject_recycler = new loginObject();
                                rejectionObject_recycler.RejectionId = rejectionJsonData.getString("id");
                                rejectionObject_recycler.RejectionName = rejectionJsonData.getString("name");
                                rejectionListData.add(rejectionObject_recycler);

                                String ID = rejectionListData.get(i).RejectionId;
                                String Name = rejectionListData.get(i).RejectionName;
                                db.saveRejection(new loginObject(ID, Name));
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
//                            User Flat List
                            bindUserArray = response.getJSONArray("BindUserFlatlist");
                            bindUserData = new ArrayList<>();
                            for (int i = 0; i < bindUserArray.length(); ) {
                                JSONObject bindUserJsonData = bindUserArray.getJSONObject(i);
                                loginObject bindUser_recycler = new loginObject();
                                bindUser_recycler.Location = bindUserJsonData.getString("Location");
                                bindUser_recycler.UnitMasterDetailId = bindUserJsonData.getString("UnitMasterDetailId");
                                bindUserData.add(bindUser_recycler);

                                String Name = bindUserData.get(i).UnitMasterDetailId;
                                String ID = bindUserData.get(i).Location;
                                db.saveFlatList(new loginObject(Name, ID));
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
//                            UserWise RoleID List
                            userWiseRoleArray = response.getJSONArray("UserWiseRoleIdlist");
                            userWiseRoleIdData = new ArrayList<>();
                            for (int i = 0; i < userWiseRoleArray.length(); ) {
                                JSONObject userWiseRoleJsonData = userWiseRoleArray.getJSONObject(i);
                                loginObject userWiseRole_recycler = new loginObject();
                                userWiseRole_recycler.UserRoleId = userWiseRoleJsonData.getString("UserRoleId");
                                userWiseRoleIdData.add(userWiseRole_recycler);

                                UserWiseId = userWiseRoleIdData.get(i).UserRoleId;
                                db.saveUserWiseRoleID(new loginObject(UserWiseId));
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
//                            Visitor List
                            bindVisitorArray = response.getJSONArray("VisitorTypelist");
                            visitorData = new ArrayList<>();
                            for (int i = 0; i < bindVisitorArray.length(); ) {
                                JSONObject userWiseRoleJsonData = bindVisitorArray.getJSONObject(i);
                                loginObject userWiseRole_recycler = new loginObject();

                                userWiseRole_recycler.visitor_lst_id = userWiseRoleJsonData.getString("VisitorTypeId");
                                userWiseRole_recycler.visitor_nme = userWiseRoleJsonData.getString("VisitorTypeName");
                                visitorData.add(userWiseRole_recycler);

                                String ID = visitorData.get(i).visitor_lst_id;
                                String Name = visitorData.get(i).visitor_nme;
                                db.saveVisitorList(new loginObject(Name, ID));
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        PD.dismiss();
                        if(UserWiseId.equals("4")){
                            Intent intent = new Intent(login.this, ServiceProvider.class);
                            intent.putExtra("chngepswd",forchangepswd);
                            intent.putExtra("oldpsswd",password);
                            startActivity(intent);
                        }else if(UserWiseId.equals("3")){
                            Intent intent = new Intent(login.this, Home_Page.class);
                            intent.putExtra("chngepswd",forchangepswd);
                            intent.putExtra("oldpsswd",password);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(login.this, GateKeeper.class);
                            intent.putExtra("chngepswd",forchangepswd);
                            intent.putExtra("oldpsswd",password);
                            startActivity(intent);
                        }
                        writenitem();

                    } else {
                        PD.dismiss();
                        Toast.makeText(login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(login.this, "Server_Error", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        MyApplication.getInstance().addToReqQueue(req);
//        }
    }

    public void writenitem(){
        try {
            File file = new File(filepath);
            file.createNewFile();
            writenameandpswd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writenameandpswd() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
            String uname = Uname.getText().toString();
            String psswd = Pswd.getText().toString();
            Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
            unme = encryption.encryptOrNull(uname);
            pwd = encryption.encryptOrNull(psswd);
            bufferedWriter.write(unme);
            bufferedWriter.write(":");
            bufferedWriter.write(pwd);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void directory() {
        File dir = new File("/mnt/sdcard/Android/data/com.android.ZipCity");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean autologin() {
        try {
            encrypted = false;
            File file = new File(filepath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line,line1 = "";
            while ((line = bufferedReader.readLine()) != null)  line1+=line; {
                stringBuffer.append(line1);
                String[] ipparts = line1.split("\\:");
                try {
                    String ip1 = String.valueOf(ipparts[0]);
                    String ip2 = String.valueOf(ipparts[1]);
                    Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
                    String Eusname = encryption.decryptOrNull(ip1);
                    String Epasswd = encryption.decryptOrNull(ip2);
                    String afteencryip = Eusname.toString();
                    String aftrencryport = Epasswd.toString();
                    Uname.setText(afteencryip);
                    Pswd.setText(aftrencryport);
                    encrypted = true;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encrypted;
    }

}
