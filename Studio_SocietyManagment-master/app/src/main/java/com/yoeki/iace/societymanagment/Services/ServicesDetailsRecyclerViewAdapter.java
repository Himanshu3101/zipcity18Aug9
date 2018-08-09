package com.yoeki.iace.societymanagment.Services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class ServicesDetailsRecyclerViewAdapter extends RecyclerView.Adapter<ServicesDetailsRecyclerViewAdapter.ViewHolder> {
    private static Context context;
    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ServicesDetailsRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.servicesdetailsrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split(",");

        try {
            if (Break[0].equalsIgnoreCase("null")) {
                holder.Serv_name.setText("");
            } else {
                holder.Serv_name.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.reg_on.setText("");
        }else {
            holder.reg_on.setText(Break[1]);
        }

        if(Break[2].equalsIgnoreCase("null")){
            holder.vndr_number.setText("");
        }else {
            holder.vndr_number.setText(Break[2]);
        }
        if(Break[3].equalsIgnoreCase("null")){
//            holder.vndr_rating.setRating((float );
        }else {
            String rating = Break[3];
            Float f= Float.parseFloat(rating);
            holder.vndr_rating.setRating(f);
        }
        if(Break[4].equalsIgnoreCase("null")){
            holder.vndr_status.setText("");
        }else {
            holder.vndr_status.setText(Break[4]);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Serv_name,reg_on,vndr_number,vndr_status;
        AppCompatRatingBar vndr_rating;
        AppCompatButton service_call;

        ViewHolder(View itemView) {
            super(itemView);
            Serv_name = itemView.findViewById(R.id.Serv_name);
            reg_on = itemView.findViewById(R.id.reg_on);
            vndr_number = itemView.findViewById(R.id.vndr_number);
            service_call = itemView.findViewById(R.id.service_call);
            vndr_rating=itemView.findViewById(R.id.rating);
            vndr_status=itemView.findViewById(R.id.status);

            service_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PhoneNUm = vndr_number.getText().toString();
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
