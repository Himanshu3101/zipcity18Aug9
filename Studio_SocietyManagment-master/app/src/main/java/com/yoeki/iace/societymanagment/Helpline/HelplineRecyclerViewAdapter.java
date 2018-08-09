package com.yoeki.iace.societymanagment.Helpline;

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

import com.yoeki.iace.societymanagment.Database.DBHandler;
import com.yoeki.iace.societymanagment.R;

import java.util.List;

public class HelplineRecyclerViewAdapter extends RecyclerView.Adapter<HelplineRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    DBHandler db;
    String[] Break;

    HelplineRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.helplinerecycler, parent, false);
        db = new DBHandler(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split(",");

        try{
            if (Break[0].equalsIgnoreCase("null")) {
                holder.Helpline_Name.setText("");
            } else {
                holder.Helpline_Name.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

      try{
          if (Break[1].equalsIgnoreCase("null")) {
              holder.Helpline_number.setText("");
          } else {
              holder.Helpline_number.setText(Break[1]);
          }
      }catch (Exception e){
             e.printStackTrace();
      }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView Helpline_Name,Helpline_number;
        AppCompatButton call;


        ViewHolder(View itemView) {
            super(itemView);
            Helpline_Name = itemView.findViewById(R.id.Helpline_FullName);
            Helpline_number = itemView.findViewById(R.id.Helpline_number);
            call = itemView.findViewById(R.id.helpline_btn);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String PhoneNUm = Helpline_number.getText().toString();
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
