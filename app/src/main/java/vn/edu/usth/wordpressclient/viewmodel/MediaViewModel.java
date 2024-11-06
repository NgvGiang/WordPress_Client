package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.model.WebCardModel;
import vn.edu.usth.wordpressclient.repository.ContentRepository;
import vn.edu.usth.wordpressclient.repository.WebRepository;

public class MediaViewModel extends AndroidViewModel {
    //init repository
    //init live data
    private final ContentRepository mediaRepository= ContentRepository.getInstance(getApplication());
    private final MutableLiveData<ArrayList<MediaCardModel>> mediaModelsLiveData= new MutableLiveData<>();
    private final MutableLiveData<Boolean> uploadSuccessLiveData = new MutableLiveData<>();
    public MediaViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<ArrayList<MediaCardModel>> getMediaModelsLiveData() {
        return mediaModelsLiveData;
    }

    public void fetchMediaUrls(String accessToken, String domain) {
        mediaRepository.fetchMediaUrls(accessToken, domain, mediaModelsLiveData);
    }
    public void uploadImage(Uri fileUri, String accessToken, View rootview){
        mediaRepository.uploadImageToWordPress(fileUri, accessToken, rootview);
    }
}
