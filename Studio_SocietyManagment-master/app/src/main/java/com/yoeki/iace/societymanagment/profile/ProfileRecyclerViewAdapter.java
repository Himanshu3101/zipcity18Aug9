package com.yoeki.iace.societymanagment.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.DataObject.loginObject;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private static Context context;
    ArrayList<String> Statust;
    List<loginObject> loginBData;
//    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ProfileRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.profilerecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String unit = mData.get(position);
        String[] Break = unit.split(",");

        if (Break[0].equalsIgnoreCase("null")|| Break[0].equalsIgnoreCase("") || Break[0].equalsIgnoreCase(" ")){
            holder.memName.setText("");
        }else {
            holder.memName.setText(Break[0]);
        }

        if(Break[1].equalsIgnoreCase("null")|| Break[1].equalsIgnoreCase("") || Break[1].equalsIgnoreCase(" ")){
            holder.memGender.setText("");
        }else {
            holder.memGender.setText(Break[1]);
        }

        if(Break[2].equalsIgnoreCase("null")|| Break[2].equalsIgnoreCase("") || Break[2].equalsIgnoreCase(" ")){
            holder.memContact.setText("");
        }else {
            holder.memContact.setText(Break[2]);
        }

        if(Break[3].equalsIgnoreCase("null")|| Break[3].equalsIgnoreCase("") || Break[3].equalsIgnoreCase(" ")){
            holder.mem_status.setText("");
        }else {
            if(Break[3].equals("Active")){
                holder.mem_status.setText("Deactive");
            }else{
                holder.mem_status.setText("Active");
            }
        }

        if(Break[4].equalsIgnoreCase("null")|| Break[4].equalsIgnoreCase("") || Break[4].equalsIgnoreCase(" ")){
            holder.mem_logID.setText("");
        }else {
            holder.mem_logID.setText(Break[4]);
        }

        if(Break[5].equalsIgnoreCase("null")|| Break[5].equalsIgnoreCase("") || Break[5].equalsIgnoreCase(" ")){
            holder.mem_ID.setText("");
        }else {
            holder.mem_ID.setText(Break[5]);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView memName,memGender,memContact,mem_logID,mem_ID;
        AppCompatButton mem_status;
        ProgressDialog PD;

        ViewHolder(View itemView) {
            super(itemView);
            memName = itemView.findViewById(R.id.mem_name);
            memGender = itemView.findViewById(R.id.mem_gender);
            memContact = itemView.findViewById(R.id.mem_contact);
            mem_status = itemView.findViewById(R.id.mem_status);
            mem_logID = itemView.findViewById(R.id.mem_logID);
            mem_ID = itemView.findViewById(R.id.mem_ID);

            PD = new ProgressDialog(context);
            PD.setMessage("Loading...");
            PD.setCancelable(false);

            mem_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forStatusUpdated();
                }
            });
        }

            public void forStatusUpdated(){
                PD.show();
                Statust = new ArrayList<>();

                String json_url=(context.getString(R.string.BASE_URL) + "/UpdateUserStatus");

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String UID = prefs.getString("UserID"," ");
                String MemLogID = mem_logID.getText().toString();
                String MemID = mem_ID.getText().toString();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("UserId",MemLogID);
                params.put("UserSecondaryId",MemID);

                JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                        params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray ProfileDetailArray = null,requestTypeArray = null,rejectionListArray = null,bindUserArray = null,userWiseRoleArray = null;

                        try {
                            JSONObject loginData = new JSONObject(String.valueOf(response));
                            String resStatus = loginData.getString("status");
                            String msg = loginData.getString("message");
                            if (resStatus.equalsIgnoreCase("Success")) {
                                String Status = loginData.getString("UserStatus");
                                if(Status.equals("Active")){
                                    mem_status.setText("Deactive");
                                }else{
                                    mem_status.setText("Active");
                                }
                                PD.dismiss();
                            }else{
                                PD.dismiss();
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
