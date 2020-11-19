package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.DataRepository;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;


public class   ContentViewModel extends AndroidViewModel {
    private final LiveData<ContentEntity> mObservableContent;
    private final LiveData<NotificationEntity> mObservableHistoryNotificaiton;
    private final DataRepository mRepository;

    private final int mContentId;

    public ContentViewModel(@NonNull Application application, DataRepository repository, final int contentId) {
        super(application);
        mContentId = contentId;

        mRepository = repository;
        mObservableContent = repository.loadContent(contentId);
        mObservableHistoryNotificaiton = repository.loadHistoryNotification(contentId);
    }

    public LiveData<ContentEntity> getContents() {
        return mObservableContent;
    }

    public LiveData<NotificationEntity> getHistoryNotification() {
        return mObservableHistoryNotificaiton;
    }

    public DataRepository getmRepository() {
        return mRepository;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mContentId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int cntentId) {
            mApplication = application;
            mContentId = cntentId;
            mRepository = ((App) application).getRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ContentViewModel(mApplication, mRepository, mContentId);
        }
    }
}
