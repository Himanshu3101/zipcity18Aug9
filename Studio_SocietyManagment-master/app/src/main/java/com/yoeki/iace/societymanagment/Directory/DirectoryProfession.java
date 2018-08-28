package com.yoeki.iace.societymanagment.Directory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IACE on 25-Aug-18.
 */

public class DirectoryProfession extends AppCompatActivity {
    profess_detail_recycler cadapter;
    ArrayList<String> Member_Detail;
    ProgressDialog PD;
    AppCompatTextView Filter_data;
    DBHandler db;
    AppCompatButton bck;
    Spinner srch_by_profess;
    String ProffessionIds;
    ArrayAdapter<String> profee_lst_Name;
    static ArrayList<String> ProfessionListArray,Mem_detailsArray;
    static List<String> professionaltList,mem_detail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profess_details);

        db = new DBHandler(this);
        Intent i = getIntent();
        Member_Detail = new ArrayList<String>();
        Member_Detail = i.getStringArrayListExtra("Member Details");
        String bhk_Type = i.getStringExtra("BHK type");
        String unit_stat = i.getStringExtra("Unit Stat");
        String tenant_Stat = i.getStringExtra("Tenant Stat");

        final RecyclerView recyclerView = findViewById(R.id.by_prof_detail);
        bck = findViewById(R.id.Dir_back_Details);
        srch_by_profess = findViewById(R.id.srch_by_profess);
        Filter_data = findViewById(R.id.Filter_data);

        if(tenant_Stat.equals("Not Available")) {
            Filter_data.setText(bhk_Type + ">>" + unit_stat);
        }
        else{
            Filter_data.setText(bhk_Type + ">>" + unit_stat+ ">>" + tenant_Stat);
        }

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               db.deleteMem_directory();
               Intent intent = new Intent(getApplicationContext(),Directory.class);
               startActivity(intent);
               finish();
            }
        });

        ProfessionListArray = new ArrayList<>();
        Mem_detailsArray = new ArrayList<>();
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        try {
            professionaltList = db.getProfeesionList();
            for (final String list : professionaltList) {
                String log = list;
                ProfessionListArray.add(log);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        profee_lst_Name =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner, ProfessionListArray);
        profee_lst_Name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srch_by_profess.setAdapter(profee_lst_Name);
        profee_lst_Name.insert("--Search by Profession--", 0);

        srch_by_profess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                String client_Selection = adapterView.getItemAtPosition(position).toString();

                if(!client_Selection.equals("--Search by Profession--")) {

                    for (int i = 0; i < ProfessionListArray.size(); i++) {
                        String listName = String.valueOf(ProfessionListArray.get(i));
                        if (listName.equals(client_Selection)) {
                            try {

                                mem_detail = db.getMemberDetails(listName);
                                for (final String list : mem_detail) {
                                    String log = list;
                                    Mem_detailsArray.add(log);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    cadapter = new profess_detail_recycler(getApplicationContext(),Mem_detailsArray);
                    recyclerView.setAdapter(cadapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cadapter = new profess_detail_recycler(this,Member_Detail);
        recyclerView.setAdapter(cadapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.deleteMem_directory();
        Intent intent = new Intent(getApplicationContext(),Directory.class);
        startActivity(intent);
        finish();
    }
}
