package vn.edu.usth.wordpressclient.view.media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.ViewPagerAdapter;
import vn.edu.usth.wordpressclient.viewmodel.MediaViewModel;

public class MediaActivity extends AppCompatActivity {
    private ProgressBar toolbarProgressBar;
    private TabLayout MediatabLayout;
    private ViewPager2 MediaviewPager2;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private MediaViewModel mediaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        toolbarProgressBar = findViewById(R.id.toolbar_progress_bar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        String authToken = "Bearer " + SessionManager.getInstance(this).getAccessToken();
                        View rootView = findViewById(android.R.id.content);
                        // Gọi hàm upload ảnh lên WordPress
                        mediaViewModel.uploadImage(fileUri, authToken, rootView);
                    }
                    if (result.getResultCode() == Activity.RESULT_CANCELED){
                        toolbarProgressBar.setVisibility(View.GONE);
                    }
                }
        );
        mediaViewModel.getUploadSuccessLiveData().observe(this, success ->{

            if(success){
                Snackbar.make(findViewById(android.R.id.content),R.string.upload_image_successfully,2500).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    // Refresh RecyclerView
                    MediaAllFragment mediaAllFragment = (MediaAllFragment) getSupportFragmentManager().findFragmentByTag("f0");
                    if (mediaAllFragment != null) {
                        mediaAllFragment.onResume();
                        toolbarProgressBar.setVisibility(View.GONE);
                    }
                }, 2500);
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.upload_image_failed, Snackbar.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickerLauncher.launch(intent);
                toolbarProgressBar.setVisibility(View.VISIBLE);
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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new MediaAllFragment(), getString(R.string.ALL));
        viewPagerAdapter.addFragment(new MediaImagesFragment(), getString(R.string.images));
        viewPagerAdapter.addFragment(new MediaDocumentsFragment(), getString(R.string.documents));
        viewPagerAdapter.addFragment(new MediaVideosFragment(), getString(R.string.videos));
        MediaviewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(MediatabLayout, MediaviewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}