package com.yoeki.iace.societymanagment.Recharge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;

public class RechargeRecyclerViewAdapter extends RecyclerView.Adapter<RechargeRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RechargeRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rechargerecycler, parent, false);
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
        TextView name;
        ImageView icondown,iconup;
        RecyclerView recharge;
        LinearLayoutCompat layout1;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.data);
            icondown=itemView.findViewById(R.id.icondown);
            iconup=itemView.findViewById(R.id.iconup);
            recharge = itemView.findViewById(R.id.rechargeamount);
            layout1=itemView.findViewById(R.id.layout1);


            iconup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layout1.setVisibility(View.GONE);
                    icondown.setVisibility(View.VISIBLE);
                    iconup.setVisibility(View.GONE);
                   /* if (iconup.getVisibility()==View.VISIBLE) {
                        layout1.setVisibility(View.GONE);
                    } else {
                        layout1.setVisibility(View.VISIBLE);
                        iconup.setVisibility(View.GONE);
                        icondown.setVisibility(View.VISIBLE);

                        }*/
                }
            });

            icondown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout1.setVisibility(View.VISIBLE);
                    iconup.setVisibility(View.VISIBLE);
                    icondown.setVisibility(View.GONE);
//                    if (layout1.getVisibility() == View.VISIBLE) {
//                        layout1.setVisibility(View.GONE);
//                    } else {
//                        layout1.setVisibility(View.VISIBLE);
//                        icondown.setVisibility(View.GONE);
//                        iconup.setVisibility(View.VISIBLE);
//
//
//                    }

                    RechargeAmountRecyclerViewAdapter amountadapter;
                    ArrayList<String> RechargeAmountList;


                  RechargeAmountList = new ArrayList<>();
                  RechargeAmountList.add("Meter Number");
                  RechargeAmountList.add("Electricity");
                  RechargeAmountList.add("Balance");
                  RechargeAmountList.add("Previous Amount");


                 recharge.setLayoutManager(new LinearLayoutManager(context));
                 amountadapter = new RechargeAmountRecyclerViewAdapter(context, RechargeAmountList);
                 recharge.setAdapter(amountadapter);
                   }
            });
        }
    }
}
