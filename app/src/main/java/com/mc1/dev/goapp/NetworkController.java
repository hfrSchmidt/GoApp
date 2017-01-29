package com.mc1.dev.goapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// ----------------------------------------------------------------------
// class NetworkController
//
// this class handles all http communication
// ----------------------------------------------------------------------
class NetworkController {
    private static final String LOG_TAG = NetworkController.class.getSimpleName();

    private final String SERVERURL = "localhost:8080/goserver"; // change to hosted url
    private Context mCtx;

    NetworkController(Context ctx) {
        this.mCtx = ctx;
    }

    void start() {
        NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue().start();
    }

    void stop() {
        NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue().stop();
    }

    void postMatch(String token, String JSONData) {


        String url = SERVERURL + "/match/" + token;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO do something
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO what to do on Error?
                    }
                }
        );
        NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue()
                .add(strRequest);

    }

    public void deleteMatch(String token) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(SERVERURL + "/match/" + token);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes("");
            wr.close();

        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }

        // receive response
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            // response.toString() -> full response
        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }
    }

    public void postMove(String token, String JSONData) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(SERVERURL + "/play/" + token);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(JSONData);
            wr.close();

        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }

        // catch response
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            // response.toString() -> full response message
        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }
    }

    public void getMove(String token) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(SERVERURL + "/play" + token);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(""); // -> this might not work, but on get request only the headers are needed, so what else to write?
            wr.close();

        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }

        // catch response
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            // response.toString() -> full response message
            // TODO response contains move information -> do sth with it
        } catch (Exception e) {
            e.getStackTrace();
            e.getMessage();
        }
    }

    public void retry(HttpURLConnection con, String JSONdata, int retryCount) {

        if (retryCount >= 5) {
            return;
        }

        // TODO
    }
}
