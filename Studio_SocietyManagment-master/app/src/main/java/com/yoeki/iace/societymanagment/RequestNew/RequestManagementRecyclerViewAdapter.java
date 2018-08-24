package com.yoeki.iace.societymanagment.RequestNew;

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


public class RequestManagementRecyclerViewAdapter extends RecyclerView.Adapter<RequestManagementRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;

    // data is passed into the constructor
    public RequestManagementRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.requestrecycler, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("~");

        try {
            if (Break[0].equalsIgnoreCase("null")|| Break[0].equalsIgnoreCase("") || Break[0].equalsIgnoreCase(" ")){
                holder.Req_title.setText("N/A");
            } else {
                holder.Req_title.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
        if(Break[1].equalsIgnoreCase("null")|| Break[1].equalsIgnoreCase("") || Break[1].equalsIgnoreCase(" ")){
            holder.Req_Type.setText("N/A");
        }else {
            holder.Req_Type.setText(Break[1]);
        }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[2].equalsIgnoreCase("null")|| Break[2].equalsIgnoreCase("") || Break[2].equalsIgnoreCase(" ")){
            holder.Req_name.setText("N/A");
        }else {
            holder.Req_name.setText(Break[2]);
        }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[3].equalsIgnoreCase("null")|| Break[3].equalsIgnoreCase("") || Break[3].equalsIgnoreCase(" ")){
            holder.Req_flat.setText("N/A");
        }else {
            holder.Req_flat.setText(Break[3]);
        }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[4].equalsIgnoreCase("null")|| Break[4].equalsIgnoreCase("") || Break[4].equalsIgnoreCase(" ")){
            holder.req_cre_on.setText("N/A");
        }else {
            holder.req_cre_on.setText(Break[4]);
        }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[5].equalsIgnoreCase("null")|| Break[5].equalsIgnoreCase("") || Break[5].equalsIgnoreCase(" ")){
            holder.req_cre_by.setText("N/A");
        }else {
            holder.req_cre_by.setText(Break[5]);
        }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[6].equalsIgnoreCase("null")|| Break[6].equalsIgnoreCase("") || Break[6].equalsIgnoreCase(" ")){
            holder.Req_request.setText("N/A");
        }else {
            holder.Req_request.setText(Break[6]);
        }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[7].equalsIgnoreCase("null")|| Break[7].equalsIgnoreCase("") || Break[7].equalsIgnoreCase(" ")){
                holder.Req_status.setText("N/A");
            } else {
                holder.Req_status.setText(Break[7]);
            }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[8].equalsIgnoreCase("null")|| Break[8].equalsIgnoreCase("") || Break[8].equalsIgnoreCase(" ")){
                holder.Req_fdate.setText("N/A");
            } else {
                holder.Req_fdate.setText(Break[8]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[9].equalsIgnoreCase("null")|| Break[9].equalsIgnoreCase("") || Break[9].equalsIgnoreCase(" ")){
            holder.Req_tdate.setText("N/A");
        }else {
            holder.Req_tdate.setText(Break[9]);
        }
        }catch(Exception e){e.printStackTrace();}


        try{
        if(Break[10].equalsIgnoreCase("null")|| Break[10].equalsIgnoreCase("") || Break[10].equalsIgnoreCase(" ")){
            holder.Req_description.setText("N/A");
        }else {
            String dottedelement=null;
            if(Break[10].length()>35){

                dottedelement  =  Break[10].substring(0,32)+"...";

                holder.Req_description.setText(dottedelement);
            }else{

                holder.Req_description.setText(dottedelement);
                holder.Req_description.setText(Break[10]);
            }

        }
        }catch(Exception e){e.printStackTrace();}

        try{
        if(Break[10].equalsIgnoreCase("null")|| Break[10].equalsIgnoreCase("") || Break[10].equalsIgnoreCase(" ")){
            holder.descripFull.setText("N/A");
        }else {
            holder.descripFull.setText(Break[10]);
        }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[11].equalsIgnoreCase("null")|| Break[11].equalsIgnoreCase("") || Break[11].equalsIgnoreCase(" ")){
                holder.vndr_number.setText("N/A");
            } else {
                holder.vndr_number.setText(Break[11]);
            }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[12].equalsIgnoreCase("null")|| Break[12].equalsIgnoreCase("") || Break[12].equalsIgnoreCase(" ")){
                holder.Req_uniqueCode.setText("N/A");
            } else {
                holder.Req_uniqueCode.setText(Break[12]);
            }
        }catch(Exception e){e.printStackTrace();}


        try {
            if (Break[13].equalsIgnoreCase("null")|| Break[13].equalsIgnoreCase("") || Break[13].equalsIgnoreCase(" ")){
                holder.Req_IID.setText("N/A");
            } else {
                holder.Req_IID.setText(Break[13]);
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
        AppCompatTextView Req_title,Req_Type,Req_name,Req_flat,req_cre_on,req_cre_by,Req_request,Req_status,Req_fdate,Req_tdate
        ,Req_description,descripFull,vndr_number,Req_uniqueCode,Req_IID;
        AppCompatButton req_call,requestpopup;

        ViewHolder(View itemView) {
            super(itemView);
            Req_title = itemView.findViewById(R.id.Req_title);
            Req_Type = itemView.findViewById(R.id.Req_Type);
            Req_name = itemView.findViewById(R.id.Req_name);
            Req_flat = itemView.findViewById(R.id.Req_flat);
            req_cre_on = itemView.findViewById(R.id.req_cre_on);
            req_cre_by = itemView.findViewById(R.id.req_cre_by);
            Req_request = itemView.findViewById(R.id.Req_request);
            Req_status = itemView.findViewById(R.id.Req_status);
            Req_fdate = itemView.findViewById(R.id.Req_fdate);
            Req_tdate = itemView.findViewById(R.id.Req_tdate);
            Req_description = itemView.findViewById(R.id.Req_description);
            descripFull = itemView.findViewById(R.id.descripFull);
            vndr_number = itemView.findViewById(R.id.vndr_number);
            req_call = itemView.findViewById(R.id.req_call);
            requestpopup=itemView.findViewById(R.id.btnfrRequestShow);
            Req_uniqueCode = itemView.findViewById(R.id.Req_uniqueCode);
            Req_IID = itemView.findViewById(R.id.Req_IID);

//            Req_title.setPaintFlags(Req_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            requestpopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String r_title = Req_title.getText().toString();
                    String r_type = Req_Type.getText().toString();
                    String r_name = Req_name.getText().toString();
                    String r_flat = Req_flat.getText().toString();
                    String r_createdon = req_cre_on.getText().toString();
                    String r_createdby = req_cre_by.getText().toString();
                    String r_requestno = Req_request.getText().toString();
                    String r_status = Req_status.getText().toString();
                    String r_fdate = Req_fdate.getText().toString();
                    String r_tdate = Req_tdate.getText().toString();
                    String r_descriptionfull = descripFull.getText().toString();
                    String r_uniqueCo = Req_uniqueCode.getText().toString();
                    String r_reqId = Req_IID.getText().toString();

                    Intent intent=new Intent(context,RequestPopup.class);
                    intent.putExtra("R_title", r_title);
                    intent.putExtra("R_type", r_type);
                    intent.putExtra("R_name", r_name);
                    intent.putExtra("R_flat", r_flat);
                    intent.putExtra("R_createdon", r_createdon);
                    intent.putExtra("R_createdby", r_createdby);
                    intent.putExtra("R_requestno", r_requestno);
                    intent.putExtra("R_status", r_status);
                    intent.putExtra("R_fdate", r_fdate);
                    intent.putExtra("R_tdate", r_tdate);
                    intent.putExtra("R_descrfull", r_descriptionfull);
                    intent.putExtra("R_Unique", r_uniqueCo);
                    intent.putExtra("R_ReqID", r_reqId);
                    context.startActivity(intent);
                }
            });
            req_call.setOnClickListener(new View.OnClickListener() {
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
