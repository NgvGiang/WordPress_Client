package vn.edu.usth.wordpressclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    private static SessionManager instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    // Shared preferences file name and keys
    private static final String PREF_NAME = "user_session";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    // Constructor
    private SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }
    // Create login session
    public void createLoginSession(String accessToken) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply(); // Save the session
    }

    // Check login status
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGGED_IN, false);
    }

    // Get stored access token
    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    // Logout user
    public void logoutUser() {
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, FirstLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear all activities on top of this one
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Start a new task
        context.startActivity(intent);
    }
}