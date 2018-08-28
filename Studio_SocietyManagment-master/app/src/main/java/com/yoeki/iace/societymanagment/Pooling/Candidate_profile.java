package com.yoeki.iace.societymanagment.Pooling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.yoeki.iace.societymanagment.R;

/**
 * Created by IACE on 27-Aug-18.
 */

public class Candidate_profile extends Activity {
    AppCompatTextView Cand_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_profile);

        this.setFinishOnTouchOutside(true);
        Cand_name = (AppCompatTextView)findViewById(R.id.Cand_name);
        Intent i = getIntent();
        String cand = i.getStringExtra("nameCandidate");
        Cand_name.setText(cand);
    }
}
