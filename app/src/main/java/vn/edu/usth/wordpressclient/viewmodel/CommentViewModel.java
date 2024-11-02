package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.model.CommentCardModel;
import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.repository.CommentRepository;
import vn.edu.usth.wordpressclient.repository.ContentRepository;

public class CommentViewModel extends AndroidViewModel {
    private final CommentRepository commentRepository= CommentRepository.getInstance(getApplication());
    private final MutableLiveData<ArrayList<CommentCardModel>> commentModelsLiveData= new MutableLiveData<>();
    public CommentViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<ArrayList<CommentCardModel>> getCommentModelsLiveData() {
        return commentModelsLiveData;
    }
    public void getComments(String accessToken, String domain) {
        commentRepository.getComments(accessToken, domain, 100, "all", commentModelsLiveData);
    }
}
