package com.yoeki.iace.societymanagment.societymanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yoeki.iace.societymanagment.MyApplication;
import com.yoeki.iace.societymanagment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IACE on 13-Aug-18.
 */

public class Request_closed extends Activity {
    EditText Req_Closed_submit_reason;
    TextView Req_Closed_submit_feedback,Req_Closed_submit_code;
    Button Req_Closed_submit_feedback_btn,Req_Closed_submit;
    RatingBar Req_Closed_submit_rating_btn;
    ProgressDialog PD;

    String idReques,feedbackStat;
    Boolean validation;
    ArrayList<String> feedbackIDs;
    String feedback[];
    List<String> FeedbackD;
    int f;
    protected ArrayList<CharSequence> feedbackServices = new ArrayList<CharSequence>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_closed);

        Req_Closed_submit_code = (TextView)findViewById(R.id.Req_Closed_submit_code);
        Req_Closed_submit_feedback = (TextView)findViewById(R.id.Req_Closed_submit_feedback);
        Req_Closed_submit_reason = (EditText)findViewById(R.id.Req_Closed_submit_reason);
        Req_Closed_submit_feedback_btn = (Button)findViewById(R.id.Req_Closed_submit_feedback_btn);
        Req_Closed_submit = (Button)findViewById(R.id.Req_Closed_submit);
        Req_Closed_submit_rating_btn = (RatingBar)findViewById(R.id.Req_Closed_submit_rating_btn);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(false);

        Intent intent= getIntent();
        String unCode = intent.getStringExtra("R_Unique");
        idReques = intent.getStringExtra("R_requestIID");
        Req_Closed_submit_code.setText(unCode);

        FeedbackD = new ArrayList<String>();
        FeedbackD.add("Satisfy");
        FeedbackD.add("Not Satisfy");

        feedback = new String[FeedbackD.size()];
        feedback = FeedbackD.toArray(feedback);

        Req_Closed_submit_feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new AlertDialog.Builder(Request_closed.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Select Visitor Type")
                            .setSingleChoiceItems(feedback, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    ListView lw = ((AlertDialog)dialog).getListView();
                                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                                    Req_Closed_submit_feedback.setText(checkedItem.toString());
                                    if(checkedItem.toString().equals("Satisfy")){
                                        f=0;
                                        feedbackStat = "S";

                                    }else{
                                        f=1;
                                        feedbackStat = "N";
                                    }
//                                    try{
//                                        VisitIds = String.valueOf(db.getVisitListID(checkedItem.toString()));
//                                    }catch(Exception e){
//                                        e.printStackTrace();
//                                    }
                                }
                            })
                            .show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        Req_Closed_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();

                if (validation == true) {
                    try {
                        forClosedRequest();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean validations() {
        validation = true;
        if(f==0){
            if (Req_Closed_submit_feedback.getText().toString().equals("")){
                validation = false;
                Req_Closed_submit_feedback.setError("Select Feedbacks");

            }else if (Req_Closed_submit_feedback.getText().toString().equals("")) {
                validation = false;
                Req_Closed_submit_feedback.setError("Select Feedbacks");
            }
        }else{
            if (Req_Closed_submit_feedback.getText().toString().equals("") &&
                    Req_Closed_submit_reason.getText().toString().equals("")) {
                validation = false;
                Req_Closed_submit_feedback.setError("Select Feedbacks");
                Req_Closed_submit_reason.setError("Enter Reason");

            }else if (Req_Closed_submit_feedback.getText().toString().equals("")) {
                validation = false;
                Req_Closed_submit_feedback.setError("Select Feedbacks");
            }else if (Req_Closed_submit_reason.getText().toString().equals("")) {
                validation = false;
                Req_Closed_submit_reason.setError("Enter Reason");
            }
        }
        return validation;
    }

    public  void forClosedRequest(){
        PD.show();
        String  json_url = (getString(R.string.BASE_URL) + "/RatingWiseRequestStatus");
        String reason = Req_Closed_submit_reason.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String UID = prefs.getString("UserID"," ");
        String rating = String.valueOf(Req_Closed_submit_rating_btn.getRating());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserId",UID);
        params.put("RequestId",idReques);
        params.put("SatisfactionStatus",feedbackStat);
        params.put("Rating",rating);
        params.put("Reason",reason);

        JsonObjectRequest req = new JsonObjectRequest(json_url, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray BDetailArray = null;
                try {
                    JSONObject loginData = new JSONObject(String.valueOf(response));
                    String resStatus = loginData.getString("status");

                    if (resStatus.equalsIgnoreCase("Success")) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Request Closed Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(),SocietyManagement.class);
                        startActivity(intent);
                        finish();
                    }else{
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Please try after some time...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.w("error in response", "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(req);
    }

}
