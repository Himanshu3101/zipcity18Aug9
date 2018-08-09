package com.yoeki.iace.societymanagment.Adapter.SocietyAdapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoeki.iace.societymanagment.R;
import com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info.Lift_Info;

import java.util.List;

public class GeneralLiftRecyclerAdapter extends RecyclerView.Adapter<GeneralLiftRecyclerAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public GeneralLiftRecyclerAdapter(Lift_Info context, List<String> data) {
        this.mInflater = LayoutInflater.from((Context) context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.liftrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String society = mData.get(position);
        String[] Liftdata = society.split(",");
        holder.LiftNo.setText(Liftdata[0]);
//        holder.Lift_Name.setText(Liftdata[1]);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView LiftNo,Lift_Name;

        ViewHolder(View itemView) {
            super(itemView);
            LiftNo = itemView.findViewById(R.id.liftno);
//            Lift_Name = itemView.findViewById(R.id.liftype);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
