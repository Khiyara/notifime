package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.implementation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.JniManager;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.BaseActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.FileDownloadProvider;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.App;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.AppDatabase;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.NotificationService;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.activity.NotificationInterface;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.db.entity.NotificationEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.model.Notification;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.SearchController;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.SearchView;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.SearchControllerListener;

public class SearchActivity extends BaseActivity implements SearchControllerListener, NotificationInterface {
    private static final String TAG = "Search Activity";
    public static final String SHARED_PREFERENCES = "Search";
    public static final int REQUEST_WRITE_STORAGE = 112;
    public static final int REQUEST_READ_STORAGE = 100;
    public JniManager jniManager;
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;

    private NotificationService notificationService = new NotificationService(this, this, this);

    public SearchActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDrawer(R.layout.search_layout);
        Button notificationButton = (Button) this.findViewById(R.id.notification_button);
        notificationButton.setVisibility(View.GONE);

        SearchController searchController = new SearchController((SearchView) this.findViewById(R.id.search), this);
        ((SearchView) this.findViewById(R.id.search)).setListeners(searchController);
        checkExternalMedia();
        if (Build.VERSION.SDK_INT > 23)
            requestPermission(this);
        jniManager = new JniManager();
    }

    private void checkExternalMedia(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (!mExternalStorageAvailable) {
            Toast.makeText(getApplicationContext(), "[-] External storage not mounted", Toast.LENGTH_LONG).show();
        }
        else if (!mExternalStorageWriteable) {
            Toast.makeText(getApplicationContext(), "[-] External storage is not writeable, allow us to have permission", Toast.LENGTH_LONG).show();
        }
        else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/notifime";
            Log.d(TAG, "[+] " + path);
            File storageDir = new File(path);
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), "[-] Directory not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestPermission(Activity activity) {
        // PERMISSION FOR WRITE SDCARD
        boolean hasPermission = (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
        // PERMISSION FOR READ SDCARD
        hasPermission = (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "[+] Permission to write at external storage granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "[-] Apps was not allowed to write your external storage", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "[+] Permission to read at external storage granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "[-] Apps was not allowed to read your external storage", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
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
//        String text = title + " new episode is release now";
        String text = jniManager.nBuildText(title);
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
//            String message = title + " will be remineded at " + date + " " + time;
            String message = jniManager.nBuildMessage(title, date, time);
            NotificationEntity notificationEntity = new NotificationEntity(title, message, id, text);
            this.getNotificationService().createNotification(date, time, text, title, id);
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

        } catch (Exception e) {
            Log.e(TAG, "[-] Something went wrong");
            Log.e(TAG, e.toString());
        }
    }

    public void showPopupInternet() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.popup_internet_title)
                .setMessage(R.string.popup_internet_message)
                .setPositiveButton(R.string.popup_internet_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= 28) {
                            Intent panelIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivityForResult(panelIntent, 0);
                        } else {
                            if (isAirplaneModeOn()) {
                                showPopupAirplaneMode();
                            }
                            else {
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifiManager.setWifiEnabled(true);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.popup_internet_no, null)
                .show();
    }

    private boolean isAirplaneModeOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(getApplicationContext().getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
        return Settings.System.getInt(
                getApplicationContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    private void showPopupAirplaneMode() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.popup_airplane_title)
                .setMessage(R.string.popup_airplane_message)
                .setPositiveButton(R.string.popup_internet_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent panelIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                        startActivityForResult(panelIntent, 0);
                    }
                })
                .setNegativeButton(R.string.popup_internet_no, null)
                .show();
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
