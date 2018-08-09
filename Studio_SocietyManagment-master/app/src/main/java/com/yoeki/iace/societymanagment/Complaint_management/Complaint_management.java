package com.yoeki.iace.societymanagment.Complaint_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yoeki.iace.societymanagment.Adapter.ComplaintRecyclerViewAdapter;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class Complaint_management extends AppCompatActivity {
    ComplaintRecyclerViewAdapter cadapter;
    private ArrayList<String> ComplaintList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_management);

        ArrayList<String> ComplaintList = new ArrayList<>();
        ComplaintList.add("Complaint");
        ComplaintList.add("Complaint");
        ComplaintList.add("Complaint");
        ComplaintList.add("Complaint");

        RecyclerView recyclerView = findViewById(R.id.complaint);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cadapter = new ComplaintRecyclerViewAdapter(this, ComplaintList);
        recyclerView.setAdapter(cadapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Complaint_management.this,ComplaintCreated.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }



    }

