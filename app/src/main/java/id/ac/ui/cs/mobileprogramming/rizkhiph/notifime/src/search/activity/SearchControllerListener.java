package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity;

import android.widget.AdapterView;

public interface SearchControllerListener extends AdapterView.OnItemClickListener {
    public void onCreateNotification();
    public void onStateChange(String text);
    public void onGetSearch(String text);
    public void onGetDetail(String text);
    public void onHistoryButtonClick();
}
