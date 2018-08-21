package com.yoeki.iace.societymanagment.Service_Provider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class SrvcPrvdeProfileRecyclerViewAdapter extends RecyclerView.Adapter<SrvcPrvdeProfileRecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    // data is passed into the constructor
    SrvcPrvdeProfileRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.srvcprvderprofilerecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);

        String[]  array = unit.split(",");
        for(int i=0;i<array.length;i++)
        {
//            System.out.println(array[i]);
            holder.P_Servicetype.setText(array[i]);
        }
//

//        if(Break[0].equalsIgnoreCase("null")){
//            holder.P_Servicetype.setText("");
//        }else {
//            holder.P_Servicetype.setText(Break[0]);
//        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView P_Servicetype;

        ViewHolder(View itemView) {
            super(itemView);
            P_Servicetype = itemView.findViewById(R.id.servicetype);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
