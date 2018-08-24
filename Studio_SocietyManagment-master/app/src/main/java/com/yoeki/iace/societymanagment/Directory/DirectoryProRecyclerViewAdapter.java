package com.yoeki.iace.societymanagment.Directory;

/**
 * Created by IACE on 23-Aug-18.
 */
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class DirectoryProRecyclerViewAdapter extends RecyclerView.Adapter<DirectoryProRecyclerViewAdapter.ViewHolder> {
    private static Context context;
    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    DirectoryProRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.professionrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String firstLetter = unit.substring(0,1); //takes first letter
        String first = firstLetter.toUpperCase();
        holder.icon.setText(first);
        holder.Profession_FullName.setText(unit);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Profession_FullName,icon;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            Profession_FullName = itemView.findViewById(R.id.profession_FullName);

        }

    }
}
