package br.com.daniel.drinkwater.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import br.com.daniel.drinkwater.R;
import br.com.daniel.drinkwater.ui.MainActivity;

public class NotificationPublisher extends BroadcastReceiver {

    public static final String KEY_NOTIFICATION = "key_notification";
    public static final String KEY_NOTIFICATION_ID = "key_notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Intent intention = new Intent(context.getApplicationContext(),
                MainActivity.class);
        final PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intention, 0);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String message = intent.getStringExtra(KEY_NOTIFICATION);
        int id = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);


        Notification notification = getNotification(message,
                context, notificationManager, pendingIntent);
        assert notificationManager != null;
        notificationManager.notify(id, notification);


    }

    private Notification getNotification(String content,
                                         Context context,
                                         NotificationManager manager,
                                         PendingIntent intent) {
        Notification.Builder builder =
                new Notification.Builder(context.getApplicationContext())
                        .setContentText(content)
                        .setTicker("alerta")
                        .setContentIntent(intent)
                        .setAutoCancel(false)
                        .setSmallIcon(R.drawable.ic_access_alarm);

        verifyVersionAndroid(manager, builder);
        return builder.build();
    }

    private void verifyVersionAndroid(NotificationManager manager, Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            final NotificationChannel channel =
                    new NotificationChannel(channelId, "Channel",
                            NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
    }
}
