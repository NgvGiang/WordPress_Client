package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.models.GetCommentsCallback;
import vn.edu.usth.wordpressclient.CommentAPIServices;

public class AllCommentViewModel extends AndroidViewModel {
    private MutableLiveData<List<Comment>> comments;
    private MutableLiveData<Boolean> isLastPage = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private int currentPage = 1;
    private static final int PER_PAGE = 100;

    public AllCommentViewModel(@NonNull Application application, MutableLiveData<List<Comment>> comments) {
        super(application);
        this.comments = comments;
    }

    public MutableLiveData<List<Comment>> getComments() {
        return comments;
    }

    public MutableLiveData<Boolean> getIsLastPage() {
        return isLastPage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void loadComments(String userDomain, boolean isInitialLoad) {
        if (isLoading.getValue() != null && isLoading.getValue()) return;
        isLoading.setValue(true);

        List<Comment> commentList = new ArrayList<>();
        CommentAPIServices.getAllCommentsFromUser(
                getApplication(), userDomain, PER_PAGE, currentPage, commentList, new GetCommentsCallback() {
                    @Override
                    public void onSuccess(List<Comment> newComments) {
                        List<Comment> currentComments = comments.getValue();
                        if (currentComments != null) {
                            currentComments.addAll(newComments);
                            currentComments.sort((o1, o2) -> {
                                LocalDateTime d1 = LocalDateTime.parse(o1.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                                LocalDateTime d2 = LocalDateTime.parse(o2.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                                return d2.compareTo(d1);
                            });
                            comments.setValue(currentComments);
                        }
                        isLoading.setValue(false);

                        if (newComments.size() < PER_PAGE) {
                            isLastPage.setValue(true);
                        } else {
                            currentPage++;
                        }
                    }

                    @Override
                    public void onError(String error) {
                        isLoading.setValue(false);
                    }
                }
        );
    }
}
