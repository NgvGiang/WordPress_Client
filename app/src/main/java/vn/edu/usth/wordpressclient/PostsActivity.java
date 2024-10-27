package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.PostsFragments.PostDraftFragment;
import vn.edu.usth.wordpressclient.PostsFragments.PostPublishedFragment;
import vn.edu.usth.wordpressclient.PostsFragments.PostScheduledFragment;
import vn.edu.usth.wordpressclient.PostsFragments.PostTrashedFragment;

public class PostsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posts);

        Intent intent = getIntent();
        domain = intent.getStringExtra("domain");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, ContentTextEditor.class);
                intent.putExtra("endpoint", "posts");
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.posts));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        tabLayout = findViewById(R.id.PostsTabLayout);
        viewPager2 = findViewById(R.id.PostviewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,domain);
        viewPagerAdapter.addFragment(new PostPublishedFragment(), getString(R.string.published));
        viewPagerAdapter.addFragment(new PostDraftFragment(), getString(R.string.drafts));
        viewPagerAdapter.addFragment(new PostScheduledFragment(), getString(R.string.schedules));
        viewPagerAdapter.addFragment(new PostTrashedFragment(), getString(R.string.trashed));
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}