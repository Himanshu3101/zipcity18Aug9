package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.yoeki.iace.societymanagment.R;

import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.FlatInfo_SocietyAdapter.FlatInfoRecyclerViewAdapter;

import java.util.ArrayList;

public class FlatInform extends AppCompatActivity {

    FlatInfoRecyclerViewAdapter flatadapter;

    private ArrayList<String> FlatInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flat_inform);

        FlatInfoList = new ArrayList<>();
        FlatInfoList.add("1 BHK");


        RecyclerView recyclerView = findViewById(R.id.mainrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        flatadapter = new FlatInfoRecyclerViewAdapter(this, FlatInfoList);
        recyclerView.setAdapter(flatadapter);
    }
}
