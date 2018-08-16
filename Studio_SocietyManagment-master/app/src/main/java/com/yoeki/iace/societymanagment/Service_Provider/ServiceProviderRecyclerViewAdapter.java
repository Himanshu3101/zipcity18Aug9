package com.yoeki.iace.societymanagment.Service_Provider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceProviderRecyclerViewAdapter extends RecyclerView.Adapter<ServiceProviderRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ServiceProviderRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.serviceproviderrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split("~");

        try {
            if (Break[0].equalsIgnoreCase("null")|| Break[0].equalsIgnoreCase("") || Break[0].equalsIgnoreCase(" ")){
                holder.Ser_Req_title.setText("N/A");
            } else {
                holder.Ser_Req_title.setText(Break[0]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(Break[1].equalsIgnoreCase("null")|| Break[1].equalsIgnoreCase("") || Break[1].equalsIgnoreCase(" ")){
                holder.Ser_Req_Type.setText("N/A");
            }else {
                holder.Ser_Req_Type.setText(Break[1]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[2].equalsIgnoreCase("null")|| Break[2].equalsIgnoreCase("") || Break[2].equalsIgnoreCase(" ")){
                holder.Ser_Req_name.setText("N/A");
            }else {
                holder.Ser_Req_name.setText(Break[2]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[3].equalsIgnoreCase("null")|| Break[3].equalsIgnoreCase("") || Break[3].equalsIgnoreCase(" ")){
                holder.Ser_Req_flat.setText("N/A");
            }else {
                holder.Ser_Req_flat.setText(Break[3]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[4].equalsIgnoreCase("null")|| Break[4].equalsIgnoreCase("") || Break[4].equalsIgnoreCase(" ")){
                holder.Ser_req_cre_on.setText("N/A");
            }else {
                holder.Ser_req_cre_on.setText(Break[4]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[5].equalsIgnoreCase("null")|| Break[5].equalsIgnoreCase("") || Break[5].equalsIgnoreCase(" ")){
                holder.Ser_req_cre_by.setText("N/A");
            }else {
                holder.Ser_req_cre_by.setText(Break[5]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[6].equalsIgnoreCase("null")|| Break[6].equalsIgnoreCase("") || Break[6].equalsIgnoreCase(" ")){
                holder.Ser_Req_request.setText("N/A");
            }else {
                holder.Ser_Req_request.setText(Break[6]);
            }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[7].equalsIgnoreCase("null")|| Break[7].equalsIgnoreCase("") || Break[7].equalsIgnoreCase(" ")){
                holder.Ser_Req_status.setText("N/A");
            } else {
                holder.Ser_Req_status.setText(Break[7]);
                if( Break[7].equals("Created")){
                    holder.stat_of_Req.setText("Accept");
                }else if(Break[7].equals("Closed")){
                    holder.div.setVisibility(View.GONE);
                    holder.btn_frame.setVisibility(View.GONE);
                } else{
                    holder.stat_of_Req.setText("Closed");
                }
            }
        }catch(Exception e){e.printStackTrace();}

        try {
            if (Break[8].equalsIgnoreCase("null")|| Break[8].equalsIgnoreCase("") || Break[8].equalsIgnoreCase(" ")){
                holder.Ser_Req_fdate.setText("N/A");
            } else {
                holder.Ser_Req_fdate.setText(Break[8]);
            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[9].equalsIgnoreCase("null")|| Break[9].equalsIgnoreCase("") || Break[9].equalsIgnoreCase(" ")){
                holder.Ser_Req_tdate.setText("N/A");
            }else {
                holder.Ser_Req_tdate.setText(Break[9]);
            }
        }catch(Exception e){e.printStackTrace();}


        try{
            if(Break[10].equalsIgnoreCase("null")|| Break[10].equalsIgnoreCase("") || Break[10].equalsIgnoreCase(" ")){
                holder.Ser_Req_description.setText("N/A");
            }else {
                String dottedelement=null;
                if(Break[10].length()>35){

                    dottedelement  =  Break[10].substring(0,32)+"...";

                    holder.Ser_Req_description.setText(dottedelement);
                }else{

                    holder.Ser_Req_description.setText(dottedelement);
                    holder.Ser_Req_description.setText(Break[10]);
                }

            }
        }catch(Exception e){e.printStackTrace();}

        try{
            if(Break[12].equalsIgnoreCase("null")|| Break[9].equalsIgnoreCase("") || Break[9].equalsIgnoreCase(" ")){
                holder.req_iid.setText("N/A");
            }else {
                holder.req_iid.setText(Break[12]);
            }
        }catch(Exception e){e.printStackTrace();}
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView Ser_Req_title,Ser_Req_Type,Ser_Req_name,Ser_Req_flat,Ser_req_cre_on,Ser_req_cre_by,Ser_Req_request,Ser_Req_status
                ,Ser_Req_fdate,Ser_Req_tdate,Ser_Req_description,req_iid,stat_of_Req;
        AppCompatButton Ser_whole_btnfrRequest,Ser_CallRequest,Ser_AcceptRequest;
        ProgressDialog PD;
        String IDofReq;
        View div;
        FrameLayout btn_frame;
        private ArrayList<String> RequestList;

        ViewHolder(View itemView) {
            super(itemView);

            Ser_Req_title = itemView.findViewById(R.id.Ser_Req_title);
            Ser_Req_Type = itemView.findViewById(R.id.Ser_Req_Type);
            Ser_Req_name = itemView.findViewById(R.id.Ser_Req_name);
            Ser_Req_flat = itemView.findViewById(R.id.Ser_Req_flat);
            Ser_req_cre_on = itemView.findViewById(R.id.Ser_req_cre_on);
            Ser_req_cre_by = itemView.findViewById(R.id.Ser_req_cre_by);
            Ser_Req_request = itemView.findViewById(R.id.Ser_Req_request);
            Ser_Req_status = itemView.findViewById(R.id.Ser_Req_status);
            Ser_Req_fdate = itemView.findViewById(R.id.Ser_Req_fdate);
            Ser_Req_tdate = itemView.findViewById(R.id.Ser_Req_tdate);
            Ser_Req_description = itemView.findViewById(R.id.Ser_Req_description);
            stat_of_Req = itemView.findViewById(R.id.stat_of_Req);

            div = itemView.findViewById(R.id.div);
            btn_frame = itemView.findViewById(R.id.btn_frame);

            req_iid = itemView.findViewById(R.id.req_iid);

            Ser_whole_btnfrRequest = itemView.findViewById(R.id.Ser_whole_btnfrRequest);
            Ser_AcceptRequest = itemView.findViewById(R.id.Ser_AcceptRequest);


            PD = new ProgressDialog(context);
            PD.setMessage("Loading...");
            PD.setCancelable(false);

            Ser_AcceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datafrAcceptByService();
                }
            });

            Ser_whole_btnfrRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void datafrAcceptByService(){
            PD.show();
            String  json_url, statusSend;
            HashMap<String, String> params;
            String StatusSEnd = stat_of_Req.getText().toString();
            if(StatusSEnd.equals("Accept")){
                statusSend="A";
            }else{
                statusSend="R";
            }
            IDofReq = req_iid.getText().toString();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String UID = prefs.getString("UserID"," ");

            json_url=(context.getString(R.string.BASE_URL) + "/ChangeStatus");
            params = new HashMap<String, String>();
            params.put("userId",UID);
            params.put("Flag","R");
            params.put("Id",IDofReq);
            params.put("Status",statusSend);

            JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                    params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray BDetailArray = null;

                    RequestList = new ArrayList<String>();
                    try {
                        JSONObject loginData = new JSONObject(String.valueOf(response));
                        String resStatus = loginData.getString("status");

                        if (resStatus.equals("Success")){
                            PD.dismiss();
                            Toast.makeText(context, "Request Submitted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,ServiceProvider.class);
                            context.startActivity(intent);
                        }else{
                            PD.dismiss();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            },  new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PD.dismiss();
                    Toast.makeText(context, "Something went Wrong :"+error, Toast.LENGTH_SHORT).show();
                    Log.w("error in response", "Error: " + error.getMessage());
                }
            });

            req.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            MyApplication.getInstance().addToReqQueue(req);
        }
    }
}
