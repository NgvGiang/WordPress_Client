package vn.edu.usth.wordpressclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import vn.edu.usth.wordpressclient.models.Comment;

public class CommentActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    String domain;
    ActivityResultLauncher<Intent> commentDetailLauncher;
    AllCommentsFragment allCommentsFragment;
    PendingCommentsFragment pendingCommentsFragment;
    UnrepliedCommentsFragment unrepliedCommentsFragment;
    ApprovedCommentsFragment approvedCommentsFragment;
    SpamCommentsFragment spamCommentsFragment;
    TrashedCommentsFragment trashedCommentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);;
        EdgeToEdge.enable(this);

        Intent intent = getIntent();
        domain = intent.getStringExtra("domain");
        Bundle bundle = new Bundle();
        bundle.putString("domain", domain);

        allCommentsFragment = new AllCommentsFragment();
        allCommentsFragment.setArguments(bundle);

        pendingCommentsFragment = new PendingCommentsFragment();
        pendingCommentsFragment.setArguments(bundle);

        unrepliedCommentsFragment = new UnrepliedCommentsFragment();
        unrepliedCommentsFragment.setArguments(bundle);

        approvedCommentsFragment = new ApprovedCommentsFragment();
        approvedCommentsFragment.setArguments(bundle);

        spamCommentsFragment = new SpamCommentsFragment();
        spamCommentsFragment.setArguments(bundle);

        trashedCommentsFragment = new TrashedCommentsFragment();
        trashedCommentsFragment.setArguments(bundle);

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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(allCommentsFragment, getString(R.string.ALL));
        viewPagerAdapter.addFragment(pendingCommentsFragment, getString(R.string.pending));
        viewPagerAdapter.addFragment(unrepliedCommentsFragment, getString(R.string.unreplied));
        viewPagerAdapter.addFragment(approvedCommentsFragment, getString(R.string.approved));
        viewPagerAdapter.addFragment(spamCommentsFragment, getString(R.string.spam));
        viewPagerAdapter.addFragment(trashedCommentsFragment, getString(R.string.trashed));
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(viewPagerAdapter.getTitle(position));
        }).attach();

        commentDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int position = data.getIntExtra("position", -1);
                            String newStatus = data.getStringExtra("status");

                            Log.i("position cmt activity", "" + position);
                            Log.i("status", newStatus);
                            if (position != -1) {
                                updateCommentAtPosition(position, newStatus);
                            }
                        }
                    }
                }
        );
    }
    private void updateCommentAtPosition(int position, String newStatus) {
        // Update the comment in the corresponding fragment
        if (newStatus.equals("hold")) {
            Log.i("update pending", "udp");
            approvedCommentsFragment.removeCommentAtPosition(position);
            pendingCommentsFragment.addComment(approvedCommentsFragment.getComments().get(position));
        } else if (newStatus.equals("approved")) {
            Log.i("update approved", "uda");
            pendingCommentsFragment.removeCommentAtPosition(position);
            approvedCommentsFragment.addComment(spamCommentsFragment.getComments().get(position));
        }
    }
}