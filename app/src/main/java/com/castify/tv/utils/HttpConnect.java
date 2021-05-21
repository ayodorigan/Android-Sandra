package com.castify.tv.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnect {

    private static final String TAG = "HttpConnect";

    public static JSONObject fetchJSON(String fetchURL) throws IOException {
        BufferedReader reader = null;
        java.net.URL url = new java.net.URL(fetchURL);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {

            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return new JSONObject(json);
        } catch (Exception e) {
            Log.e(HttpConnect.class.getSimpleName(), "JSON ERROR", e);
        }finally {
            urlConnection.disconnect();
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(HttpConnect.class.getSimpleName(), "JSON feed closed", e);
                }
            }
        }

        return null;
    }



    public static void sendLogs(Context context, String url)  {

        try {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            Log.e("beaconURLReady" , url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> Log.i(TAG, response), error -> Log.e(TAG , error.toString()));
            queue.add(stringRequest);
            queue.start();
        }catch (Exception e) {
            Log.e(TAG , "Logger", e);
        }
    }
}
