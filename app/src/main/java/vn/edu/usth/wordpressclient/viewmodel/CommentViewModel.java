package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Map;

import vn.edu.usth.wordpressclient.model.CommentCardModel;
import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.repository.CommentRepository;
import vn.edu.usth.wordpressclient.repository.ContentRepository;

public class CommentViewModel extends AndroidViewModel {
    private final CommentRepository commentRepository= CommentRepository.getInstance(getApplication());
//    private final MutableLiveData<ArrayList<CommentCardModel>> allCommentModelsLiveData= new MutableLiveData<>();
//    private final MutableLiveData<ArrayList<CommentCardModel>> pendingCommentModelsLiveData= new MutableLiveData<>();
//    private final MutableLiveData<ArrayList<CommentCardModel>> approvedCommentModelsLiveData= new MutableLiveData<>();
//    private final MutableLiveData<ArrayList<CommentCardModel>> spamCommentModelsLiveData= new MutableLiveData<>();
//    private final MutableLiveData<ArrayList<CommentCardModel>> trashCommentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CommentCardModel>> commentModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<String> successLiveData= new MutableLiveData<>();
    private final MutableLiveData<Boolean> statusLiveData= new MutableLiveData<>();
    public CommentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<CommentCardModel>> getCommentModelsLiveData() {
        return commentModelsLiveData;
    }

    public MutableLiveData<String> getSuccessLiveData() {
        return successLiveData;
    }

    public MutableLiveData<Boolean> getStatusLiveData() {
        return statusLiveData;
    }
//    public MutableLiveData<ArrayList<CommentCardModel>> getAllCommentModelsLiveData() {
//        return allCommentModelsLiveData;
//    }
//
//    public MutableLiveData<ArrayList<CommentCardModel>> getPendingCommentModelsLiveData() {
//        return pendingCommentModelsLiveData;
//    }
//
//    public MutableLiveData<ArrayList<CommentCardModel>> getApprovedCommentModelsLiveData() {
//        return approvedCommentModelsLiveData;
//    }
//
//    public MutableLiveData<ArrayList<CommentCardModel>> getSpamCommentModelsLiveData() {
//        return spamCommentModelsLiveData;
//    }
//
//    public MutableLiveData<ArrayList<CommentCardModel>> getTrashCommentModelsLiveData() {
//        return trashCommentModelsLiveData;
//    }
//
//    public void getComments(String accessToken, String domain, String status) {
//        if (status.equals("all")) {
//            commentRepository.getComments(accessToken, domain, 100, status, allCommentModelsLiveData);
//            return;
//        }
//        if (status.equals("hold")) {
//            commentRepository.getComments(accessToken, domain, 100, status, pendingCommentModelsLiveData);
//            return;
//        }
//        if (status.equals("approve")) {
//            commentRepository.getComments(accessToken, domain, 100, status, approvedCommentModelsLiveData);
//            return;
//        }
//        if (status.equals("spam")) {
//            commentRepository.getComments(accessToken, domain, 100, status, spamCommentModelsLiveData);
//            return;
//        }
//        if (status.equals("trash")) {
//            commentRepository.getComments(accessToken, domain, 100, status, trashCommentModelsLiveData);
//            return;
//        }
//
//    }

    public void getComments(String status) {
        commentRepository.getComments(100, status, commentModelsLiveData);
    }

    public void replyComment(String domain, String content, Long parent, Long post) {
        commentRepository.replyComment(domain, content, parent, post, successLiveData);
    }

    public void updateCommentStatus(Long id, String status) {
        commentRepository.updateCommentStatus(id, status, statusLiveData);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteComment(id, statusLiveData);
    }
}
