package vn.edu.usth.wordpressclient;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import vn.edu.usth.wordpressclient.models.GetUserIdCallback;

public class UserIdManager {
    private static UserIdManager instance;
    private static Long userId;
    private static final Object lock = new Object();

    // Private constructor to prevent instantiation
    private UserIdManager() {}

    // Synchronized getInstance method to enforce thread safety
    public static synchronized UserIdManager getInstance() {
        if (instance == null) {
            instance = new UserIdManager();
        }
        return instance;
    }

    public static Long getUserId() {
        return userId;
    }

    public void setUserId(Context context, String domain, GetUserIdCallback callback) {
        if (userId != null) {
            callback.onSuccess(Collections.singletonList(userId.intValue()));
            return;
        }

        synchronized (lock) {
            if (userId == null) {
                CommentAPIServices.getUserId(context, domain, new GetUserIdCallback() {
                    @Override
                    public void onSuccess(List<Integer> longs) {
                        userId = (long) longs.get(0); // Cache the retrieved userId
                        callback.onSuccess(Collections.singletonList(userId.intValue()));
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError(error);
                    }
                });
            } else {
                callback.onSuccess(Collections.singletonList(userId.intValue()));
            }
        }
    }
}

