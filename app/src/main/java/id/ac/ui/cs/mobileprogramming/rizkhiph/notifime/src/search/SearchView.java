package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.widget.SwitchCompat;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;

public class SearchView extends LinearLayout {
    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListeners(SearchController listener){
        findViewById(R.id.search_button).setOnClickListener(listener);
        findViewById(R.id.notification_button).setOnClickListener(listener);
        findViewById(R.id.history_button).setOnClickListener(listener);
        ((SwitchCompat) findViewById(R.id.type_to_search)).setOnCheckedChangeListener(listener);
        ((ListView) findViewById(R.id.textView_fetched_results)).setOnItemClickListener(listener);
    }

    public String getText() {return ((EditText) this.findViewById(R.id.text_to_search)).getText().toString();}
}
