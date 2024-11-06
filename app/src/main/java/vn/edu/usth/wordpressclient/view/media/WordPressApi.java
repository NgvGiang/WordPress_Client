package vn.edu.usth.wordpressclient.view.media;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WordPressApi {
    @Multipart
    @POST("media")
    Call<ResponseBody> uploadImage(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part file
    );
}
