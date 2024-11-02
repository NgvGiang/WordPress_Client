package vn.edu.usth.wordpressclient.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class QueueManager {
    private static QueueManager instance;
    private RequestQueue requestQueue;
    private static Context context;

    private QueueManager(Context ctx) {
        context = ctx.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized QueueManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new QueueManager(ctx.getApplicationContext());
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
