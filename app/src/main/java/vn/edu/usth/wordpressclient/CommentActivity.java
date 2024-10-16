package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.commentgroupfragments.AllCommentsFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.ApprovedCommentsFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.PendingCommentsFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.SpamCommentsFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.TrashedCommentsFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.UnrepliedCommentsFragment;

public class CommentActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);;
        EdgeToEdge.enable(this);
        Intent intent = getIntent();
        domain = intent.getStringExtra("domain");

        Toolbar toolbar = findViewById(R.id.comment_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.comments));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        tabLayout = findViewById(R.id.comment_tab_mode);
        viewPager2 = findViewById(R.id.comment_view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,domain);
        viewPagerAdapter.addFragment(new AllCommentsFragment(), getString(R.string.ALL));
        viewPagerAdapter.addFragment(new PendingCommentsFragment(), getString(R.string.pending));
        viewPagerAdapter.addFragment(new UnrepliedCommentsFragment(), getString(R.string.unreplied));
        viewPagerAdapter.addFragment(new ApprovedCommentsFragment(), getString(R.string.approved));
        viewPagerAdapter.addFragment(new SpamCommentsFragment(), getString(R.string.spam));
        viewPagerAdapter.addFragment(new TrashedCommentsFragment(), getString(R.string.trashed));
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}