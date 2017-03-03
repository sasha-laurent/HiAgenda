package com.sashalaurent.example.testapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sasha on 02/03/2017.
 */

public class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {

    interface JSONConsumer {
        void setJSONObject(JSONObject object);
    }

    private JSONConsumer _consumer;

    public JSONAsyncTask(JSONConsumer consumer){
        _consumer = consumer;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream stream = connection.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder(2048);

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            try {
                JSONObject object = new JSONObject(responseStrBuilder.toString());
                Log.i("json_object", object.toString());
                return object;
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            finally {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(JSONObject result){
        Log.i("json_result", result.toString());
        _consumer.setJSONObject(result);
    }
}
