package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main.activity.implementation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.BaseActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.MainLayoutBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.activity.implementation.ContentListActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main.MainController;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main.activity.MainControllerListener;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.activity.implementation.NotificationActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.implementation.SearchActivity;

public class MainActivity extends BaseActivity implements MainControllerListener {

    private static final String TAG = "Main Activity";
    private MainLayoutBinding binding;
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //super.setContentView(view);
        super.onCreateDrawer(view);
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            binding.logo.setImageResource(R.drawable.logo_landscape);
        } else {
            binding.logo.setImageResource(R.drawable.logo);
        }
        MainController mainController = new MainController(this);
        binding.searchPageButton.setOnClickListener(mainController);
        binding.notificationViewButton.setOnClickListener(mainController);
        binding.contentViewButton.setOnClickListener(mainController);
    }

    @Override
    public void onSearchButtonClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onNotificationButtonClick() {
        Intent intent = new Intent(this, NotificationActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onContentButtonClick() {
        Intent intent = new Intent(this, ContentListActivity.class);
        this.startActivity(intent);
    }
}
