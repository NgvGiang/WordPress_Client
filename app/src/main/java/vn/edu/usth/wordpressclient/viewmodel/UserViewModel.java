package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import vn.edu.usth.wordpressclient.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository = UserRepository.getInstance(getApplication());
    private final MutableLiveData<JSONObject> userInfoLiveData = new MutableLiveData<>();
    public UserViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<JSONObject> getUserInfoLiveData(){
        return userInfoLiveData;
    }
    public void getUserInfo(String accessToken) {
        if (userInfoLiveData.getValue() == null) {
            userRepository.getUserInfo(accessToken, userInfoLiveData);
        }
    }
}
