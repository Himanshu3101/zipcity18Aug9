package com.yoeki.iace.societymanagment.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yoeki.iace.societymanagment.Notification.Notification;
import com.yoeki.iace.societymanagment.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IACE on 07-Aug-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //    private static int BIG_TEXT_NOTIFICATION_KEY = 0;
    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.notify.txt";
    String Title, Desc;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentText(remoteMessage.getNotification().getBody());
//        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        Desc = remoteMessage.getNotification().getBody();
        Title = remoteMessage.getNotification().getTitle();
        directory();
        builder.setContentTitle(Title);
        builder.setContentText(Desc);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.zipciti_logo);
        builder.setContentIntent(pendingIntent);
        builder.setVibrate(new long[]{200, 200, 200, 200});
//        builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.header));
//        builder.setNumber(++BIG_TEXT_NOTIFICATION_KEY);
//        builder.setContentText("Reason - " + reason);
        builder.build().flags |= android.app.Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        try {
            Uri Snotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), Snotification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void directory() {
        File dir = new File("/mnt/sdcard/Android/data/com.android.ZipCity");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
            writenitem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writenitem() {
        try {
            File file = new File(filepath);
            if (file.exists()) {
                readWriteFromFile(this);
            } else {
                file.createNewFile();
                readWriteFromFile(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readWriteFromFile(Context context) {

        String ret = "";

        try {
            File file = new File(filepath);
            if ( file != null ) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                fileReader.close();
                ret = stringBuilder.toString();
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
                bufferedWriter.write(ret+Title+ "~" +Desc+"$");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }
}
