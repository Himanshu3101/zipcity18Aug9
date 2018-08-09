package com.yoeki.iace.societymanagment.Rules;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    RulesRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rulesrecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("&");

        if(Break[0].equalsIgnoreCase("null")){
            holder.R_title.setText("");
        }else {
            holder.R_title.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.R_description.setText("");
        }else {
            holder.R_description.setText(Break[1]);
        }


    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView R_title,R_description;


        ViewHolder(View itemView) {
            super(itemView);
            R_title = itemView.findViewById(R.id.r_title);
            R_description = itemView.findViewById(R.id.r_desc);

        }
    }
}
