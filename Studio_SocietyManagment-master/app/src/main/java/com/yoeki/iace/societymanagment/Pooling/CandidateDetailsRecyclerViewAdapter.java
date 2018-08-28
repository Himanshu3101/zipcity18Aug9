package com.yoeki.iace.societymanagment.Pooling;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class CandidateDetailsRecyclerViewAdapter extends RecyclerView.Adapter<CandidateDetailsRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    Context context ;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    CandidateDetailsRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.candidatedetailsrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String request = mData.get(position);
        holder.name.setText(request);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView name;
        AppCompatButton profile_candidate;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Candidate_name);
            profile_candidate = itemView.findViewById(R.id.profile_candidate);

            profile_candidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name_candid = name.getText().toString();
                    Intent intent = new Intent(context,Candidate_profile.class);
                    intent.putExtra("nameCandidate",name_candid);
                    context.startActivity(intent);
                }
            });
        }
    }
}
