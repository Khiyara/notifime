package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.implementation;

import android.os.Bundle;

import androidx.annotation.Nullable;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.BaseActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.db.entity.SearchHistoryEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.fragment.SearchListFragment;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.model.SearchHistory;

public class HistoryActivity extends BaseActivity {

    private static final String TAG = "History Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.history_layout);
        super.onCreateDrawer(R.layout.history_layout);

        if (savedInstanceState == null) {
            SearchListFragment fragment = new SearchListFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, SearchListFragment.TAG)
                    .commit();
        }
    }

    public void removeHistory(SearchHistory searchHistory) {
        ((App)getApplication()).getDatabase().searchHistoryDao().delete((SearchHistoryEntity) searchHistory);
    }
}
