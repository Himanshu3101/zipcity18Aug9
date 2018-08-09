package com.yoeki.iace.societymanagment.Services;

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

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.ViewHolder> {
    private static Context context;
    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ServicesRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.servicesrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String firstLetter = unit.substring(0,1); //takes first letter
        String first = firstLetter.toUpperCase();
        holder.icon.setText(first);
        holder.Serv_FullName.setText(unit);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Serv_FullName,icon;
        AppCompatButton service_btn;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            Serv_FullName = itemView.findViewById(R.id.Serv_FullName);
            service_btn = itemView.findViewById(R.id.service_btn);

            service_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String serviceName = Serv_FullName.getText().toString();
                    Intent intent = new Intent(context, ServiceDetails.class);
                    intent.putExtra("service_select",serviceName);
                    context.startActivity(intent);
                }
            });
        }

    }
}
