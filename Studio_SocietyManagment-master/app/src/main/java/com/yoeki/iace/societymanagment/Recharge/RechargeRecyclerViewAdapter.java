package com.yoeki.iace.societymanagment.Recharge;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        Button icondown,iconup;
        AppCompatButton rchrge_proceed;
        FrameLayout up,down;
        RecyclerView recharge;
        LinearLayoutCompat layout1;
        View view_recharge;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.data);
            icondown=itemView.findViewById(R.id.icondown);
            iconup=itemView.findViewById(R.id.iconup);
            view_recharge=itemView.findViewById(R.id.view_recharge);
            recharge = itemView.findViewById(R.id.rechargeamount);
            layout1=itemView.findViewById(R.id.layout1);
            rchrge_proceed = itemView.findViewById(R.id.rchrge_proceed);
            up =itemView.findViewById(R.id.up);
            down =itemView.findViewById(R.id.down);

            rchrge_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Recharge Proceed", Toast.LENGTH_SHORT).show();
                }
            });

            iconup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layout1.setVisibility(View.GONE);
                    down.setVisibility(View.VISIBLE);
                    up.setVisibility(View.GONE);
                    view_recharge.setVisibility(View.VISIBLE);
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
                    up.setVisibility(View.VISIBLE);
                    down.setVisibility(View.GONE);
                    view_recharge.setVisibility(View.GONE);
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
                    RechargeAmountList.add("Last Recharge Amount");
                    RechargeAmountList.add("Previous Recharge Amount");
                    RechargeAmountList.add("Balance");


                    recharge.setLayoutManager(new LinearLayoutManager(context));
                    amountadapter = new RechargeAmountRecyclerViewAdapter(context, RechargeAmountList);
                    recharge.setAdapter(amountadapter);
                }
            });
        }
    }
}
