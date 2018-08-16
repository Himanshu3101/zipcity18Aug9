package com.yoeki.iace.societymanagment.Circular;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

public class Circular_Popup extends Activity {
    TextView P_title,P_fdate,P_tdate,P_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular__popup);

        this.setFinishOnTouchOutside(true);
        P_title =  findViewById(R.id.p_title);
        P_fdate =  findViewById(R.id.p_fdate);
        P_tdate =  findViewById(R.id.p_tdate);
        P_description =  findViewById(R.id.p_description);

        Intent intent= getIntent();
        String title = intent.getStringExtra("C_title");
        String fromdate = intent.getStringExtra("C_Fromdate");
        String todate = intent.getStringExtra("C_TODATE");
        String description = intent.getStringExtra("C_Descriptionn");

        P_title.setText(title);
        P_fdate.setText(fromdate);
        P_tdate.setText(todate);
        P_description.setText(description);



    }
}
