package vn.edu.usth.wordpressclient.view.media;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.wordpressclient.utils.DomainManager;

public class RetrofitClient {
    private static String domain = DomainManager.getInstance().getSelectedDomain();
    private static final String BASE_URL = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

