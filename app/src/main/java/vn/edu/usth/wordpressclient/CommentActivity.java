package vn.edu.usth.wordpressclient;

import android.annotation.SuppressLint;
import android.os.Bundle;

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

public class CommentActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private CommentViewPagerAdapter mCommentViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        mTabLayout = findViewById(R.id.comment_tab_mode);
        mViewPager = findViewById(R.id.comment_view_pager);
        mCommentViewPagerAdapter = new CommentViewPagerAdapter(this);
        mViewPager.setAdapter(mCommentViewPagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("ALL");
                    break;
                case 1:
                    tab.setText("PENDING");
                    break;
                case 2:
                    tab.setText("UNREPLIED");
                    break;
                case 3:
                    tab.setText("APPROVED");
                    break;
                case 4:
                    tab.setText("SPAM");
                    break;
                case 5:
                    tab.setText("TRASHED");
                    break;
            }
        }).attach();
    }
}