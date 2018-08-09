package com.yoeki.iace.societymanagment.Adapter.VendorListAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class VendorListRecyclerViewAdapter extends RecyclerView.Adapter<VendorListRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    public VendorListRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.vendorlistrecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String vendorlist = mData.get(position);
        Break = vendorlist.split(",");

        if(Break[0].equalsIgnoreCase("null")){
            holder.Vendorlist_name.setText("");
        }else {
            holder.Vendorlist_name.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.Vendorlist_contact.setText("");
        }else {
            holder.Vendorlist_contact.setText(Break[1]);
        }

        if(Break[2].equalsIgnoreCase("null")){
            holder.Vendorlist_verifi_type.setText("");
        }else {
            holder.Vendorlist_verifi_type.setText(Break[2]);
        }

        if(Break[3].equalsIgnoreCase("null")){
            holder.Vendorlist_verifi_no.setText("");
        }else {
            holder.Vendorlist_verifi_no.setText(Break[3]);
        }

        if(Break[4].equalsIgnoreCase("null")){
            holder.Vendorlist_verifi_by.setText("");
        }else {
            holder.Vendorlist_verifi_by.setText(Break[4]);
        }

        if(Break[5].equalsIgnoreCase("null")){
            holder.Vendorlist_verifi_on.setText("");
        }else {
            holder.Vendorlist_verifi_on.setText(Break[5]);
        }

//        if(Break[6].equalsIgnoreCase("null")){
//            holder.Vendorlist_fromdate.setText("");
//        }else {
//            holder.Vendorlist_fromdate.setText(Break[6]);
//        }
//
//        if(Break[7].equalsIgnoreCase("null")){
//            holder.Vendorlist_todate.setText("");
//        }else {
//            holder.Vendorlist_todate.setText(Break[7]);
//        }

        if(Break[6].equalsIgnoreCase("null")){
            holder.Vendorlist_servicetype.setText("");
        }else {
            holder.Vendorlist_servicetype.setText(Break[6]);
        }
//        if(Break[9].equalsIgnoreCase("null")){
//            holder.Vendorlist_type.setText("");
//        }else {
//            holder.Vendorlist_type.setText(Break[9]);
//        }
//        if(Break[10].equalsIgnoreCase("null")){
//            holder.Vendorlist_typevalue.setText("");
//        }else {
//            holder.Vendorlist_typevalue.setText(Break[10]);
//        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Vendorlist_name,Vendorlist_contact,Vendorlist_verifi_type,Vendorlist_verifi_no,Vendorlist_verifi_by,
                Vendorlist_verifi_on,Vendorlist_fromdate,Vendorlist_todate,Vendorlist_servicetype,Vendorlist_type,Vendorlist_typevalue;


        ViewHolder(View itemView) {
            super(itemView);
            Vendorlist_name = itemView.findViewById(R.id.vendor_name);
            Vendorlist_contact = itemView.findViewById(R.id.vendor_contact);
            Vendorlist_verifi_type = itemView.findViewById(R.id.verification_type);
            Vendorlist_verifi_no = itemView.findViewById(R.id.verification_no);
            Vendorlist_verifi_by = itemView.findViewById(R.id.verification_by);
            Vendorlist_verifi_on = itemView.findViewById(R.id.verification_on);
//            Vendorlist_fromdate = itemView.findViewById(R.id.from_date);
//            Vendorlist_todate = itemView.findViewById(R.id.to_date);
            Vendorlist_servicetype = itemView.findViewById(R.id.service_type);
//            Vendorlist_type = itemView.findViewById(R.id.vendorlist_type);
//             Vendorlist_typevalue = itemView.findViewById(R.id.vendorlist_typevalue);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
