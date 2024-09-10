package vn.edu.usth.wordpressclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import vn.edu.usth.wordpressclient.commentgroupfragments.AllTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.ApprovedTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.PendingTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.SpamTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.TrashedTabFragment;
import vn.edu.usth.wordpressclient.commentgroupfragments.UnrepliedTabFragment;

public class CommentActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);;
        EdgeToEdge.enable(this);

        Toolbar toolbar = findViewById(R.id.comment_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Comments");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        tabLayout = findViewById(R.id.comment_tab_mode);
        viewPager2 = findViewById(R.id.comment_view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new AllTabFragment(), "ALL");
        viewPagerAdapter.addFragment(new PendingTabFragment(), "PENDING");
        viewPagerAdapter.addFragment(new UnrepliedTabFragment(), "UNREPLIED");
        viewPagerAdapter.addFragment(new ApprovedTabFragment(), "APPROVED");
        viewPagerAdapter.addFragment(new SpamTabFragment(), "SPAM");
        viewPagerAdapter.addFragment(new TrashedTabFragment(), "TRASHED");
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();
    }
}