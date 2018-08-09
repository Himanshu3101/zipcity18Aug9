package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.FlatInfo_SocietyAdapter.ThreeBhkRecyclerViewAdapter;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class ThreeBhk extends AppCompatActivity {
    ThreeBhkRecyclerViewAdapter threeadapter;

    private ArrayList<String> ThreeBhkList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flat_three_bhk);

        ThreeBhkList = new ArrayList<>();
        ThreeBhkList.add("1 BHK");

        RecyclerView recyclerView = findViewById(R.id.threebhk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        threeadapter = new ThreeBhkRecyclerViewAdapter(this, ThreeBhkList);
        recyclerView.setAdapter(threeadapter);
    }
}
