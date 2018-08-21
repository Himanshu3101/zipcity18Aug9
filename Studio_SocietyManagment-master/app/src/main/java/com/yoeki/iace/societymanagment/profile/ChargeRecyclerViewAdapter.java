package com.yoeki.iace.societymanagment.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class ChargeRecyclerViewAdapter extends RecyclerView.Adapter<ChargeRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;



    ChargeRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chargerecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("&");

        if(Break[0].equalsIgnoreCase("null")){
            holder.ChargesType.setText("");
        }else {
            holder.ChargesType.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.ChargesAmount.setText("");
        }else {
            holder.ChargesAmount.setText(Break[1]);
        }

    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView ChargesType,ChargesAmount;

        ViewHolder(View itemView) {
            super(itemView);
            ChargesType = itemView.findViewById(R.id.chargedesc);
            ChargesAmount = itemView.findViewById(R.id.amount);

        }
    }
}
