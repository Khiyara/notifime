package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.activity.implementation;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.BaseActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.activity.ContentListListener;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.fragment.ContentFragment;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.fragment.ContentListFragment;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.model.Content;

public class ContentListActivity extends BaseActivity implements ContentListListener {

    private boolean twoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            setContentView(R.layout.content_layout);
//            this.addDynamicFragment();
//            if (savedInstanceState == null) {
//                ContentListFragment fragment = new ContentListFragment();
//
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.fragment_container, fragment, ContentListFragment.TAG).commit();
//            }
        }
//        super.setContentView(R.layout.content_layout);
        super.onCreateDrawer(R.layout.content_layout);
//            this.addDynamicFragment();
        if (findViewById(R.id.item_detail_container) != null) {
            twoPane = true;
        }else {
            twoPane = false;
        }
        if (savedInstanceState == null) {
            ContentListFragment fragment = new ContentListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, ContentListFragment.TAG).commit();
        }
//        else {
//            setContentView(R.layout.content_layout_landscape);
//            if (savedInstanceState == null) {
//                //たすけて！
//
//            }
//        }
    }

    private void addDynamicFragment() {
//        Fragment fg = ListContentFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fg).commit();
    }

    @Override
    public void onFragmentChangeRequest(Fragment newFragment) {
        this.changeDetailFragment(newFragment);
    }

    public void changeDetailFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void show(Content content) {
        ContentFragment contentFragment = ContentFragment.forContent(content.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(twoPane ? R.id.item_detail_container : R.id.fragment_container,
                        contentFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.defaultBackPressed();
    }
}
