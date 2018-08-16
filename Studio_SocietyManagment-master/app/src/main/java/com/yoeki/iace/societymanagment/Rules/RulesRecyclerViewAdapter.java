package com.yoeki.iace.societymanagment.Rules;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoeki.iace.societymanagment.Circular.Circular_Popup;
import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    RulesRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rulesrecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("&");

        if(Break[0].equalsIgnoreCase("null")){
            holder.R_title.setText("");
        }else {
            holder.R_title.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")){
            holder.R_description.setText("");
        }else {
            String dottedelement=null;
            if(Break[1].length()>35){

                dottedelement  =  Break[1].substring(0,32)+"...";

                holder.R_description.setText(dottedelement);
            }else{

                holder.R_description.setText(dottedelement);
                holder.R_description.setText(Break[1]);
            }

        }
        if(Break[1].equalsIgnoreCase("null")){
            holder.R_descriptionfull.setText("");
        }else {
            holder.R_descriptionfull.setText(Break[1]);
        }

    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   {
        TextView R_title,R_description,R_descriptionfull;
        AppCompatButton btnRulespopup;


        ViewHolder(View itemView) {
            super(itemView);
            R_title = itemView.findViewById(R.id.r_title);
            R_description = itemView.findViewById(R.id.r_desc);
            R_descriptionfull = itemView.findViewById(R.id.R_descriptionfull);
            btnRulespopup = itemView.findViewById(R.id.r_dialogbutton);
            btnRulespopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ttitle = R_title.getText().toString();
                    String ddescriptionfull = R_descriptionfull.getText().toString();

                    Intent intent= new Intent(context,RulesPopup.class);
                    intent.putExtra("R_title", ttitle);
                    intent.putExtra("R_Descriptionn", ddescriptionfull);
                    context.startActivity(intent);
                }
            });

        }
    }
}
