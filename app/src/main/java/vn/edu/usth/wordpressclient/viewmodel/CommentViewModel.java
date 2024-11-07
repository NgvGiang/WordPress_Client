package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.usth.wordpressclient.model.CommentCardModel;
import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.repository.CommentRepository;
import vn.edu.usth.wordpressclient.repository.ContentRepository;

public class CommentViewModel extends AndroidViewModel {
    private final CommentRepository commentRepository= CommentRepository.getInstance(getApplication());
    private final MutableLiveData<ArrayList<CommentCardModel>> allCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CommentCardModel>> pendingCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CommentCardModel>> approvedCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CommentCardModel>> spamCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CommentCardModel>> trashCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CommentCardModel>> unrepliedCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<JSONObject> postOfComment = new MutableLiveData<>();
    private final MutableLiveData<String> successLiveData= new MutableLiveData<>();
    private final MutableLiveData<Boolean> statusLiveData= new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteLiveData= new MutableLiveData<>();

    public CommentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<JSONObject> getPostOfComment() {
        return postOfComment;
    }

    public MutableLiveData<String> getSuccessLiveData() {
        return successLiveData;
    }

    public MutableLiveData<Boolean> getStatusLiveData() {
        return statusLiveData;
    }

    public MutableLiveData<Boolean> getDeleteLiveData() {
        return deleteLiveData;
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getAllCommentModelsLiveData() {
        return allCommentModelsLiveData;
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getPendingCommentModelsLiveData() {
        return pendingCommentModelsLiveData;
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getUnrepliedCommentModelsLiveData() {
        return unrepliedCommentModelsLiveData;
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getApprovedCommentModelsLiveData() {
        return approvedCommentModelsLiveData;
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getSpamCommentModelsLiveData() {
        return spamCommentModelsLiveData;
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getTrashCommentModelsLiveData() {
        return trashCommentModelsLiveData;
    }

    public void getComments(String status, String accessToken, String domain) {
        switch (status) {
            case "all":
                commentRepository.getComments(accessToken, domain, 100, status, allCommentModelsLiveData);
                break;
            case "hold":
                commentRepository.getComments(accessToken, domain,100, status, pendingCommentModelsLiveData);
                break;
            case "approve":
                commentRepository.getComments( accessToken, domain,100, status, approvedCommentModelsLiveData);
                break;
            case "spam":
                commentRepository.getComments( accessToken, domain,100, status, spamCommentModelsLiveData);
                break;
            case "trash":
                commentRepository.getComments(accessToken, domain,100, status, trashCommentModelsLiveData);
                break;
        }
    }

    public void replyComment(String domain, String content, int parent, int post) {
        commentRepository.replyComment(domain, content, parent, post, successLiveData);
    }

    public void updateCommentStatus(int id, String status) {
        commentRepository.updateCommentStatus(id, status, statusLiveData);
    }

    public void deleteComment(int id) {
        commentRepository.deleteComment(id, deleteLiveData);
    }

    public void getUnrepliedComment(String accessToken, String domain, int id) {
        commentRepository.getUnrepliedComment(accessToken, domain, 100, id, unrepliedCommentModelsLiveData);
    }

    public void getPostOfComment(int id) {
        commentRepository.getPostOfComment(id, postOfComment);
    }
}
