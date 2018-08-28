package com.yoeki.iace.societymanagment.Pooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yoeki.iace.societymanagment.Home_Page;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class PoolingSystem extends AppCompatActivity {

    PoolingSystemRecyclerViewAdapter padapter;
    AppCompatButton pooling_bck;
    private ArrayList<String> PoolingSystemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pooling_system);

        pooling_bck =(AppCompatButton)findViewById(R.id.pooling_bck);
        pooling_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home_Page.class);
                startActivity(intent);
                finish();
            }
        });
        PoolingSystemList = new ArrayList<>();
        PoolingSystemList.add("A Building Election");
        PoolingSystemList.add("B Building Election");
        PoolingSystemList.add("C Building Election");
        PoolingSystemList.add("D Building Election");

        RecyclerView recyclerView = findViewById(R.id.PoolingSystem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        padapter = new PoolingSystemRecyclerViewAdapter(this, PoolingSystemList);
        recyclerView.setAdapter(padapter);
    }
}
