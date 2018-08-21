package com.yoeki.iace.societymanagment.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;

public class AllotedUnitRecyclerViewAdapter extends RecyclerView.Adapter<AllotedUnitRecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private ArrayList<String> chargeslist;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;
    ArrayList<String> ChargesList;
    ChargeRecyclerViewAdapter amountadapter;
//    String ChargesList;


    // data is passed into the constructor
    AllotedUnitRecyclerViewAdapter(Context context, ArrayList<String> unitList, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.chargeslist = unitList;
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

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Unit_no,Unit_Status;
        Button icondown,iconup;
        RecyclerView charge;
        LinearLayoutCompat unitlayout;
        FrameLayout iconupframe,icondownframe;

        ViewHolder(View itemView) {
            super(itemView);

            Unit_no = itemView.findViewById(R.id.flatno);
            Unit_Status=itemView.findViewById(R.id.by);
            icondown=itemView.findViewById(R.id.icondown);
            iconup=itemView.findViewById(R.id.iconup);
            charge = itemView.findViewById(R.id.chargesflatno);
            unitlayout=itemView.findViewById(R.id.unitlayout);
            iconupframe=itemView.findViewById(R.id.iconupframe);
            icondownframe=itemView.findViewById(R.id.icondownframe);


            iconup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    unitlayout.setVisibility(View.GONE);
                    icondownframe.setVisibility(View.VISIBLE);
                    iconupframe.setVisibility(View.GONE);
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
                    unitlayout.setVisibility(View.VISIBLE);
                    iconupframe.setVisibility(View.VISIBLE);
                    icondownframe.setVisibility(View.GONE);
//                    if (layout1.getVisibility() == View.VISIBLE) {
//                        layout1.setVisibility(View.GONE);
//                    } else {
//                        layout1.setVisibility(View.VISIBLE);
//                        icondown.setVisibility(View.GONE);
//                        iconup.setVisibility(View.VISIBLE);
//
//
//                    }

//                    ChargesList = new ArrayList<String>();
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//                    String  chargedetail = prefs.getString("chargesList"," ");
//                    ChargesList.add(chargedetail);




                    charge.setLayoutManager(new LinearLayoutManager(context));
                    amountadapter = new ChargeRecyclerViewAdapter(context, chargeslist);
                    charge.setAdapter(amountadapter);
                }
            });
        }
    }
}
