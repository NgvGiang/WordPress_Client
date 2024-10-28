package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.model.WebCardModel;
import vn.edu.usth.wordpressclient.repository.WebRepository;

public class WebViewModel extends AndroidViewModel {
    //init repository
    //init live data
    private final WebRepository webRepository ;
    private final MutableLiveData<ArrayList<WebCardModel>> webModelsLiveData;
    public WebViewModel(@NonNull Application application) {
        super(application);
        webRepository = WebRepository.getInstance(application);
        webModelsLiveData = new MutableLiveData<>();
    }
    public LiveData<ArrayList<WebCardModel>> getWebModelsLiveData() {
        return webModelsLiveData;
    }
    public void fetchSites(String accessToken) {
        webRepository.fetchSites(accessToken, webModelsLiveData);
    }
}
