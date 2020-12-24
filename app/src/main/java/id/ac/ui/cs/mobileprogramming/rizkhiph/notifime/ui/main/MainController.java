package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main.activity.MainControllerListener;

@SuppressLint("NewApi")
public class MainController implements View.OnClickListener {

    private final String TAG = "Main controller";

    private MainControllerListener listener;

    public MainController(MainControllerListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Log.i(TAG, "[+] Button Clicked");
        int id = v.getId();
        switch (id) {
            case R.id.search_page_button:
                Log.i(TAG, "[+] Search Button Clicked");
                this.getListener().onSearchButtonClick();
                break;
            case R.id.notification_view_button:
                Log.i(TAG, "[+] Notification Button Clicked");
                this.getListener().onNotificationButtonClick();
                break;
            case R.id.content_view_button:
                Log.i(TAG, "[+] Content Button Clicked");
                this.getListener().onContentButtonClick();
                break;
            default:
                Log.i(TAG, "[+] Search Button Clicked");
                this.getListener().onSearchButtonClick();
                break;
        }
    }

    public MainControllerListener getListener() {
        return this.listener;
    }

}

