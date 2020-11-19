package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.AppDatabase;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity.SearchControllerListener;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity.implementation.SearchActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.db.entity.SearchHistoryEntity;

@SuppressLint("SearchApi")
public class SearchController implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    private final String TAG = "Search controller";

    private SearchControllerListener listener;
    private SearchView searchView;
    private SearchService searchService;

    private String state = "Anime";

    public SearchController(SearchView searchView, SearchControllerListener listener) {
        this.searchView = searchView;
        this.listener = listener;
        this.searchService = new SearchService(listener);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "[+] Button Clicked");
        String name = this.getSearchView().getText();
        if (v.getId() == R.id.history_button) {
            this.getListener().onHistoryButtonClick();
            return;
        }
        if (v.getId() == R.id.notification_button) {
            this.getListener().onCreateNotification();
            return;
        }
        else if (this.state.equals("Anime")) {
            this.getSearchService().searchAnime(name);
        } else {
            this.getSearchService().searchManga(name);
        }

        SearchActivity activity = (SearchActivity) getListener();
        AppDatabase appDatabase = ((App) activity.getApplication()).getDatabase();
        SearchHistoryEntity searchHistory = new SearchHistoryEntity(name);
        appDatabase.searchHistoryDao().insert(searchHistory);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        // SearchActivity Change Text
        Log.i(TAG, "[+] Switch Button Clicked");
        this.setState(isChecked);
        Log.i(TAG, "[+] State Now: " + this.state);
        this.getListener().onStateChange(this.state);
    }

    public void setState(boolean isChecked) {
        if (isChecked) {
            this.state = "Manga";
        } else {
            this.state = "Anime";
        }
    }

    public String getState() {
        return this.state;
    }

    public SearchControllerListener getListener() {
        return this.listener;
    }

    public SearchView getSearchView() {
        return this.searchView;
    }

    public SearchService getSearchService() {
        return this.searchService;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "[+] clicked Item: " + id + " at position:" + position);
        Log.i(TAG, "[+] " + parent.getItemAtPosition(position));
        String parsedId = ((String) parent.getItemAtPosition(position)).split("\\|")[0];
        Log.i(TAG, "[+] " + parsedId);
        if (this.state == "Anime") {
            this.getSearchService().detailAnime(parsedId);
            this.getListener().onItemClick(parent, view, position, id);
        } else {
            this.getSearchService().detailManga(parsedId);
        }
    }
}
