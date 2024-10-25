package vn.edu.usth.wordpressclient;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.wordpressclient.models.Comment;

public class CommentViewModel extends ViewModel {
    private MutableLiveData<List<Comment>> commentsLiveData;
    private List<Comment> comments;

    public CommentViewModel() {
        commentsLiveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        comments = new ArrayList<>();
        commentsLiveData.setValue(comments);
    }

    public MutableLiveData<List<Comment>> getCommentsLiveData() {
        return commentsLiveData;
    }
}
