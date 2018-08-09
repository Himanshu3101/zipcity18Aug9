package com.yoeki.iace.societymanagment.Adapter.RequestAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;
import com.yoeki.iace.societymanagment.Request_Management.Popup_Request;

import java.util.ArrayList;
import java.util.List;

public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    // data is passed into the constructor
    public RequestRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.requestrecycler_old, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String request = mData.get(position);
        Break = request.split(",");

        if(Break[0].equalsIgnoreCase("null")){
            holder.name.setText("");
        }else {
            holder.name.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.Created.setText("");
        }else {
            holder.Created.setText(Break[1]);
        }

        if(Break[2].equalsIgnoreCase("null")){
            holder.Desc.setText("");
        }else {
            holder.Desc.setText(Break[2]);
        }

        if(Break[3].equalsIgnoreCase("null")){
            holder.Unit.setText("");
        }else {
            holder.Unit.setText(Break[3]);
        }

        if(Break[4].equalsIgnoreCase("null")){
            holder.Assigen.setText("");
        }else {
            holder.Assigen.setText(Break[4]);
        }

        if(Break[5].equalsIgnoreCase("null")){
            holder.Status.setText("");
        }else {
            holder.Status.setText(Break[5]);
        }

        if(Break[6].equalsIgnoreCase("null")){
            holder.Type.setText("");
        }else {
            holder.Type.setText(Break[6]);
        }

        if(Break[7].equalsIgnoreCase("null")){
            holder.requestIDD.setText("");
        }else {
            holder.requestIDD.setText(Break[7]);
        }

        if(Break[8].equalsIgnoreCase("null")){
            holder.cancelldIDD.setText("");
        }else {
            holder.cancelldIDD.setText(Break[8]);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView name,Created,Desc,Unit,Assigen,Status,Type,requestIDD,cancelldIDD;
        AppCompatButton btnfrRequestShow;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            Created = itemView.findViewById(R.id.createdOn);
            Desc = itemView.findViewById(R.id.description);
            Unit = itemView.findViewById(R.id.UnitNo);
            Assigen = itemView.findViewById(R.id.assignedTo);
            Status = itemView.findViewById(R.id.status);
            Type = itemView.findViewById(R.id.type);
            requestIDD = itemView.findViewById(R.id.requestIDD);
            cancelldIDD= itemView.findViewById(R.id.cancelldIDD);
            btnfrRequestShow = itemView.findViewById(R.id.btnfrRequestShow);
            btnfrRequestShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> UserRoleID = db.getRoleID();
                    ArrayList<String> finalRoleIDList = new ArrayList<>();
                    for (final String link : UserRoleID) {
                        String log = link;
                        finalRoleIDList.add(log);
                    }
                    if (finalRoleIDList.contains("1") || finalRoleIDList.contains("4")|| finalRoleIDList.contains("3")){
                        String Titl = name.getText().toString();
                        String Create = Created.getText().toString();
                        String Description = Desc.getText().toString();
                        String State = Status.getText().toString();
                        String RequestIDD = requestIDD.getText().toString();
                        String CanecelIDD = cancelldIDD.getText().toString();

                        Intent intent = new Intent(context, Popup_Request.class);
                        intent.putExtra("Title", Titl);
                        intent.putExtra("Created",Create);
                        intent.putExtra("Description", Description);
                        intent.putExtra("Statuss", State);
                        intent.putExtra("RequestID", RequestIDD);
                        intent.putExtra("CancelID", CanecelIDD);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "You are not Authorized Member", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
