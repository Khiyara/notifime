package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common;

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
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloadProvider extends ContentProvider {
    private static final String TAG = "File Download Provider";
    static final String PROVIDER_NAME = "id.ac.ui.cs.mobileprogramming.rizkhiph.notifime";
    static final String BASE_PATH =  "/images";
    public static final String URL = "content://" + PROVIDER_NAME + BASE_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);

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
//        InputStream is = download(url);

//        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        String basePath = uri.getPath();
//        Log.d(TAG, "[+] Bitmap " + bitmap.toString());
//        saveBitmap(context, uri, basePath, key, bitmap, false);
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
    protected static void saveBitmap(String basePath, Bitmap value, boolean updateDatabase) {

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

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "[-] Could not save image to " + uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "[-] Could not save image to " + uri.toString());
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
        String url;
        Context context;
        Uri uri;
        int key;

        private final String TAG = "Request Task";

        public RequestTask(FileDownloadProvider provider, String url, Context context, Uri uri, int key) {
            this.provider = provider;
            this.url = url;
            this.context = context;
            this.uri = uri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.i(TAG, "[+] Forging GET Request to " + url);
                URL newUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection) newUrl.openConnection();

                InputStream output = con.getInputStream();
                Log.d(TAG, "[+] Success downloading thumbnail");
                Log.d(TAG, "[+] " + output.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(output);
                System.out.println(bitmap);
                String basePath = uri.getPath();
                Log.d(TAG, "[+] Bitmap " + bitmap.toString());

                saveBitmap(context, uri, basePath, key, bitmap, false);
            } catch (Exception e) {
                Log.e(TAG, "[-] Something went wrong");
                Log.e(TAG, e.toString());
            }
            return null;
        }

    }
}
