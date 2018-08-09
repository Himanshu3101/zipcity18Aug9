package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.FacilityRecyclerViewAdapter;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class FacilityInfo extends AppCompatActivity {
    FacilityRecyclerViewAdapter fadapter;

    private ArrayList<String> FacilityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_info);

        ArrayList<String> FacilityList = new ArrayList<>();
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");
        FacilityList.add("Facility Name");

        RecyclerView recyclerView0 = findViewById(R.id.facility);
        recyclerView0.setLayoutManager(new LinearLayoutManager(this));
        fadapter = new FacilityRecyclerViewAdapter(this, FacilityList);
        recyclerView0.setAdapter(fadapter);

    }
}
