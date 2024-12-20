package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.model.ContentCardModel;
import vn.edu.usth.wordpressclient.repository.ContentRepository;
import vn.edu.usth.wordpressclient.utils.SingleLiveEvent;

public class ContentViewModel extends AndroidViewModel {
    private final ContentRepository contentRepository = ContentRepository.getInstance(getApplication());
    private final SingleLiveEvent<Boolean> deleteSuccessLiveData = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> createSuccessLiveData = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> restoreSuccessLiveData = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> publishSuccessLiveData = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> editSuccessLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> publishPagesArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> draftPagesArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> scheduledPagesArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> trashedPagesArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> publishPostsArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> draftPostsArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> scheduledPostsArrayLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ContentCardModel>> trashedPostsArrayLiveData = new MutableLiveData<>();
    public ContentViewModel(@NonNull Application application) {
        super(application);
    }
    public SingleLiveEvent<Boolean> getRestoreSuccessLiveData() {
        return restoreSuccessLiveData;
    }
    public SingleLiveEvent<Boolean> getCreateSuccessLiveData() {
        return createSuccessLiveData;
    }
    public SingleLiveEvent<Boolean> getDeleteSuccessLiveData() {
        return deleteSuccessLiveData;
    }
    public SingleLiveEvent<Boolean> getEditSuccessLiveData(){
        return editSuccessLiveData;
    }
    public SingleLiveEvent<Boolean> getPublishSuccessLiveData(){
        return publishSuccessLiveData;
    }

    public LiveData<ArrayList<ContentCardModel>> getPublishPagesArrayLiveData() {
        return publishPagesArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getDraftPagesArrayLiveData() {
        return draftPagesArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getScheduledPagesArrayLiveData() {
        return scheduledPagesArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getTrashedPagesArrayLiveData() {
        return trashedPagesArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getPublishPostsArrayLiveData() {
        return publishPostsArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getDraftPostsArrayLiveData() {
        return draftPostsArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getScheduledPostsArrayLiveData() {
        return scheduledPostsArrayLiveData;
    }
    public LiveData<ArrayList<ContentCardModel>> getTrashedPostsArrayLiveData() {
        return trashedPostsArrayLiveData;
    }


    //function use cross page to post, use to create page or post base on endpoint
    // endpoint: String: "pages" for pages api, "posts" for posts api
    public void createContent(String endpoint, String domain, String title, String content, String status, String date) {
        contentRepository.createContent(endpoint, domain, title, content, status, date, createSuccessLiveData);
    }
    public void fetchContent( String domain,String endpoint,String status) {
//      endpoint: String: "pages" for pages api, "posts" for posts api
        switch (endpoint) {
            case "pages":

            switch (status) {
                case "publish":
                    contentRepository.fetchContent(domain, endpoint, "publish", publishPagesArrayLiveData);
                    break;
                case "draft":
                    contentRepository.fetchContent(domain, endpoint, "draft", draftPagesArrayLiveData);
                    break;
                case "schedule":
                    contentRepository.fetchContent(domain, endpoint, "future", scheduledPagesArrayLiveData);
                    break;
                case "trash":
                    contentRepository.fetchContent(domain, endpoint, "trash", trashedPagesArrayLiveData);
                    break;
            }
            case "posts":
                switch (status) {
                    case "publish":
                        contentRepository.fetchContent(domain, endpoint, "publish", publishPostsArrayLiveData);
                        break;
                    case "draft":
                        contentRepository.fetchContent(domain, endpoint, "draft", draftPostsArrayLiveData);
                        break;
                    case "schedule":
                        contentRepository.fetchContent(domain, endpoint, "future", scheduledPostsArrayLiveData);
                        break;
                    case "trash":
                        contentRepository.fetchContent(domain, endpoint, "trash", trashedPostsArrayLiveData);
                        break;
                }
                break;
        }
    }

    public void deleteContent(String endpoint, String domain, int id) {
        contentRepository.deleteContent(endpoint, domain, id, deleteSuccessLiveData);
    }
    public void restoreContent(String endpoint, String domain, int id) {
        contentRepository.restoreContent(endpoint, domain, id, restoreSuccessLiveData);
    }
    public void trashContent(String endpoint, String domain, int id) {
        contentRepository.trashContent(endpoint, domain, id, deleteSuccessLiveData);
    }
    public void editContent(String endpoint, String domain, int id, String title, String content, String status, String date) {
        contentRepository.editContent(endpoint, domain, id, title, content, status, date, editSuccessLiveData);
    }
    public void publishContent(String endpoint, String domain, int id) {
        contentRepository.publishContent(endpoint, domain, id, publishSuccessLiveData);
    }
}
