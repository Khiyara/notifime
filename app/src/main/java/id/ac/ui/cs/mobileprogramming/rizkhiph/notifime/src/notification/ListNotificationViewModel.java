package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.DataRepository;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;

public class ListNotificationViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandle;
    private final DataRepository repository;
    private final LiveData<List<NotificationEntity>> notifications;

    public ListNotificationViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandle = savedStateHandle;

        repository = ((App) application).getRepository();
        notifications = Transformations.switchMap(
                savedStateHandle.getLiveData(QUERY_KEY, null),
                (Function<CharSequence, LiveData<List<NotificationEntity>>>) query -> {
                    if(TextUtils.isEmpty(query)) {
                        return repository.getNotifications();
                    }
                    return repository.searchNotification("%" + query + "%");
                }
        );
    }

    public void setQuery(CharSequence query) {
        mSavedStateHandle.set(QUERY_KEY, query);
    }

    public LiveData<List<NotificationEntity>> getNotifications() {
        return notifications;
    }

    public DataRepository getRepository() {
        return repository;
    }
}
