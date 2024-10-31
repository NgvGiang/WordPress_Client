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
    private final WebRepository webRepository = WebRepository.getInstance(getApplication());;
    private final MutableLiveData<ArrayList<WebCardModel>> webModelsLiveData=new MutableLiveData<>();
    private final MutableLiveData<Integer> siteNumber = new MutableLiveData<>();

    public WebViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<Integer> getSiteNumber() {
        return siteNumber;
    }
    public LiveData<ArrayList<WebCardModel>> getWebModelsLiveData() {
        return webModelsLiveData;
    }
    public void fetchSites(String accessToken) {
        webRepository.fetchSites(accessToken, webModelsLiveData,siteNumber);
    }
    public void updateSiteTitle(String accessToken, String domain, String newTitle){
        webRepository.updateSiteTitle(accessToken, domain, newTitle);
    }
}
