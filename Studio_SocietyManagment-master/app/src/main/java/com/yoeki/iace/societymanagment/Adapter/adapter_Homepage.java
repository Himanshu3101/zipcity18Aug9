package com.yoeki.iace.societymanagment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoeki.iace.societymanagment.Complaint_management.Complaint_management;
import com.yoeki.iace.societymanagment.R;
import com.yoeki.iace.societymanagment.Request_Management.Request_management;
import com.yoeki.iace.societymanagment.Society_Information.OLD_Coding.Flat_Info.Society_dashboard;
import com.yoeki.iace.societymanagment.VendorApproval.VendorApproval;
import com.yoeki.iace.societymanagment.VendorList.VendorList;
import com.yoeki.iace.societymanagment.profile.Profile;

import java.util.List;

/**
 * Created by IACE on 16-Jul-18.
 */

public class adapter_Homepage extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static Context context;
    private LayoutInflater layoutInflater;
    static List<String> home_data;

    public adapter_Homepage(Context context, List<String> homePageList) {
        layoutInflater = LayoutInflater.from(context);
        this.home_data = homePageList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.recycler_layout, parent, false);
        adapter_Homepage.Holder holder = new adapter_Homepage.Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final adapter_Homepage.Holder holder1 = (adapter_Homepage.Holder) holder;
        String name1 = home_data.get(position);
        holder1.menu_name.setText((CharSequence) name1);
    }

    @Override
    public int getItemCount() {
        return home_data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        AppCompatTextView menu_name;
        AppCompatButton selection;
        public Holder(final View itemView) {
            super(itemView);
            menu_name = (AppCompatTextView) itemView.findViewById(R.id.name);
            selection = (AppCompatButton) itemView.findViewById(R.id.selection);
            selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = menu_name.getText().toString();
                    if(name.equalsIgnoreCase("Dashboard")){

                    }else if(name.equalsIgnoreCase("Society Information")){
                        Intent intent = new Intent(context, Society_dashboard.class);
                        context.startActivity(intent);
                    }else if(name.equalsIgnoreCase("Request Management")){
                        Intent intent = new Intent(context, Request_management.class);
                        context.startActivity(intent);
                    }else if(name.equalsIgnoreCase("Complaint Management")){
                        Intent intent = new Intent(context, Complaint_management.class);
                        context.startActivity(intent);
                    }/*else if(name.equalsIgnoreCase("Visitors Management")){
                        Intent intent = new Intent(context, VisitorsManagement.class);
                        context.startActivity(intent);
                    }*/else if(name.equalsIgnoreCase("Profile")){
                        Intent intent = new Intent(context, Profile.class);
                        context.startActivity(intent);
                    }else if(name.equalsIgnoreCase("Vendor List")){
                        Intent intent = new Intent(context, VendorList.class);
                        context.startActivity(intent);
                    }else if(name.equalsIgnoreCase("Vendor Approval")){
                        Intent intent = new Intent(context, VendorApproval.class);
                        context.startActivity(intent);
                    }else if(name.equalsIgnoreCase("Regular Passes")){
//                        Intent intent = new Intent(context, VendorApproval.class);
//                        context.startActivity(intent);
                    }else if(name.equalsIgnoreCase("Tenant")){

                    }
                }
            });
        }
    }
}
