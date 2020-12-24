package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.AppDatabase;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.SearchControllerListener;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.implementation.SearchActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.db.entity.SearchHistoryEntity;

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
        SearchActivity activity = (SearchActivity) getListener();
        ConnectivityManager cm =
                (ConnectivityManager) activity.getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (v.getId() == R.id.history_button) {
            this.getListener().onHistoryButtonClick();
            return;
        }
        if (v.getId() == R.id.notification_button) {
            this.getListener().onCreateNotification();
            return;
        }
        if (!isConnected) {
            activity.showPopupInternet();
        }
        else if (this.state.equals("Anime")) {
            this.getSearchService().searchAnime(name);
        } else {
            this.getSearchService().searchManga(name);
        }

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
