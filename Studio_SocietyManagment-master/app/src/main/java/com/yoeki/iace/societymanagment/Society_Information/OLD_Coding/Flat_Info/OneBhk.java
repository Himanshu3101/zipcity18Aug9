package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.FlatInfo_SocietyAdapter.OneBhkRecyclerViewAdapter;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class OneBhk extends AppCompatActivity {

   OneBhkRecyclerViewAdapter oneadapter;

    private ArrayList<String> OneBhkList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flat_one_bhk);

        OneBhkList = new ArrayList<>();
        OneBhkList.add("1 BHK");

        RecyclerView recyclerView = findViewById(R.id.onebhk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        oneadapter = new OneBhkRecyclerViewAdapter(this, OneBhkList);
        recyclerView.setAdapter(oneadapter);
    }
}
