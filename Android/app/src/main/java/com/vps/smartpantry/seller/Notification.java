package com.vps.smartpantry.seller;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.vps.smartpantry.R;
import com.vps.smartpantry.seller.MainPage;

public class Notification
{
    private Context context;
    NotificationManager notificationManager;
    int id;

    public Notification(Context c)
    {

        context=c;
        notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public int genrateNotification(String title, String subject)
    {
        id= (int) System.currentTimeMillis();
        Intent i=new Intent(context,MainPage.class);
        PendingIntent pIntent = PendingIntent.getActivity(context,id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] v = {500, 700};
        android.app.Notification n = new android.app.Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(subject)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)

                .setPriority(android.app.Notification.PRIORITY_MAX)
                .setAutoCancel(true).setSound(uri).setVibrate(v).build();
                 n.flags= android.app.Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(id, n);
        return  id;
    }

}
