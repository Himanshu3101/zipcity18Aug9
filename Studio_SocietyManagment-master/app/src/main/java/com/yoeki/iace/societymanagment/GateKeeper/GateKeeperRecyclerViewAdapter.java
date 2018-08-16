package com.yoeki.iace.societymanagment.GateKeeper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class GateKeeperRecyclerViewAdapter extends RecyclerView.Adapter<GateKeeperRecyclerViewAdapter.ViewHolder> {

    private static Context context;
    private List<String> mData;
    private LayoutInflater mInflater;
    //    private ItemClickListener mClickListener;

    GateKeeperRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gatekeeperrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("~");

        try {
            if (Break[0].equalsIgnoreCase("null") || Break[0].equalsIgnoreCase("") || Break[0].equalsIgnoreCase(" ")) {
                holder.Visit_name.setText("N/A");
            } else {
                holder.Visit_name.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(Break[1].equalsIgnoreCase("null")  || Break[1].equalsIgnoreCase("") || Break[1].equalsIgnoreCase(" ")) {
            holder.Visit_address.setText("N/A");
        }else {
            holder.Visit_address.setText(Break[1]);
        }

        if(Break[2].equalsIgnoreCase("null") || Break[2].equalsIgnoreCase("") || Break[2].equalsIgnoreCase(" ")){
            holder.Visit_fromDteTme.setText("N/A");
        }else {
            holder.Visit_fromDteTme.setText(Break[2]);
        }

        if(Break[3].equalsIgnoreCase("null")|| Break[3].equalsIgnoreCase("") || Break[3].equalsIgnoreCase(" ")){
            holder.Visit_toDteTme.setText("N/A");
        }else {
            holder.Visit_toDteTme.setText(Break[3]);
        }

        if(Break[4].equalsIgnoreCase("null")|| Break[4].equalsIgnoreCase("") || Break[4].equalsIgnoreCase(" ")){
            holder.Visit_status.setText("N/A");
        }else {
            holder.Visit_status.setText(Break[4]);
        }

        if(Break[5].equalsIgnoreCase("null")|| Break[5].equalsIgnoreCase("") || Break[5].equalsIgnoreCase(" ")){
            holder.Visit_number.setText("N/A");
        }else {
            holder.Visit_number.setText(Break[5]);
        }

        }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Visit_name,Visit_address,Visit_fromDteTme,Visit_toDteTme,Visit_status,Visit_number;
        AppCompatButton visit_calling;


        ViewHolder(View itemView) {
            super(itemView);
            Visit_name = itemView.findViewById(R.id.Visit_name);
            Visit_address = itemView.findViewById(R.id.Visit_address);
            Visit_fromDteTme = itemView.findViewById(R.id.Visit_fromDteTme);
            Visit_toDteTme = itemView.findViewById(R.id.Visit_toDteTme);
            Visit_status = itemView.findViewById(R.id.Visit_status);
            Visit_number = itemView.findViewById(R.id.Visit_number);

            visit_calling = itemView.findViewById(R.id.visit_calling);
            visit_calling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PhoneNUm = Visit_number.getText().toString();
                    try {
                        if (PhoneNUm.equals("") || PhoneNUm.equals(null)) {
                            Toast.makeText(context, "Phone number does not exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", PhoneNUm, null));
                            context.startActivity(intent);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}
