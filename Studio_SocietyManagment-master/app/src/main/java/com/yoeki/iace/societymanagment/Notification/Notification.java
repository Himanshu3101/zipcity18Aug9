package com.yoeki.iace.societymanagment.Notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yoeki.iace.societymanagment.GateKeeper.GateKeeper;
import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.R;
import com.yoeki.iace.societymanagment.Service_Provider.ServiceProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationRecyclerViewAdapter nadapter;
    private ArrayList<String> NotificationList;
    Button Notify_bck;
    String toNotify;
    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.notify.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificattion);
        Notify_bck = findViewById(R.id.Notify_bck);
        recyclerView = findViewById(R.id.notification);
        NotificationList = new ArrayList<>();

        Intent intent= getIntent();
        toNotify = intent.getStringExtra("fromNotify");

        Notify_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toNotify.equals("Home")){
                    Intent intent1 =new Intent(getApplicationContext(), Home_Page.class);
                    startActivity(intent1);
                }else if (toNotify.equals("Serv")){
                    Intent intent1 =new Intent(getApplicationContext(), ServiceProvider.class);
                    startActivity(intent1);
                }else if (toNotify.equals("Gate")){
                    Intent intent1 =new Intent(getApplicationContext(), GateKeeper.class);
                    startActivity(intent1);
                }
                finish();
            }
        });


        readWriteFromFile(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(toNotify.equals("Home")){
            Intent intent1 =new Intent(getApplicationContext(), Home_Page.class);
            startActivity(intent1);
        }else if (toNotify.equals("Serv")){
            Intent intent1 =new Intent(getApplicationContext(), ServiceProvider.class);
            startActivity(intent1);
        }else if (toNotify.equals("Gate")){
            Intent intent1 =new Intent(getApplicationContext(), GateKeeper.class);
            startActivity(intent1);
        }
        finish();
    }

    private String readWriteFromFile(Context context) {

        String ret = "";

        try {
            File file = new File(filepath);
            if ( file != null ) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                fileReader.close();
                ret = stringBuilder.toString();
                if(ret.equals("")){

                }else{
                    String[] Break = ret.split("\\$");
                        try {
                            for (int i = 0; i < Break.length; i++) {
                                String[] B_notif = Break[i].split("~");
                                String tit = B_notif[0];
                                String desc = B_notif[1];
                                String F_Notify = tit+"~"+desc;
                                NotificationList.add(F_Notify);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    nadapter = new NotificationRecyclerViewAdapter(this, NotificationList);
                    recyclerView.setAdapter(nadapter);
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }
}
