package com.martinforget.cardiaccoherencelite;

/**
 * Created by forgetm on 2016-08-23.
 */
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String CHANNEL_ID = "channel-01";
    public static String CHANNEL_NAME = "Coherence Cardiac";

    public void onReceive(Context context, Intent intent) {

        Intent mainIntent;

        mainIntent = new Intent(context, MainActivity.class);

        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle(context.getResources().getString(R.string.app_name))
                .setSmallIcon(R.drawable.notifi)
                .setContentIntent(pendingIntent)
                .setContentText(context.getResources().getString(R.string.notification_text));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(context.getResources().getString(R.string.notification_text));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            builder.setContentTitle(context.getResources().getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.BLUE, 500, 5000)
                    .setAutoCancel(true);
        }
        mNotificationManager.notify(1, builder.build());
    }
}
