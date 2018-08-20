package com.yoeki.iace.societymanagment.Recharge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;


public class Recharge extends AppCompatActivity {

    RechargeRecyclerViewAdapter radapter;
    private ArrayList<String> RechargeList;
    static List<String> NV_FlatList;
    static ArrayList<String> NV_FlatListArray;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge);

        db=new DBHandler(this);



        RechargeList = new ArrayList<>();
        RechargeList.add("T1-204");
        RechargeList.add("T1-206");
        RechargeList.add("T1-209");
        RechargeList.add("T1-212");
        forService();

        RecyclerView recyclerView = findViewById(R.id.recharge);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        radapter = new RechargeRecyclerViewAdapter(this, RechargeList);
        recyclerView.setAdapter(radapter);

    }

    public void forService() {
        NV_FlatList = db.getFlatList();
        NV_FlatListArray = new ArrayList<String>();
        for (final String link : NV_FlatList) {
            String log =  link;
            NV_FlatListArray.add(log);
        }
        String VerName = NV_FlatListArray.toString();
//                visit_location.setText(VerName);
    }
}
