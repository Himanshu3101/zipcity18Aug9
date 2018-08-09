package com.yoeki.iace.societymanagment.Adapter.VendorApprovalAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class VendorApprovalRecyclerViewAdapter extends RecyclerView.Adapter<VendorApprovalRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    public VendorApprovalRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.vendorapprovalrecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String vendorApprovallist = mData.get(position);
        Break = vendorApprovallist.split("&");

      try{
          if(Break[0].equalsIgnoreCase("null")){
              holder.VendorApproval_name.setText("");
          }else {
              holder.VendorApproval_name.setText(Break[0]);
          }

          if(Break[1].equalsIgnoreCase("null")){
              holder.VendorApproval_contact.setText("");
          }else {
              holder.VendorApproval_contact.setText(Break[1]);
          }

          if(Break[2].equalsIgnoreCase("null")){
              holder.VendorApproval_verifi_type.setText("");
          }else {
              holder.VendorApproval_verifi_type.setText(Break[2]);
          }

          if(Break[3].equalsIgnoreCase("null")){
              holder.VendorApproval_verifi_no.setText("");
          }else {
              holder.VendorApproval_verifi_no.setText(Break[3]);
          }
          if(Break[4].equalsIgnoreCase("null")){
              holder.VendorApproval_fromdate.setText("");
          }else {
              holder.VendorApproval_fromdate.setText(Break[4]);
          }

          if(Break[5].equalsIgnoreCase("null")){
              holder.VendorApproval_todate.setText("");
          }else {
              holder.VendorApproval_todate.setText(Break[5]);
          }

          if(Break[6].equalsIgnoreCase("null")){
              holder.VendorApproval_servicetype.setText("");
          }else {
              holder.VendorApproval_servicetype.setText(Break[6]);
          }
          if(Break[7].equalsIgnoreCase("null")){
              holder.VendorApproval_type.setText("");
          }else {
              holder.VendorApproval_type.setText(Break[7]);
          }
          if(Break[8].equalsIgnoreCase("null")){
              holder.VendorApproval_typevalue.setText("");
          }else {
              holder.VendorApproval_typevalue.setText(Break[8]);
          }

      }catch (Exception e){
          e.printStackTrace();
      }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView VendorApproval_name,VendorApproval_contact,VendorApproval_verifi_type,VendorApproval_verifi_no,
                VendorApproval_fromdate,VendorApproval_todate,VendorApproval_servicetype,VendorApproval_type,VendorApproval_typevalue;


        ViewHolder(View itemView) {
            super(itemView);
            VendorApproval_name = itemView.findViewById(R.id.vendor_name);
            VendorApproval_contact = itemView.findViewById(R.id.vendor_contact);
            VendorApproval_verifi_type = itemView.findViewById(R.id.verification_type);
            VendorApproval_verifi_no = itemView.findViewById(R.id.verification_no);
            VendorApproval_fromdate = itemView.findViewById(R.id.from_date);
            VendorApproval_todate = itemView.findViewById(R.id.to_date);
            VendorApproval_servicetype = itemView.findViewById(R.id.service_type);
            VendorApproval_type = itemView.findViewById(R.id.Approval_type);
            VendorApproval_typevalue = itemView.findViewById(R.id.Approval_typevalue);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
