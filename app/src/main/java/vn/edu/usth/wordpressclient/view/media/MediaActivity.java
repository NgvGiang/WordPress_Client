package vn.edu.usth.wordpressclient.view.media;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.ViewPagerAdapter;

public class MediaActivity extends AppCompatActivity {

    private TabLayout MediatabLayout;
    private ViewPager2 MediaviewPager2;
    private String domain;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        domain = intent.getStringExtra("domain");
        setContentView(R.layout.activity_media);

        FloatingActionButton fab = findViewById(R.id.fab);

//        fab.setOnClickListener(view -> Snackbar.make(findViewById(android.R.id.content), getString(R.string.under_dev), Snackbar.LENGTH_SHORT).show());
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData(); // Nhận URI của ảnh đã chọn
                        String authToken = "Bearer " + SessionManager.getInstance(this).getAccessToken(); // Thay bằng token của bạn
                        String description = "Please Work, Im fucking tired of this shit"; // Mô tả tùy chọn cho ảnh

                        // Gọi hàm upload ảnh lên WordPress
                        uploadImageToWordPress(fileUri, authToken, description);
                    }
                }
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickerLauncher.launch(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.Mediatoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.WordPress_Media));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });



        MediatabLayout = findViewById(R.id.MediatabLayout);
        MediaviewPager2 = findViewById(R.id.MediaViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, domain);
        viewPagerAdapter.addFragment(new MediaAllFragment(), getString(R.string.ALL));
        viewPagerAdapter.addFragment(new MediaImagesFragment(), getString(R.string.images));
        viewPagerAdapter.addFragment(new MediaDocumentsFragment(), getString(R.string.documents));
        viewPagerAdapter.addFragment(new MediaVideosFragment(), getString(R.string.videos));
        MediaviewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(MediatabLayout, MediaviewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();

    }

    private void uploadImageToWordPress(Uri fileUri, String authToken, String description) {
        String filePath = getRealPathFromURI(fileUri);
        File file = new File(filePath);

        // Chuẩn bị Multipart cho ảnh
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Chuẩn bị mô tả ảnh
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), description);

        WordPressApi apiService = RetrofitClient.getClient().create(WordPressApi.class);

        Call<ResponseBody> call = apiService.uploadImage(authToken, body, descBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    Log.d("Upload", "Success: " + response.message());

                } else {
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    Log.e("Upload", "Failure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Upload error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}