package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.List;

import vn.edu.usth.wordpressclient.repository.ContentRepository;

public class ContentViewModel extends AndroidViewModel {
    private final ContentRepository contentRepository = ContentRepository.getInstance(getApplication());
    private final MutableLiveData<Boolean> successLiveData= new MutableLiveData<>();
//    private final MutableLiveData<JSONObject> contentLiveData = new MutableLiveData<>();
    public ContentViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Boolean> getSuccessLiveData() {
        return successLiveData;
    }
    //function use cross page to post, use to create page or post base on endpoint
    // endpoint: String: "pages" for pages api, "posts" for posts api
    public void createContent(String endpoint, String domain, String title, String content, String status, String date) {
        contentRepository.createContent(endpoint, domain, title, content, status, date, successLiveData);
    }
    public void fetchContent( String domain,String endpoint,String status) {
//        contentRepository.fetchContent(domain,endpoint,status,contentLiveData);
    }

}
