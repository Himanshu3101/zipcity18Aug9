package com.yoeki.iace.societymanagment.Directory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoeki.iace.societymanagment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IACE on 25-Aug-18.
 */

public class profess_detail_recycler extends RecyclerView.Adapter<profess_detail_recycler.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;

    public profess_detail_recycler(Context context, ArrayList<String> candidateDetailsList) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = candidateDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public profess_detail_recycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.profess_details_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profess_detail_recycler.ViewHolder holder, int position) {
        String Member_details = mData.get(position);
        String[] Break = Member_details.split("\\$");

        try {
            if (Break[0].equalsIgnoreCase("null")|| Break[0].equalsIgnoreCase("") || Break[0].equalsIgnoreCase(" ")){
                holder.prof_title.setText("N/A");
            } else {
                holder.prof_title.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(Break[1].equalsIgnoreCase("null")|| Break[1].equalsIgnoreCase("") || Break[1].equalsIgnoreCase(" ")){
                holder.proff_name.setText("N/A");
            }else {
                holder.proff_name.setText(Break[1]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[2].equalsIgnoreCase("null")|| Break[2].equalsIgnoreCase("") || Break[2].equalsIgnoreCase(" ")){
                holder.proff_locat.setText("N/A");
            }else {
                holder.proff_locat.setText(Break[2]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[3].equalsIgnoreCase("null")|| Break[3].equalsIgnoreCase("") || Break[3].equalsIgnoreCase(" ")){
                holder.Member_number.setText("N/A");
            }else {
                holder.Member_number.setText(Break[3]);
            }
        }catch(Exception e){e.printStackTrace();}

    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView prof_title,proff_name,proff_locat,Member_number;
        AppCompatButton call_by_profession;

        ViewHolder(View itemView) {
            super(itemView);
            prof_title = itemView.findViewById(R.id.prof_title);
            proff_name = itemView.findViewById(R.id.proff_name);
            proff_locat = itemView.findViewById(R.id.proff_locat);
            Member_number = itemView.findViewById(R.id.Member_number);
            call_by_profession = itemView.findViewById(R.id.call_by_profession);

            call_by_profession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PhoneNUm = Member_number.getText().toString();
                    try {
                        if (PhoneNUm.equals("") || PhoneNUm.equals(null)) {
                            Toast.makeText(context, "Phone number does not exists.", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", PhoneNUm, null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
