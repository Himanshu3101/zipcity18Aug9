package com.yoeki.iace.societymanagment.societymanagement;

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

public class ComplaintManagementRecyclerViewAdapter extends RecyclerView.Adapter<ComplaintManagementRecyclerViewAdapter.ViewHolder> {
    private static Context context;
    private List<String> mData;
    private LayoutInflater mInflater;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ComplaintManagementRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.complaintrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("~");

        try {
            if (Break[0].equalsIgnoreCase("null")|| Break[0].equalsIgnoreCase("") || Break[0].equalsIgnoreCase(" ")){
                holder.com_title.setText("N/A");
            } else {
                holder.com_title.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(Break[1].equalsIgnoreCase("null")|| Break[1].equalsIgnoreCase("") || Break[1].equalsIgnoreCase(" ")){
                holder.com_cre_on.setText("N/A");
            }else {
                holder.com_cre_on.setText(Break[1]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[2].equalsIgnoreCase("null")|| Break[2].equalsIgnoreCase("") || Break[2].equalsIgnoreCase(" ")){
                holder.com_cre_by.setText("N/A");
            }else {
                holder.com_cre_by.setText(Break[2]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[3].equalsIgnoreCase("null")|| Break[3].equalsIgnoreCase("") || Break[3].equalsIgnoreCase(" ")){
                holder.com_unit.setText("N/A");
            }else {
                holder.com_unit.setText(Break[3]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[4].equalsIgnoreCase("null")|| Break[4].equalsIgnoreCase("") || Break[4].equalsIgnoreCase(" ")){
                holder.com_complaintno.setText("N/A");
            }else {
                holder.com_complaintno.setText(Break[4]);
            }
        }catch(Exception e){e.printStackTrace();}


        try{
            if(Break[5].equalsIgnoreCase("null")|| Break[5].equalsIgnoreCase("") || Break[5].equalsIgnoreCase(" ")){
                holder.com_description.setText("N/A");
            }else {
                String dottedelement=null;
                if(Break[5].length()>35){

                    dottedelement  =  Break[5].substring(0,32)+"...";

                    holder.com_description.setText(dottedelement);
                }else{

                    holder.com_description.setText(dottedelement);
                    holder.com_description.setText(Break[5]);
                }

            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[5].equalsIgnoreCase("null")|| Break[5].equalsIgnoreCase("") || Break[5].equalsIgnoreCase(" ")){
                holder.com_description_full.setText("N/A");
            }else {
                holder.com_description_full.setText(Break[5]);
            }
        }catch(Exception e){e.printStackTrace();}


        try {
            if (Break[6].equalsIgnoreCase("null")|| Break[6].equalsIgnoreCase("") || Break[6].equalsIgnoreCase(" ")){
                holder.com_status.setText("N/A");
            } else {
                holder.com_status.setText(Break[6]);
            }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[7].equalsIgnoreCase("null") || Break[7].equalsIgnoreCase("")) {
                holder.com_accepted.setText("N/A");
            } else {
                holder.com_accepted.setText(Break[7]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[8].equalsIgnoreCase("null")|| Break[8].equalsIgnoreCase("") || Break[8].equalsIgnoreCase(" ")){
                holder.com_type.setText("N/A");
            }else {
                holder.com_type.setText(Break[8]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[9].equalsIgnoreCase("null")|| Break[9].equalsIgnoreCase("")){
                holder.com_UniqCode.setText("N/A");
            }else {
                holder.com_UniqCode.setText(Break[9]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[10].equalsIgnoreCase("null")|| Break[10].equalsIgnoreCase("")){
                holder.com_ComplaintID.setText("N/A");
            }else {
                holder.com_ComplaintID.setText(Break[10]);
            }
        }catch(Exception e){e.printStackTrace();}
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView com_title,com_cre_on,com_cre_by,com_unit,com_complaintno,com_description,com_description_full,
                com_status,com_accepted,com_type,com_UniqCode,com_ComplaintID;
        AppCompatButton btnfrComplaintShow;

        public ViewHolder(View itemView) {
            super(itemView);
            com_title = itemView.findViewById(R.id.com_title);
            com_cre_on = itemView.findViewById(R.id.com_cre_on);
            com_cre_by = itemView.findViewById(R.id.com_cre_by);
            com_unit = itemView.findViewById(R.id.com_unit);
            com_complaintno = itemView.findViewById(R.id.com_complaintno);
            com_description = itemView.findViewById(R.id.com_description);
            com_description_full = itemView.findViewById(R.id.com_description_full);
            com_status = itemView.findViewById(R.id.com_status);
            com_accepted = itemView.findViewById(R.id.com_accepted);
            com_type = itemView.findViewById(R.id.com_type);
            com_UniqCode = itemView.findViewById(R.id.com_UniqCode);
            com_ComplaintID = itemView.findViewById(R.id.com_ComplaintID);

            btnfrComplaintShow = itemView.findViewById(R.id.btnfrComplaintShow);
            btnfrComplaintShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Scom_title,Scom_cre_on,Scom_cre_by,Scom_unit,Scom_complaintno,Scom_description_full,
                            Scom_status,Scom_accepted,Scom_type,Uniqu_code,Compl_ID;
                    Scom_title = com_title.getText().toString();
                    Scom_cre_on = com_cre_on.getText().toString();
                    Scom_cre_by = com_cre_by.getText().toString();
                    Scom_unit = com_unit.getText().toString();
                    Scom_complaintno = com_complaintno.getText().toString();
                    Scom_description_full = com_description_full.getText().toString();
                    Scom_status = com_status.getText().toString();
                    Scom_accepted = com_accepted.getText().toString();
                    Scom_type = com_type.getText().toString();
                    Uniqu_code = com_UniqCode.getText().toString();
                    Compl_ID = com_ComplaintID.getText().toString();

                    Intent intent = new Intent(context,ComplaintPopup.class);
                    intent.putExtra("Title",Scom_title);
                    intent.putExtra("Cre_On",Scom_cre_on);
                    intent.putExtra("Cre_By",Scom_cre_by);
                    intent.putExtra("Unit",Scom_unit);
                    intent.putExtra("Complt No",Scom_complaintno);
                    intent.putExtra("Description",Scom_description_full);
                    intent.putExtra("Status",Scom_status);
                    intent.putExtra("vndr_nme",Scom_accepted);
                    intent.putExtra("type",Scom_type);
                    intent.putExtra("unique",Uniqu_code);
                    intent.putExtra("ComplaintID",Compl_ID);
                    context.startActivity(intent);

                }
            });
        }
    }
}

