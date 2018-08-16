package com.yoeki.iace.societymanagment.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yoeki.iace.societymanagment.Circular.Circular_Popup;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

public class AllotedUnitRecyclerViewAdapter extends RecyclerView.Adapter<AllotedUnitRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    AllotedUnitRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.allotedunitrecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("&");

        if(Break[0].equalsIgnoreCase("null")){
            holder.Unit_no.setText("");
        }else {
            holder.Unit_no.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.Unit_Status.setText("");
        }else {
            holder.Unit_Status.setText(Break[1]);
        }

            }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView Unit_no,Unit_Status;

        ViewHolder(View itemView) {
            super(itemView);
            Unit_no = itemView.findViewById(R.id.unitno);
            Unit_Status = itemView.findViewById(R.id.u_status);

        }
    }
}
