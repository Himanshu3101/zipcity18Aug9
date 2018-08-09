package com.yoeki.iace.societymanagment.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yoeki.iace.societymanagment.Notification.Notification;
import com.yoeki.iace.societymanagment.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by IACE on 07-Aug-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private static int BIG_TEXT_NOTIFICATION_KEY = 0;
    String filepath = "/mnt/sdcard/Android/data/com.android.ZipCity/com.android.ZipCity.notify.txt";
    String Title,Desc;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(getApplicationContext(),Notification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Desc = String.valueOf(builder.setContentText(remoteMessage.getNotification().getBody()));
        Title = String.valueOf(builder.setContentTitle(remoteMessage.getNotification().getTitle()));

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



        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
        try {
            Uri Snotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), Snotification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        directory();
        writenitem();
    }

    public void directory() {
        File dir = new File("/mnt/sdcard/Android/data/com.android.ZipCity");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writenitem() {
        try {
            File file = new File(filepath);
            file.createNewFile();
            writenotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writenotification() {
        String FullNotification = Title+"~"+Desc;
        String[] readitems = FullNotification.split("~");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
            bufferedWriter.write(readitems[0]+"$"+readitems[1]);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
