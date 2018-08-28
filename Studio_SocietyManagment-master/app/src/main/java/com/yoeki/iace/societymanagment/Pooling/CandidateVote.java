package com.yoeki.iace.societymanagment.Pooling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;

public class CandidateVote extends Activity {

    CandidateDetailsRecyclerViewAdapter cadapter;
    AppCompatTextView title_poll;
    AppCompatButton save,next,submit,postDetails_bck;
    RecyclerView recyclerView;
    FrameLayout bck_frameLayut;
    int i =0;
    private ArrayList<String> CandidateDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_vote);

//        Intent i = getIntent();
//        String titl = i.getStringExtra("poll_tit");

        recyclerView = findViewById(R.id.candidaterecycle);
        title_poll = (AppCompatTextView)findViewById(R.id.title_poll);
        save = (AppCompatButton)findViewById(R.id.save);
        next = (AppCompatButton)findViewById(R.id.next);
        submit = (AppCompatButton)findViewById(R.id.submit);
        postDetails_bck = (AppCompatButton)findViewById(R.id.postDetails_bck);

        bck_frameLayut = (FrameLayout)findViewById(R.id.bck_frameLayut);

        title_poll.setText("Secretary");

        CandidateDetailsList = new ArrayList<>();

        CandidateDetailsList.add("Abhay Sharma");
        CandidateDetailsList.add("Vibhu Goel");
        CandidateDetailsList.add("Amit Singh");
        CandidateDetailsList.add("Rohit Yadav");

        recycler();

        postDetails_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==1){
                    Intent intent = new Intent(getApplicationContext(),CandidateVote.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(),PoolingSystem.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CandidateVote.this, "Data Save", Toast.LENGTH_SHORT).show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                next.setVisibility(View.GONE);
                bck_frameLayut.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                title_poll.setText("Chairman");
                CandidateDetailsList.clear();
                CandidateDetailsList.add("Shashank Tyagi");
                CandidateDetailsList.add("Varsha Sharma");
                CandidateDetailsList.add("Karan Singh");
                CandidateDetailsList.add("Pooja Yadav");
                recycler();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CandidateVote.this, "Data Successfully Submitted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),PoolingSystem.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void recycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cadapter = new CandidateDetailsRecyclerViewAdapter(this, CandidateDetailsList);
        recyclerView.setAdapter(cadapter);
    }
}
