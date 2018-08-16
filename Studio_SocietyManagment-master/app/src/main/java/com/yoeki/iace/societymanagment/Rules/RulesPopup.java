package com.yoeki.iace.societymanagment.Rules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

public class RulesPopup extends Activity {
    TextView R_title,R_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_popup);

        this.setFinishOnTouchOutside(true);
        R_title =  findViewById(R.id.r_title);
        R_description =  findViewById(R.id.r_description);


        Intent intent= getIntent();
        String title = intent.getStringExtra("R_title");
        String description = intent.getStringExtra("R_Descriptionn");

        R_title.setText(title);
        R_description.setText(description);

    }
}
