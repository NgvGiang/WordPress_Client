package vn.edu.usth.wordpressclient.models;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context ctx) {
        if (instance == null) {
            instance = new MySingleton(ctx.getApplicationContext());
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
