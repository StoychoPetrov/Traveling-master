package com.example.stoycho.traveling.tasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Stoycho on 12/14/2016.
 */

public class Request extends AsyncTask<Void,Void,String> {

    private String mUrl;

    public Request(String url)
    {
        mUrl = url;
    }

    @Override
    protected String doInBackground(Void... params) {

        String dataGetChanges = "";

        URL urlGetData;
        try {
            urlGetData = new URL(mUrl);
            HttpURLConnection urlConnectionGetChanges = (HttpURLConnection) urlGetData.openConnection();

            BufferedReader bufferedReaderGetChanges = new BufferedReader(new InputStreamReader(urlConnectionGetChanges.getInputStream()));

            String nextGetChanges = bufferedReaderGetChanges.readLine();
            while (nextGetChanges != null) {
                dataGetChanges += nextGetChanges;
                nextGetChanges = bufferedReaderGetChanges.readLine();
            }
            urlConnectionGetChanges.disconnect();
            return dataGetChanges;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
