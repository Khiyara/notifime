package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.io.IOException;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.FileDownloadProvider;

public class BindingAdapters {
    private static final String TAG = "Binding Adapters";

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("setAiring")
    public static void setAiring(TextView textView, boolean airing) {
        textView.setText(airing ? "Show is Airing now" : "Show is not Airing");
    }

    @BindingAdapter("setNotify")
    public  static void setNotify(TextView textView, boolean notify) {
        textView.setText(notify ? "This show will notified" : "This show will not notified ");
    }

    @BindingAdapter("setNotifyButton")
    public  static void setNotifyButton(Button button, boolean notify) {
        button.setText(notify ? "Disable Notification?" : "Enable Notification");
    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView view, int id)  {
        ContentResolver cp = view.getContext().getContentResolver();
//        ParcelFileDescriptor fd = cp.openInputStream()
        if (id == 0) return;
        Log.d(TAG, "[+] Getting File Image with ID " + id);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(cp, Uri.parse(FileDownloadProvider.URL + '/' + id));
            view.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
