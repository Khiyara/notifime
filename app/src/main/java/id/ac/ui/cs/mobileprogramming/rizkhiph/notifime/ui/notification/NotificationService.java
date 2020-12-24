package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.FileDownloadProvider;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.NotificationPublisher;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.activity.NotificationInterface;

public class NotificationService extends Service implements NotificationInterface {
    private final String TAG = "Notification Service";

    private Context context;
    private Activity activity;
    private NotificationInterface service;

    public NotificationService(Context context, Activity activity, NotificationInterface service) {
        this.context = context;
        this.activity = activity;
        this.service = service;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotification(String date, String time, String text,
                                   String title, int notificationId) {
        Log.i(TAG, "[+] On Create Notification");
        Long dateToTrigger = parseDate(date, time);
        Log.i(TAG, "[+] Notification will be trigger at " + (dateToTrigger));

        this.buildNotification(text, title, dateToTrigger, notificationId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buildNotification(String text, String title, long timeToTrigger, int notificationId) {
        Log.i(TAG, "[+] Building Notification");
        String channelId = createNotificationChannel(this.getContext());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getContext(), channelId);
        notificationBuilder.setSmallIcon(R.drawable.logo_only)
                .setContentText(text)
                .setContentTitle(title)
                .setLargeIcon(getBitMap(notificationId))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitMap(notificationId))
                        .bigLargeIcon(null));

        Intent intent = new Intent(this.getContext(), this.getActivity().getClass());
        PendingIntent activity = PendingIntent.getActivity(this.getContext(), notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(activity);

        Notification notification = notificationBuilder.build();
    
        Intent notificationIntent = new Intent(this.getContext().getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getContext().getApplicationContext(), notificationId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(TAG, "[+] Will be notified at " + timeToTrigger);
        AlarmManager alarmManager = (AlarmManager) this.getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToTrigger, pendingIntent);

        Log.i(TAG, "[+] Done Building Notification");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String createNotificationChannel(Context context) {
        String channelId = "ReminderNotification_1";
        String channelName = "ReminderNotification";
        int channelImportance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
        notificationChannel.setDescription("Reminder");
        notificationChannel.enableVibration(false);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        return channelId;
    }

    private Bitmap getBitMap(int id) {
        ContentResolver cp = getContext().getContentResolver();
        Log.d(TAG, "[+] Getting File Image with ID " + id);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(cp, Uri.parse(FileDownloadProvider.URL + '/' + id));
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long parseDate(String date, String time) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int dayToTrigger = dateToInt(date);
        String[] timeToTrigger = time.split(":");
        int hourToTrigger = Integer.parseInt(timeToTrigger[0]);
        int minuteToTrigger = Integer.parseInt(timeToTrigger[1]);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(year, month,
                dayToTrigger < dayOfWeek ?
                        day + (dayToTrigger + 7 - dayOfWeek) :
                        day + (dayToTrigger - dayOfWeek),
                  hourToTrigger - 2, minuteToTrigger
        );
//        return (Calendar.getInstance().getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public int dateToInt(String date) {
        switch (date) {
            case "Sundays":
                return 1;
            case "Mondays":
                return 2;
            case "Tuesdays":
                return 3;
            case "Wednesdays":
                return 4;
            case "Thursdays":
                return 5;
            case "Fridays":
                return 6;
            case "Saturdays":
                return 7;
            default:
                return 1;
        }
    }

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public NotificationInterface getService() {
        return service;
    }

    @Override
    public void onPushNotification(id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.model.Notification entity) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
