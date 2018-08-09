package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.yoeki.iace.societymanagment.R;

import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.FlatInfo_SocietyAdapter.TwoBhkRecyclerViewAdapter;

import java.util.ArrayList;

public class TwoBhk extends AppCompatActivity {
    TwoBhkRecyclerViewAdapter twoadapter;

    private ArrayList<String> TwoBhkList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flat_two_bhk);

        TwoBhkList = new ArrayList<>();
        TwoBhkList.add("1 BHK");

        RecyclerView recyclerView = findViewById(R.id.twobhk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        twoadapter = new TwoBhkRecyclerViewAdapter(this, TwoBhkList);
        recyclerView.setAdapter(twoadapter);
    }
}
