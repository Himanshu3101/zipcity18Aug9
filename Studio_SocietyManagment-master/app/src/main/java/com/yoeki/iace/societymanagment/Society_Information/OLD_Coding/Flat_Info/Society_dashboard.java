package com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.yoeki.iace.societymanagment.R;

/**
 * Created by IACE on 10-Jul-18.
 */

public class Society_dashboard extends AppCompatActivity{
//    AppCompatButton basic,lift,gates,flat,facility,parking;
    AppCompatImageButton bck;

    GridView SocietyInfogrid;
    String[] SocietyInfo = {
            "Basic Details",
            "Lift Info",
            "Gates Info",
            "Flat Info",
            "Parking Info",
            "Facility Info",


    } ;
    int[] SocietyInfoIcon = {
            R.drawable.basic_details_icon,
            R.drawable.lift_info_icon,
            R.drawable.gate_info_icon,
            R.drawable.flat_info_icon,
            R.drawable.parking_info_icon,
            R.drawable.facility_info_icon,


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.society_info_old);

//        basic = (AppCompatButton)findViewById(R.id.basic_Btn);
//        lift = (AppCompatButton)findViewById(R.id.lift_Btn);
//        gates = (AppCompatButton)findViewById(R.id.gates_Btn);
//        flat = (AppCompatButton)findViewById(R.id.flat_Btn);
//        facility = (AppCompatButton)findViewById(R.id.facility_Btn);
//        parking = (AppCompatButton)findViewById(R.id.parking_Btn);
        bck = (AppCompatImageButton)findViewById(R.id.Soc_back);

        CustomGridSocietyInfo adapter = new CustomGridSocietyInfo(Society_dashboard.this, SocietyInfo, SocietyInfoIcon);
        SocietyInfogrid=(GridView)findViewById(R.id.Society_gridview);
        SocietyInfogrid.setAdapter(adapter);

        SocietyInfogrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = null;
                if(position == 0){
                    myIntent = new Intent(view.getContext(), Basic.class);
                }
                if(position == 1){
                    myIntent = new Intent(view.getContext(), Lift_Info.class);
                }
                if(position ==2){
                    myIntent = new Intent(view.getContext(), Gates_Info.class);
                }
                if(position ==3){
                    myIntent = new Intent(view.getContext(), FlatInform.class);
                }
                if(position ==4){
                    myIntent = new Intent(view.getContext(), Parking_Info.class);
                }
                if(position ==5){
                    myIntent = new Intent(view.getContext(), FacilityInfo.class);
                }

                startActivity(myIntent);

            }
        });


        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home_Page.class);
                startActivity(intent);
                finish();
            }
        });

//        basic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Society_dashboard.this,Basic.class);
//                startActivity(intent);
//            }
//        });
//
//        lift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Society_dashboard.this, Lift_Info.class);
//                startActivity(intent);
//            }
//        });
//
//        gates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Society_dashboard.this, Gates_Info.class);
//                startActivity(intent);
//            }
//        });
//
//        parking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Society_dashboard.this, Parking_Info.class);
//                startActivity(intent);
//            }
//        });
//
//        flat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Society_dashboard.this, FlatInform.class);
//                startActivity(intent);
//            }
//        });
//
//        facility.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Society_dashboard.this, FacilityInfo.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Page.class);
        startActivity(intent);
        finish();
    }
}
