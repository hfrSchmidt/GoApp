package com.mc1.dev.goapp;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

// ----------------------------------------------------------------------
// class NetworkRequestQueue
//
// this Singleton class contains the handling of network requests through
// volley
// ----------------------------------------------------------------------

class NetworkRequestQueue {
    private static NetworkRequestQueue mInstance;
    private RequestQueue mReqQueue;
    private Context mCtx;

    private NetworkRequestQueue(Context ctx) {
        this.mCtx = ctx;
        mReqQueue = getRequestQueue();
    }

    public static synchronized NetworkRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkRequestQueue(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    RequestQueue getRequestQueue() {
        if (mReqQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 1024 * 1024); // 1MB Cache
            Network network = new BasicNetwork(new HurlStack());

            mReqQueue = new RequestQueue(cache, network);
        }
        return mReqQueue;
    }

}
