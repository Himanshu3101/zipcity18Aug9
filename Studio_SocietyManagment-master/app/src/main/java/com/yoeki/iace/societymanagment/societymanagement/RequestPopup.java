package com.yoeki.iace.societymanagment.societymanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

public class RequestPopup extends Activity {
    TextView R_title,R_type,R_name,R_flatno, R_createdon,R_createdby,R_requestnoo,R_status, R_fdate,R_tdate,R_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_popup);

        R_title =  findViewById(R.id.r_title);
        R_type =  findViewById(R.id.r_Type);
        R_name =  findViewById(R.id.r_name);
        R_flatno =  findViewById(R.id.r_flat);
        R_createdon =  findViewById(R.id.r_on);
        R_createdby =  findViewById(R.id.r_by);
        R_requestnoo =  findViewById(R.id.r_request);
        R_status =  findViewById(R.id.r_status);
        R_fdate =  findViewById(R.id.r_fdate);
        R_tdate =  findViewById(R.id.r_tdate);
        R_description =  findViewById(R.id.r_description);


        Intent intent= getIntent();
        String ttitle = intent.getStringExtra("R_title");
        String ttype = intent.getStringExtra("R_type");
        String nname = intent.getStringExtra("R_name");
        String fflat = intent.getStringExtra("R_flat");
        String ccreatedon = intent.getStringExtra("R_createdon");
        String ccreatedby = intent.getStringExtra("R_createdby");
        String rrequestno = intent.getStringExtra("R_requestno");
        String sstatus = intent.getStringExtra("R_status");
        String ffdate = intent.getStringExtra("R_fdate");
        String ttdate = intent.getStringExtra("R_tdate");
        String ddescr = intent.getStringExtra("R_descrfull");


        R_title.setText(ttitle);
        R_type.setText(ttype);
        R_name.setText(nname);
        R_flatno.setText(fflat);
        R_createdon.setText(ccreatedon);
        R_createdby.setText(ccreatedby);
        R_requestnoo.setText(rrequestno);
        R_status.setText(sstatus);
        R_fdate.setText(ffdate);
        R_tdate.setText(ttdate);
        R_description.setText(ddescr);

    }
}
