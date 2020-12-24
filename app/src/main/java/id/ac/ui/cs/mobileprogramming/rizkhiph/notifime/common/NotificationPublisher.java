package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPublisher extends BroadcastReceiver {
    private static final String TAG = "Notification Publisher";

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "[+] Receive Broadcast");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(notificationId, notification);
        Log.i(TAG, "[+] Pushing Notification");

    }

// 　　しんだあああ
}
