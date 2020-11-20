package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.implementation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.BaseActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.NotificationPublisher;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.NotificationService;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.NotificationInterface;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.fragment.NotificationFragment;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.fragment.NotificationListFragment;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.model.Notification;

public class NotificationActivity extends BaseActivity implements NotificationInterface {

    private static final String TAG = "Notification Activity";
    private boolean twoPane;
    private NotificationService notificationService = new NotificationService(this, this, this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.notification_layout);
        super.onCreateDrawer(R.layout.notification_layout);

        twoPane = findViewById(R.id.item_detail_container) != null;

        if (savedInstanceState == null) {
            NotificationListFragment fragment = new NotificationListFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, NotificationListFragment.TAG)
                    .commit();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void changeDetailFragment(Notification notification) {
        NotificationFragment notificationFragment = NotificationFragment.forNotification(notification.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(twoPane ? R.id.item_detail_container : R.id.fragment_container, notificationFragment)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPushNotification(Notification entity) {
        App app = (App) getApplication();
        Log.i(TAG, "[+] Creating New Notification");
        ContentEntity content = app.getRepository().loadContentSync(entity.getContentId());

        this.getNotificationService().createNotification(content.getDate(),
                content.getTime(), entity.getText(), entity.getTitle(), entity.getId());
    }

    public NotificationEntity updateNotification(Notification entity) {
        App app = (App) getApplication();
        Log.i(TAG, "[+] Updating Database");
        NotificationEntity notificationEntity = new NotificationEntity(entity.getId(), entity.getTitle(),
                entity.getMessage(), entity.getContentId(), entity.getText(), !entity.isNotify());

        app.getDatabase().historyNotificationDao().update(notificationEntity);
        System.out.println(notificationEntity);
        return notificationEntity;
    }

    public void cancelNotification(Notification entity) {
//        NotificationPublisher.cancelNotification(getApplicationContext(), getIntent(), entity.getId());
        Intent intent = new Intent(getApplicationContext(), NotificationPublisher.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), entity.getId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    @Override
    public void onBackPressed() {
        super.defaultBackPressed();
    }
}
