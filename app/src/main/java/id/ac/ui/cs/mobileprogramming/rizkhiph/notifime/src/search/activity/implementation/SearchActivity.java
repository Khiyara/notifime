package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity.implementation;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.squareup.picasso.Picasso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.BaseActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.FileDownloadProvider;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.AppDatabase;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.NotificationService;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.NotificationInterface;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.model.Notification;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.SearchController;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.SearchView;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity.SearchControllerListener;

public class SearchActivity extends BaseActivity implements SearchControllerListener, NotificationInterface {
    private static final String TAG = "Search Activity";
    public static final String SHARED_PREFERENCES = "Search";

    private NotificationService notificationService = new NotificationService(this, this, this);

    public SearchActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.search_layout);
        super.onCreateDrawer(R.layout.search_layout);
//        if (android.os.Build.VERSION.SDK_INT > 9)
//        {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
        Button notificationButton = (Button) this.findViewById(R.id.notification_button);
        notificationButton.setVisibility(View.GONE);

        SearchController searchController = new SearchController((SearchView) this.findViewById(R.id.search), this);
        ((SearchView) this.findViewById(R.id.search)).setListeners(searchController);
//        SharedPreferences mPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
//        String content = mPrefs.getString("Content", "");
//        if (content.equals("")) {
//            Log.i(TAG, "[+] Shared Preferences is Empty Adding ListContent Object");
//            SharedPreferences.Editor prefsEditor = mPrefs.edit();
//            ListContent listContent = new ListContent();
//            Gson gson = new Gson();
//            String json = gson.toJson(listContent);
//            prefsEditor.putString("Content", json);
//            prefsEditor.apply();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ShowToast")
    @Override
    public void onCreateNotification() {
        TextView textView = (TextView) this.findViewById(R.id.mal_id_state);
        String data = (String) textView.getText();
        Log.i(TAG, "[+] On Creating Notification for " + data);
        String[] dateTime = data.split("\\|");
        String[] broadcast = dateTime[1].split(" ");
        int id = Integer.parseInt(dateTime[0]);
        String date = broadcast[0];
        String time = broadcast[2];
        String title = dateTime[2];
        String airing = dateTime[dateTime.length - 2];
        String text = title + " new episode is release now";
        String url = dateTime[dateTime.length - 1];
        ContentValues contentValues = new ContentValues();
        contentValues.put("KEY", id);
        contentValues.put("URL", url);
        getContentResolver().insert(FileDownloadProvider.CONTENT_URI, contentValues);
        ContentEntity content = new ContentEntity(id, title, time, date, Boolean.parseBoolean(airing), false);
        AppDatabase appDatabase = ((App) getApplication()).getDatabase();
        appDatabase.contentDao().insert(content);
        Toast toast;
        if (!airing.equals("false")) {
            String message = title + " will be remineded at " + date + " " + time;
            NotificationEntity notificationEntity = new NotificationEntity(title, message, id, text);
            this.getNotificationService().createNotification(date, time, text, title, notificationEntity.getId());
            if (appDatabase.historyNotificationDao().loadHistoryNotificationSync(id) == null) {
                appDatabase.historyNotificationDao().insert(notificationEntity);
            }
            toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        } else {
            toast = Toast.makeText(getApplicationContext(), title + " is not airing", Toast.LENGTH_LONG);
        }

        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();

    }

//    public void saveToPreferences(NotificationEntity listContent) {
//        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = preferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(listContent);
//        Log.i(TAG, "[+] JSON " + json);
//        prefsEditor.putString("Content", json);
//        prefsEditor.apply();
//    }
//
//    public void setContents(ContentEntity content) {
//        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
//        String json = preferences.getString("Content", "");
//        Log.i(TAG, "[+] JSON now " + json);
//        Gson gson = new Gson();
//        NotificationEntity obj = gson.fromJson(json, NotificationEntity.class);
//        boolean isInSharedPreferences = false;
//        for (int i = 0; i < obj.getContents().size(); i++) {
//            if (obj.getContents().get(i).getId().equals(content.getId())) {
//                isInSharedPreferences = true;
//                break;
//            }
//        }
//        if (!isInSharedPreferences) {
//            Log.i(TAG, "[+] Adding ID " + content.getId());
//            obj.getContents().add(content);
//        }
//        this.saveToPreferences(obj);
//    }

    @Override
    public void onStateChange(String text) {
        ((TextView) this.findViewById(R.id.title_text)).setText(text);
        if (text.equals("Manga")) {
            Button notificationButton = (Button) this.findViewById(R.id.notification_button);
            notificationButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetSearch(String text) {
        Log.d(TAG, "[+] On Get Search List");
        findViewById(R.id.detail).setVisibility(View.GONE);
        findViewById(R.id.list).setVisibility(View.VISIBLE);
        try {
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject) parse.parse(text);
            JSONArray result = (JSONArray) obj.get("results");
            ArrayList<String> urls = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++) {
                Long id = (Long) ((JSONObject) result.get(i)).get("mal_id");
                String url = Long.toString(id);
//                Log.d(TAG, url);
                url += "|";
                url += (String) ((JSONObject) result.get(i)).get("url");
                urls.add(url);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, urls);
            ((ListView) this.findViewById(R.id.textView_fetched_results)).setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "[-] Something went wrong");
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onGetDetail(String text) {
        findViewById(R.id.list).setVisibility(View.GONE);
        findViewById(R.id.detail).setVisibility(View.VISIBLE);
        try {
            JSONParser parse = new JSONParser();
            JSONObject result = (JSONObject) parse.parse(text);
            String data = (String) Long.toString((Long) result.get("mal_id"))
                    + "|"
                    + (String) result.get("broadcast")
                    + "|"
                    + (String) result.get("title")
                    + "|"
                    + (Boolean) result.get("airing")
                    + "|"
                    + (String) result.get("image_url");
            TextView textView = (TextView) this.findViewById(R.id.mal_id_state);
            textView.setText(data);
            String views = this.formatString((String) result.toString());
            ((TextView) findViewById(R.id.detail_text)).setText(views);
            Picasso.get().load((String) result.get("image_url")).into((ImageView) findViewById(R.id.thumbnail));
//            ArrayList<String> detail = new ArrayList<>();
//            detail.add(views);
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, detail);
//            ((ListView) this.findViewById(R.id.textView_fetched_results)).setAdapter(adapter);

        } catch (Exception e) {
            Log.e(TAG, "[-] Something went wrong");
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Button notificationButton = (Button) this.findViewById(R.id.notification_button);
        notificationButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHistoryButtonClick() {
        Intent intent = new Intent(this, HistoryActivity.class);
        this.startActivity(intent);
    }

    public String formatString(String text){
        StringBuilder json = new StringBuilder();
        String indentString = "";
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n").append(indentString).append(letter).append("\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n").append(indentString).append(letter);
                    break;
                case ',':
                    json.append(letter).append("\n").append(indentString);
                    break;
                default:
                    json.append(letter);
                    break;
            }
        }
        return json.toString();
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    @Override
    public void onPushNotification(Notification entity) {
        // no operation
    }
}
