package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.DataRepository;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.db.entity.NotificationEntity;

public class NotificationSettingViewModel extends AndroidViewModel {
    private final LiveData<NotificationEntity> mObservableHistoryNotification;
    private final DataRepository mRepository;

    private final int mNotificationId;

    public NotificationSettingViewModel(@NonNull Application application, DataRepository repository, final int notificationId) {
        super(application);
        mNotificationId = notificationId;
        mRepository = repository;

        mObservableHistoryNotification = mRepository.loadNotification(notificationId);
    }

    public LiveData<NotificationEntity> getNotification() {
        return mObservableHistoryNotification;
    }

    public DataRepository getmRepository() {
        return mRepository;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final int notificationId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int notificationId) {
            this.application = application;
            this.notificationId = notificationId;
            this.mRepository = ((App) application).getRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NotificationSettingViewModel(application, mRepository, notificationId);
        }
    }
}
