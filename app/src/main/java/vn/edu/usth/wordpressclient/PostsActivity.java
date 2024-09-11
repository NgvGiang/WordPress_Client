package vn.edu.usth.wordpressclient;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.PostsFragments.DraftPostFragment;
import vn.edu.usth.wordpressclient.PostsFragments.PublishedPostFragment;
import vn.edu.usth.wordpressclient.PostsFragments.ScheduledPostFragment;
import vn.edu.usth.wordpressclient.PostsFragments.TrashedPostFragment;

public class PostsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Posts");
        tabLayout = findViewById(R.id.PostsTabLayout);
        viewPager2 = findViewById(R.id.PostviewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new PublishedPostFragment(), "PUBLISHED");
        viewPagerAdapter.addFragment(new DraftPostFragment(), "DRAFTS");
        viewPagerAdapter.addFragment(new ScheduledPostFragment(), "SCHEDULED");
        viewPagerAdapter.addFragment(new TrashedPostFragment(), "TRASHED");
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}