package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.MediaFragments.MediaAllFragment;
import vn.edu.usth.wordpressclient.MediaFragments.MediaDocumentsFragment;
import vn.edu.usth.wordpressclient.MediaFragments.MediaVideosFragment;
import vn.edu.usth.wordpressclient.MediaFragments.MediaImagesFragment;

public class MediaActivity extends AppCompatActivity {

    private TabLayout MediatabLayout;
    private ViewPager2 MediaviewPager2;
    String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        domain = intent.getStringExtra("domain");
        setContentView(R.layout.activity_media);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show());


        Toolbar toolbar = findViewById(R.id.Mediatoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.WordPress_Media));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        
        MediatabLayout = findViewById(R.id.MediatabLayout);
        MediaviewPager2 = findViewById(R.id.MediaviewPager);
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
}