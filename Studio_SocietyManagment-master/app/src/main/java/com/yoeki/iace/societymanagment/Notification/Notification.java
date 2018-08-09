package com.yoeki.iace.societymanagment.Notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    NotificationRecyclerViewAdapter nadapter;

    private ArrayList<String> NotificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificattion);

        ArrayList<String> NotificationList = new ArrayList<>();
        NotificationList.add("society");
        NotificationList.add("visitor");
        NotificationList.add("request");
        NotificationList.add("complaint");

        RecyclerView recyclerView = findViewById(R.id.notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nadapter = new NotificationRecyclerViewAdapter(this, NotificationList);
        recyclerView.setAdapter(nadapter);
    }
}
