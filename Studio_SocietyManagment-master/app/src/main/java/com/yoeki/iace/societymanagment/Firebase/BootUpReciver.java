package com.yoeki.iace.societymanagment.Firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by himanshu.Srivastava on 4/29/2017.
 */

public class BootUpReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context, MyFirebaseInstanceID.class);
            context.startService(intent1);
    }

}
