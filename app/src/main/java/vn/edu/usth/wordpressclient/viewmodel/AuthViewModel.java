package vn.edu.usth.wordpressclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import vn.edu.usth.wordpressclient.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> accessTokenLiveData = new MutableLiveData<>();
    private final AuthRepository authRepository = AuthRepository.getInstance(getApplication());
    public AuthViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getAccessTokenLiveData() {
        return accessTokenLiveData;
    }
    public void getAccessTokenByAuthorizationCode(String authorizationCode){
        authRepository.getAccessTokenByAuthorizationCode(authorizationCode, accessTokenLiveData);
    }
}
