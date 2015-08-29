package com.plainit.tockto.util;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import com.plainit.tockto.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by newpouy on 15. 8. 28.
 */
public class UseServerREST extends AsyncTask {
    private static final String TAG = "UseServerREST";

    @Override
    protected Object doInBackground(Object[] params) {
        Log.d(TAG, "do in background "+params[0].toString());
        getUserInfo(params[0].toString());
        return null;
    }

    public User getUserInfo(String kakaoID) {
        try {
            kakaoID = "testID";
            URL url = new URL("http://52.69.73.195:8082/api/users/"+kakaoID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            Log.d(TAG, "Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                Log.d(TAG, output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        User user = new User();
        return user;
    }
    public boolean registOrNot(){
        return true;
    }
}
