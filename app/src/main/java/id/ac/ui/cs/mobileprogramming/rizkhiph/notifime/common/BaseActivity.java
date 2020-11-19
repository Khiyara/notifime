package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.activity.implementation.ContentListActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.main.activity.implementation.MainActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.implementation.NotificationActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity.implementation.SearchActivity;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Base Activity";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    protected void onCreateDrawer(View view) {
        setContentView(view);
        onCreateDrawer(-1);
    }

    protected void onCreateDrawer(int layoutResId) {
        Log.d(TAG, "[+] On Create Base Activity");
        if (layoutResId != -1)
            setContentView(layoutResId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        Log.d(TAG, "[+] Navigation Element clicked");
        switch(item.getItemId()) {
            case R.id.nav_item_zero:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.nav_item_one:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.nav_item_two:
                intent = new Intent(this, ContentListActivity.class);
                break;
            case R.id.nav_item_three:
                intent = new Intent(this, NotificationActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        this.startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage(R.string.exit_confirmation)
                .setPositiveButton(R.string.exit_confirmation_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.exit_confirmation_bo, null)
                .show();
    }

    //    public DrawerLayout drawerLayout;
//    public ListView drawerList;
//    public String[] layers;
//    private ActionBarDrawerToggle drawerToggle;

}
