package vn.edu.usth.wordpressclient.view.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.view.ContentTextEditor;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.view.adapter.ViewPagerAdapter;

public class PagesActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
//    private String domain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);;
        EdgeToEdge.enable(this);
        // Find the Toolbar and set it as the ActionBar
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PagesActivity.this, ContentTextEditor.class);
                intent.putExtra("endpoint", "pages");
                intent.putExtra("create","publish");
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.page));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        tabLayout = findViewById(R.id.PagetabLayout);
        viewPager2 = findViewById(R.id.PageviewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new PagePublishedFragment(), getString(R.string.published));
        viewPagerAdapter.addFragment(new PageDraftFragment(), getString(R.string.drafts));
        viewPagerAdapter.addFragment(new PageScheduledFragment(), getString(R.string.schedules));
        viewPagerAdapter.addFragment(new PageTrashedFragment(), getString(R.string.trashed));
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}