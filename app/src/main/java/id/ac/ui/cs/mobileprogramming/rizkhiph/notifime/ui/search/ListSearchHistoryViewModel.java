package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.DataRepository;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.db.entity.SearchHistoryEntity;

public class ListSearchHistoryViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandle;
    private final DataRepository repository;
    private final LiveData<List<SearchHistoryEntity>> searchHistories;

    public ListSearchHistoryViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandle = savedStateHandle;

        repository = ((App) application).getRepository();
        searchHistories = Transformations.switchMap(
                savedStateHandle.getLiveData(QUERY_KEY, null),
                (Function<CharSequence, LiveData<List<SearchHistoryEntity>>>) query -> {
                    if(TextUtils.isEmpty(query)) {
                        return repository.getHistories();
                    }
                    return repository.searchHistory("%" + query + "%");
                }
        );
    }

    public void setQuery(CharSequence query) {
        mSavedStateHandle.set(QUERY_KEY, query);
    }

    public LiveData<List<SearchHistoryEntity>> getSearchHistories() {
        return searchHistories;
    }
}
