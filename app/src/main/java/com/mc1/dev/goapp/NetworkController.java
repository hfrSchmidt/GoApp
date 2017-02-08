package com.mc1.dev.goapp;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

// ----------------------------------------------------------------------
// class NetworkController
//
// this class handles all http communication
// ----------------------------------------------------------------------
class NetworkController {
    private static final String LOG_TAG = NetworkController.class.getSimpleName();

    private final String SERVERURL = "http://10.0.2.2:8080/goserver"; // change to hosted url
    // specifies the amount of retries after timeout
    private final int RETRYCOUNT = 5;
    // timeout in seconds
    private final int TIMEOUT = 20;
    private Context mCtx;
    private JsonObjectRequest jsonRequest;

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
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(JSONData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jObj != null) {
            jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    error.printStackTrace();
                }
            }
            );
            // no exponential backoff is used
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT * 1000, RETRYCOUNT, 1.0f));
            NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue()
                    .add(jsonRequest);
        }
    }

    public void deleteMatch(String token) {
        String url = SERVERURL + "/match/" + token;

        jsonRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response on Delete:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        }
        );
        // no exponential backoff is used
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT * 1000, RETRYCOUNT, 1.0f));
        NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue()
                .add(jsonRequest);
    }

    public void postMove(String token, String JSONData) {
        String url = SERVERURL + "/play/" + token;

        JSONObject jObj = null;
        try {
            jObj = new JSONObject(JSONData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jObj != null) {
            jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }
            );
            // no exponential backoff is used
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT * 1000, RETRYCOUNT, 1.0f));
            NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue()
                    .add(jsonRequest);
        }
    }

    public void getMove(String token) {
        String url = SERVERURL + "/play/" + token;

        jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
        // no exponential backoff is used
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT * 1000, RETRYCOUNT, 1.0f));
        NetworkRequestQueue.getInstance(mCtx.getApplicationContext()).getRequestQueue()
                .add(jsonRequest);
    }
}
