package vn.edu.usth.wordpressclient.view.media;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.view.adapter.ViewPagerAdapter;

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

        fab.setOnClickListener(view -> Snackbar.make(findViewById(android.R.id.content), getString(R.string.under_dev), Snackbar.LENGTH_SHORT).show());


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