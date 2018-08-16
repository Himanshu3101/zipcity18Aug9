package com.yoeki.iace.societymanagment.societymanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

public class ComplaintPopup extends Activity {
    TextView C_title,C_type,C_name,C_flatno, C_createdon,C_createdby,C_complaintno,C_status,C_description;
    Button complaintclosed;
    String uniqueC,Complaint_ID,sstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_popup);
        this.setFinishOnTouchOutside(true);
        C_title =(TextView)  findViewById(R.id.c_title);
        C_type =(TextView)  findViewById(R.id.c_Type);
        C_name = (TextView) findViewById(R.id.c_name);
        C_flatno = (TextView) findViewById(R.id.c_flat);
        C_createdon =(TextView) findViewById(R.id.c_on);
        C_createdby =(TextView)  findViewById(R.id.c_by);
        C_complaintno = (TextView) findViewById(R.id.c_complaint);
        C_status =(TextView)  findViewById(R.id.c_status);
         C_description =(TextView)  findViewById(R.id.c_description);
         complaintclosed=(Button)findViewById(R.id.closed);


        Intent intent= getIntent();
        String ttitle = intent.getStringExtra("Title");
        String ttype = intent.getStringExtra("type");
        String nname = intent.getStringExtra("vndr_nme");
        String fflat = intent.getStringExtra("Unit");
        String ccreatedon = intent.getStringExtra("Cre_On");
        String ccreatedby = intent.getStringExtra("Cre_By");
        String rrequestno = intent.getStringExtra("Complt No");
        sstatus = intent.getStringExtra("Status");
        String ddescr = intent.getStringExtra("Description");
        uniqueC = intent.getStringExtra("unique");
        Complaint_ID = intent.getStringExtra("ComplaintID");


        C_title.setText(ttitle);
        C_type.setText(ttype);
        C_name.setText(nname);
        C_flatno.setText(fflat);
        C_createdon.setText(ccreatedon);
        C_createdby.setText(ccreatedby);
        C_complaintno.setText(rrequestno);
        C_status.setText(sstatus);
         C_description.setText(ddescr);


        complaintclosed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ComplaintPopup.this,ComplaintClosed.class);
                intent.putExtra("Unique",uniqueC);
                intent.putExtra("Comp_ID",Complaint_ID);
                intent.putExtra("stat",sstatus);
                startActivity(intent);
            }
        });


    }
}
