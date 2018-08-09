package com.yoeki.iace.societymanagment.Circular;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class CircularRecyclerViewAdapter extends RecyclerView.Adapter<CircularRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    CircularRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.circularrecyclerlist, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("&");

        if(Break[0].equalsIgnoreCase("null")){
            holder.C_title.setText("");
        }else {
            holder.C_title.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.C_fdate.setText("");
        }else {
            holder.C_fdate.setText(Break[1]);
        }

        if(Break[2].equalsIgnoreCase("null")){
            holder.C_tdate.setText("");
        }else {
            holder.C_tdate.setText(Break[2]);
        }

        if(Break[3].equalsIgnoreCase("null")){
            holder.C_description.setText("");
        }else {
            String dottedelement=null;
            if(Break[3].length()>35){

                dottedelement  =  Break[3].substring(0,32)+"...";

                holder.C_description.setText(dottedelement);
            }else{

                holder.C_description.setText(dottedelement);
                holder.C_description.setText(Break[3]);
            }

        }


        if(Break[3].equalsIgnoreCase("null")){
            holder.C_descriptionfull.setText("");
        }else {
            holder.C_descriptionfull.setText(Break[3]);
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView C_title,C_fdate,C_tdate,C_description,C_descriptionfull;
        AppCompatButton btnCircularpopup;

        ViewHolder(View itemView) {
            super(itemView);
            C_title = itemView.findViewById(R.id.c_title);
            C_fdate = itemView.findViewById(R.id.c_fdate);
            C_tdate = itemView.findViewById(R.id.c_tdate);
            C_description = itemView.findViewById(R.id.c_descrip);
            C_descriptionfull = itemView.findViewById(R.id.C_descriptionfull);
            btnCircularpopup = itemView.findViewById(R.id.c_dialogbutton);
            btnCircularpopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ttitle = C_title.getText().toString();
                    String ffdate = C_fdate.getText().toString();
                    String ttdate = C_tdate.getText().toString();
                    String ddescription = C_descriptionfull.getText().toString();

                    Intent intent= new Intent(context,Circular_Popup.class);
                    intent.putExtra("C_title", ttitle);
                    intent.putExtra("C_Fromdate", ffdate);
                    intent.putExtra("C_TODATE", ttdate);
                    intent.putExtra("C_Descriptionn", ddescription);
                    context.startActivity(intent);
                }
            });
        }
    }
}
