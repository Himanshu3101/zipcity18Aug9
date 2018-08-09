package com.yoeki.iace.societymanagment.Adapter.SocietyAdapter.FlatInfo_SocietyAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class FlatInfoRecyclerViewAdapter extends RecyclerView.Adapter<FlatInfoRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    Context context;

    // data is passed into the constructor
    public FlatInfoRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.flatrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        holder.OneBhk.setText(unit);
        holder.TwoBhk.setText(unit);
        holder.ThreeBhk.setText(unit);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView OneBhk;
        TextView TwoBhk;
        TextView ThreeBhk;

        ViewHolder(View itemView) {
            super(itemView);
            OneBhk = itemView.findViewById(R.id.oneid);
            TwoBhk = itemView.findViewById(R.id.twoid);
            ThreeBhk = itemView.findViewById(R.id.threeid);

            OneBhk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info.OneBhk.class);
                    context.startActivity(intent);

                }
            });
            TwoBhk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info.TwoBhk.class);
                    context.startActivity(intent);

                }
            });
            ThreeBhk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info.ThreeBhk.class);
                    context.startActivity(intent);

                }
            });
        }

    }
}
