package com.yoeki.iace.societymanagment.Notification;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    NotificationRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notificationrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit,tit = null ,desc = null;
        unit = mData.get(position);
        String[] Break = unit.split("~");

        holder.Noti_title.setText(Break[0]);
        holder.Noti_Desc.setText(Break[1]);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView Noti_title,dateNotiTime,Noti_Desc;

        ViewHolder(View itemView) {
            super(itemView);
            Noti_title = itemView.findViewById(R.id.Noti_title);
            dateNotiTime = itemView.findViewById(R.id.dateNotiTime);
            Noti_Desc = itemView.findViewById(R.id.Noti_Desc);
        }
    }
}
