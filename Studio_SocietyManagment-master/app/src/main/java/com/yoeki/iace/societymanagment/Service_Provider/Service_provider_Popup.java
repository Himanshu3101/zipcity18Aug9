package com.yoeki.iace.societymanagment.Service_Provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

/**
 * Created by IACE on 16-Aug-18.
 */

public class Service_provider_Popup extends Activity {

    TextView Service_prov_R_title,Service_prov_R_type,Service_prov_R_name,Service_prov_R_flatno,Service_prov_R_createdon,
            Service_prov_R_createdby,Service_prov_R_requestnoo,Service_prov_R_status, Service_prov_R_fdate,Service_prov_R_tdate,
            Service_prov_R_description;
//    Button req_closed;
//    String unCode,reque_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_request_popup);

        this.setFinishOnTouchOutside(true);
        Service_prov_R_title = (TextView)findViewById(R.id.Service_prov_r_title);
        Service_prov_R_type = (TextView)findViewById(R.id.Service_prov_r_Type);
        Service_prov_R_name = (TextView)findViewById(R.id.Service_prov_r_name);
        Service_prov_R_flatno = (TextView)findViewById(R.id.Service_prov_r_flat);
        Service_prov_R_createdon = (TextView)findViewById(R.id.Service_prov_r_on);
        Service_prov_R_createdby = (TextView)findViewById(R.id.Service_prov_r_by);
        Service_prov_R_requestnoo =(TextView)findViewById(R.id.Service_prov_r_request);
        Service_prov_R_status = (TextView)findViewById(R.id.Service_prov_r_status);
        Service_prov_R_fdate = (TextView)findViewById(R.id.Service_prov_r_fdate);
        Service_prov_R_tdate = (TextView)findViewById(R.id.Service_prov_r_tdate);
        Service_prov_R_description = (TextView)findViewById(R.id.Service_prov_r_description);

        Intent intent= getIntent();
        String ttitle = intent.getStringExtra("ser_R_title");
        String ttype = intent.getStringExtra("ser_R_type");
        String nname = intent.getStringExtra("ser_R_name");
        String fflat = intent.getStringExtra("ser_R_flat");
        String ccreatedon = intent.getStringExtra("ser_R_createdon");
        String ccreatedby = intent.getStringExtra("ser_R_createdby");
        String rrequestno = intent.getStringExtra("ser_R_requestno");
        String sstatus = intent.getStringExtra("ser_R_status");
        String ffdate = intent.getStringExtra("ser_R_fdate");
        String ttdate = intent.getStringExtra("ser_R_tdate");
        String ddescr = intent.getStringExtra("ser_R_descrfull");
        String stat = intent.getStringExtra("ser_R_state");

        Service_prov_R_title.setText(ttitle);
        Service_prov_R_type.setText(ttype);
        Service_prov_R_name.setText(nname);
        Service_prov_R_flatno.setText(fflat);
        Service_prov_R_createdon.setText(ccreatedon);
        Service_prov_R_createdby.setText(ccreatedby);
        Service_prov_R_requestnoo.setText(rrequestno);
        Service_prov_R_status.setText(sstatus);
        Service_prov_R_fdate.setText(ffdate);
        Service_prov_R_tdate.setText(ttdate);
        Service_prov_R_description.setText(ddescr);
    }


}
