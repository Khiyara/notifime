package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.db.entity.SearchHistoryEntity;

public class DataRepository {
    private static DataRepository instance;

    private final AppDatabase appDatabase;
    private MediatorLiveData<List<ContentEntity>> mObservableContentList;
    private MediatorLiveData<List<NotificationEntity>> mObservableNotificationList;
    private MediatorLiveData<List<SearchHistoryEntity>> mObservableSearchHistoryList;

    private DataRepository(final AppDatabase database) {
        appDatabase = database;
        mObservableContentList = new MediatorLiveData<>();
        mObservableNotificationList = new MediatorLiveData<>();
        mObservableSearchHistoryList = new MediatorLiveData<>();

        mObservableContentList.addSource(appDatabase.contentDao().getAll(), new Observer<List<ContentEntity>>() {
            @Override
            public void onChanged(List<ContentEntity> o) {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableContentList.setValue(o);
                }
            }
        });

        mObservableNotificationList.addSource(appDatabase.historyNotificationDao().getAll(), new Observer<List<NotificationEntity>>() {
            @Override
            public void onChanged(List<NotificationEntity> notificationEntities) {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableNotificationList.setValue(notificationEntities);
                }
            }
        });

        mObservableSearchHistoryList.addSource(appDatabase.searchHistoryDao().getAll(), new Observer<List<SearchHistoryEntity>>() {
            @Override
            public void onChanged(List<SearchHistoryEntity> searchHistoryEntities) {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableSearchHistoryList.setValue(searchHistoryEntities);
                }
            }
        });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(database);
                }
            }
        }
        return instance;
    }

    public LiveData<List<ContentEntity>> getContents() {
        return mObservableContentList;
    }

    public LiveData<ContentEntity> loadContent(final int contentId) {
        return appDatabase.contentDao().findOne(contentId);
    }

    public LiveData<List<ContentEntity>> fetchContent() {
        return appDatabase.contentDao().getAll();
    }

    public LiveData<NotificationEntity> loadHistoryNotification(final int contentId) {
        return appDatabase.historyNotificationDao().findOneByContent(contentId);
    }

    public LiveData<List<ContentEntity>> searchContent(String query) {
        return appDatabase.contentDao().searchContent(query);
    }

    public LiveData<List<NotificationEntity>> getNotifications() {
        return mObservableNotificationList;
    }

    public LiveData<NotificationEntity> loadNotification(final int notificationId) {
        return appDatabase.historyNotificationDao().findOne(notificationId);
    }

    public LiveData<List<NotificationEntity>> searchNotification(String query) {
        return appDatabase.historyNotificationDao().searchNotification(query);
    }

    public ContentEntity loadContentSync(final int contentId) {
        return appDatabase.contentDao().loadContentSync(contentId);
    }

    public LiveData<List<SearchHistoryEntity>> getHistories() {
        return mObservableSearchHistoryList;
    }

    public LiveData<List<SearchHistoryEntity>> searchHistory(String query) {
        return appDatabase.searchHistoryDao().searchHistory(query);
    }
}
