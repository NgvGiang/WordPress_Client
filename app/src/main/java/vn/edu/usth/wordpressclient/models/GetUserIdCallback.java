package vn.edu.usth.wordpressclient.models;

import java.util.List;

public interface GetUserIdCallback {
    void onSuccess(List<Integer> longs);
    void onError(String error);
}
