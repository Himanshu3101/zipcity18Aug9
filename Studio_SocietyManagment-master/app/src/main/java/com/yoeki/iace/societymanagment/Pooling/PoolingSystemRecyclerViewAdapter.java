package com.yoeki.iace.societymanagment.Pooling;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class PoolingSystemRecyclerViewAdapter extends RecyclerView.Adapter<PoolingSystemRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    PoolingSystemRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.poolingsystemrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String request = mData.get(position);
        holder.title.setText(request);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        AppCompatButton P_popup;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.p_title);
            P_popup=itemView.findViewById(R.id.P_popup);

            P_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String poll_title = title.getText().toString();
                    Intent intent=new Intent(context,CandidateVote.class);
                    intent.putExtra("poll_tit",poll_title);
                    context.startActivity(intent);
                }
            });



        }
    }
}
