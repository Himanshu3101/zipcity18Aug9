package com.yoeki.iace.societymanagment.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yoeki.iace.societymanagment.R;

/**
 * Created by IACE on 08-Aug-18.
 */

public class MyFirebaseInstanceID extends FirebaseInstanceIdService {
    public static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {
            String Token = FirebaseInstanceId.getInstance().getToken();
            sendRegistrationToServer(Token);
            Log.d(REG_TOKEN,Token);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    private void sendRegistrationToServer(String token) {
        SharedPreferenceUtils.getInstance(this).setValue(getString(R.string.firebase_cloud_messaging_token), token);
    }
}