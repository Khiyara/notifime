package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.main.activity.implementation.MainActivity;

public class FileDownloadProvider extends FileProvider {
    private static final String TAG = "File Download Provider";
    static final String PROVIDER_NAME = "id.ac.ui.cs.mobileprogramming.rizkhiph.notifime";
    static final String BASE_PATH =  "/images";
    public static final String URL = "content://" + PROVIDER_NAME + BASE_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/notifime";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        try {
            downloadIndirect(contentValues.getAsString("URL"), contentValues.getAsInteger("KEY"), getContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
//        return super.openFile(uri, mode);
        ContextWrapper cw = new ContextWrapper(getContext());

//        File directory = cw.getDir(BASE_PATH, Context.MODE_PRIVATE);
        File directory = getContext().getFilesDir();
        directory.mkdirs();

        long id = ContentUris.parseId(uri);
        File path = new File(directory, String.valueOf(id));

        int iMode = 0;
        if (mode.contains("w")) {
            iMode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
            if (!path.exists()) {
                try {
                    path.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (mode.contains("r")) {
            iMode |= ParcelFileDescriptor.MODE_READ_ONLY;
        }
        if (mode.contains("+")) {
            iMode |= ParcelFileDescriptor.MODE_APPEND;
        }

        return ParcelFileDescriptor.open(path, iMode);
    }

    private void downloadIndirect(String url, int key, Context context, Uri uri)
            throws MalformedURLException, IOException {
        Log.i(TAG, "[+] Creating Request to API Server to download thumbnail");
        RequestTask task = (RequestTask) new RequestTask(this, url, context, uri, key)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private InputStream download(String url) throws MalformedURLException,
            IOException {

        URL newUrl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) newUrl.openConnection();
        return con.getInputStream();
    }

    public static InputStream openInputStream(Context context, Uri contentUri, int key) throws FileNotFoundException {
        Uri uri = ContentUris.withAppendedId(contentUri, key);

        return context.getContentResolver().openInputStream(uri);
    }

    public static OutputStream openOutputStream(Context context, Uri contentUri, int key) throws FileNotFoundException {
        Uri uri = ContentUris.withAppendedId(contentUri, key);

        return context.getContentResolver().openOutputStream(uri);
    }

    protected static boolean deleteFile(Uri uri, String basePath,
                                        Context context, boolean notifyChange) {

        if (uri == null || context == null)
            return false;

        ContextWrapper cw = new ContextWrapper(context);

        File directory = cw.getDir(basePath, Context.MODE_PRIVATE);
        String id = uri.getLastPathSegment();
        File file = new File(directory, id);

        if (file.exists()) {
            boolean result = file.delete();
            if (notifyChange && result)
                context.getContentResolver().notifyChange(uri, null);

            if (result)
                Log.d(TAG, "[+] Deleted file for " + uri.toString());
            return result;
        }
        return false;
    }

    protected static void saveBitmap(Context context, Uri contentUri,
                                     String basePath, int key, Bitmap value, boolean updateDatabase) {

        Uri uri = ContentUris.withAppendedId(contentUri, key);
        Log.d(TAG, "[+] Uri Path " + uri.getPath());
        try {
            if (value == null) {
                deleteFile(uri, basePath, context, true);
                return;
            }

            OutputStream outStream;
            try {
                outStream = openOutputStream(context, contentUri, key);
                // SEARCH SAVE BITMAP ANDROID
                saveToStream(value, outStream,
                        Bitmap.CompressFormat.PNG);
                outStream.close();
                Log.d(TAG,
                        "[+] Image (" + value.getWidth() + "x" + value.getHeight()
                                + "pixels) saved to " + uri.toString());
                outStream = new FileOutputStream(EXTERNAL_STORAGE + "/" + key + ".bmp");
                saveToStream(value, outStream,
                        Bitmap.CompressFormat.PNG);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if (updateDatabase) {
                ContentValues values = new ContentValues();
                context.getContentResolver().update(uri, values, null, null);
            }
        }
    }

    private static void saveToStream(Bitmap value, OutputStream outStream, Bitmap.CompressFormat format) {
        value.compress(format, 100, outStream);
    }

    private static class RequestTask extends AsyncTask<Void, Void, Void> {
        FileDownloadProvider provider;
        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;
        String url;
        Context context;
        Uri uri;
        int key;
        int id = 8765;

        private final String TAG = "Request Task";

        public RequestTask(FileDownloadProvider provider, String url, Context context, Uri uri, int key) {
            this.provider = provider;
            this.url = url;
            this.context = context;
            this.uri = uri;
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Creating Notification manager on download");
            String channelId = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channelId = createNotificationChannel(context);
            }
            mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context, channelId);
            mBuilder.setContentTitle("File Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.logo_only);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.i(TAG, "[+] Forging GET Request to " + url);
                URL newUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection) newUrl.openConnection();

                int fileLength = con.getContentLength();
                InputStream output = con.getInputStream();
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = output.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        output.close();
                        return null;
                    }
                    total += count;
                    System.out.println(total);
                    System.out.println(fileLength);
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                }
                Log.d(TAG, "[+] Success downloading thumbnail");
                Log.d(TAG, "[+] " + output.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(output);
                String basePath = uri.getPath();

                saveBitmap(context, uri, basePath, key, bitmap, false);
            } catch (Exception e) {
                Log.e(TAG, "[-] Something went wrong");
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("Download is finished");
            mBuilder.setContentText("Download completed")
                    .setProgress(0,0,false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        private void publishProgress(int progress) {
            System.out.println("Update progress");
            mBuilder.setProgress(100, progress, false);
            mNotifyManager.notify(id, mBuilder.build());

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public String createNotificationChannel(Context context) {
            String channelId = "DownloadNotification_1";
            String channelName = "DownloadNotification";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription("Download");
            notificationChannel.enableVibration(false);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        }

    }
}
