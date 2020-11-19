package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content;

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
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.entity.ContentEntity;

public class ListContentViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandle;
    private final DataRepository repository;
    private final LiveData<List<ContentEntity>> contents;

    public ListContentViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandle = savedStateHandle;

        repository = ((App) application).getRepository();
        contents = Transformations.switchMap(
                savedStateHandle.getLiveData(QUERY_KEY, null),
                (Function<CharSequence, LiveData<List<ContentEntity>>>) query -> {
                    if(TextUtils.isEmpty(query)) {
                        return repository.getContents();
                    }
                    return repository.searchContent( "%" + query + "%");
        }
        );
    }

    public void setQuery(CharSequence query) {
        mSavedStateHandle.set(QUERY_KEY, query);
    }

    public LiveData<List<ContentEntity>> getContents() {
        return contents;
    }

    public DataRepository getRepository() {
        return repository;
    }
}
