package vn.edu.usth.wordpressclient.models;

import java.util.List;

public interface GetCommentsCallback {
    void onSuccess(List<Comment> newComments);
    void onError(String error);
}
