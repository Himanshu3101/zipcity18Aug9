package com.yoeki.iace.societymanagment.Recharge;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class StatementPopup extends Activity {

    StatementPopupRecyclerViewAdapter sadapter;
    private ArrayList<String> StatementPopupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statement_popup);

        this.setFinishOnTouchOutside(true);
        StatementPopupList = new ArrayList<>();
        StatementPopupList.add("17/08/2018");
        StatementPopupList.add("17/08/2018");
        StatementPopupList.add("17/08/2018");
        StatementPopupList.add("17/08/2018");


        RecyclerView recyclerView =  findViewById(R.id.statementpopup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sadapter = new StatementPopupRecyclerViewAdapter( this ,StatementPopupList);
        recyclerView.setAdapter(sadapter);

    }
}
