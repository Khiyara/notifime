package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestApi extends Service {

    private final String TAG = "Request API";
    public RequestApi() {}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getRequest(String link, RequestInterface service, final int type) {
        Log.i(TAG, "[+] Creating Request to API Server");
        RequestTask task = (RequestTask) new RequestTask(new RequestTask.AsyncResponse() {
            @Override
            public void processFinish(String output, RequestInterface service) {
                Log.i(TAG, "[+] Getting Result: " + output);
                service.setResponseText(output, type);
            }
        }, link, service).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static class RequestTask extends AsyncTask<Void, Void, String> {
        String link;
        RequestInterface service;
        AsyncResponse delegate = null;

        private final String TAG = "Request Task";

        public RequestTask(AsyncResponse delegate, String link, RequestInterface service) {
            this.link = link;
            this.delegate = delegate;
            this.service = service;
        }

        public interface AsyncResponse {
            void processFinish(String output, RequestInterface service);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Log.i(TAG, "[+] Forging GET Request to " + this.link);
                URL url = new URL(this.link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "[+] Success Fetching Data");
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    for (String line = in.readLine(); line != null; line = in.readLine()){
                        response.append(line);
                    }
                    in.close();
                    return response.toString();
                } else {
                    Log.i(TAG, "[-] Cannot get resource");
                    Log.i(TAG, "[-] Response code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e(TAG, "[-] Something went wrong");
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            delegate.processFinish(response, service);
        }
    }
}
