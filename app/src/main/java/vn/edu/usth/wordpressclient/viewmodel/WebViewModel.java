package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.wordpressclient.Web_card_model;
import vn.edu.usth.wordpressclient.repository.WebRepository;

public class WebViewModel extends AndroidViewModel {
    //init repository
    //init live data
    private final WebRepository webRepository ;
    private final MutableLiveData<ArrayList<Web_card_model>> webModelsLiveData;
    public WebViewModel(@NonNull Application application) {
        super(application);
        webRepository = WebRepository.getInstance(application);
        webModelsLiveData = new MutableLiveData<>();
    }
    public LiveData<ArrayList<Web_card_model>> getWebModelsLiveData() {
        return webModelsLiveData;
    }
    public void fetchSites(String accessToken) {
        webRepository.fetchSites(accessToken, webModelsLiveData);
    }
}
