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

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, domain);
        viewPagerAdapter.addFragment(allCommentsFragment, getString(R.string.ALL));
        viewPagerAdapter.addFragment(pendingCommentsFragment, getString(R.string.pending));
        viewPagerAdapter.addFragment(unrepliedCommentsFragment, getString(R.string.unreplied));
        viewPagerAdapter.addFragment(approvedCommentsFragment, getString(R.string.approved));
        viewPagerAdapter.addFragment(spamCommentsFragment, getString(R.string.spam));
        viewPagerAdapter.addFragment(trashedCommentsFragment, getString(R.string.trashed));
        viewPager2.setOffscreenPageLimit(5);
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
                            int position = data.getIntExtra("updatedCommentId", -1);
                            String newStatus = data.getStringExtra("status");
                            String fragment = data.getStringExtra("fragment");
                            Comment repliedComment = data.getParcelableExtra("replyComment", Comment.class);
                            String updateContent = data.getStringExtra("content");

                            Log.i("edited Content in comment activity", updateContent);
                            if (position != -1) {
                                updateCommentAtPosition(position, newStatus, updateContent, fragment);
                            }

                            if (repliedComment != null) {
                                approvedCommentsFragment.addComment(repliedComment);
                                allCommentsFragment.addComment(repliedComment);
                                Optional<Comment> comment = unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == repliedComment.getParent()).findFirst();
                                if (unrepliedCommentsFragment.getComments().stream().filter(comment2 -> comment2.getId() == repliedComment.getParent()).findFirst().isPresent()) {
                                    Comment comment1 = comment.get();
                                    unrepliedCommentsFragment.removeCommentBaseOnId(comment1.getId());
                                }
                            }
                        }
                    }
                }
        );
    }

    private void updateCommentAtPosition(int position, String newStatus, String content, String fragment) {
        if (fragment.equals("AllCommentsFragment")) {
            if (newStatus != null) {
                if (newStatus.equals("hold")) {
                    Comment comment = allCommentsFragment.getComments().get(position);
                    approvedCommentsFragment.removeCommentBaseOnId(comment.getId());
                    if (pendingCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() == 0) {
                        pendingCommentsFragment.addComment(comment);
                    }
                    if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() == 0) {
                        allCommentsFragment.changeStatus(comment, position);
                    }
                } else if (newStatus.equals("approve")) {
                    Comment comment = allCommentsFragment.getComments().get(position);
                    pendingCommentsFragment.removeCommentBaseOnId(comment.getId());
                    if (approvedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() == 0) {
                        approvedCommentsFragment.addComment(comment);
                    }
                    if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        allCommentsFragment.changeStatus(comment, position);
                    }
                } else if (newStatus.equals("spam")) {
                    Comment comment = allCommentsFragment.getComments().get(position);
                    if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        allCommentsFragment.removeCommentBaseOnId(comment.getId());
                    }
                    spamCommentsFragment.addComment(comment);
                    if (pendingCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        pendingCommentsFragment.removeCommentBaseOnId(comment.getId());
                    } else if (approvedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        approvedCommentsFragment.removeCommentBaseOnId(comment.getId());
                    }
                    if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
                    }
                    if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                        Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                        unrepliedCommentsFragment.addComment(unrepliedComment);
                    }
                } else if (newStatus.equals("trash")) {
                    Comment comment = allCommentsFragment.getComments().get(position);
                    if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        allCommentsFragment.removeCommentBaseOnId(comment.getId());
                    }
                    if (pendingCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        pendingCommentsFragment.removeCommentBaseOnId(comment.getId());
                    } else if (approvedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        approvedCommentsFragment.removeCommentBaseOnId(comment.getId());
                    }
                    if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                        unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
                    }
                    if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                        Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                        unrepliedCommentsFragment.addComment(unrepliedComment);
                    }
                    trashedCommentsFragment.addComment(comment);
                }
            }
            if (content != null) {
                Comment comment = allCommentsFragment.getComments().get(position);
                comment.setContent(content);
                allCommentsFragment.changeStatus(comment, position);

                OptionalInt approvedIndex = IntStream.range(0, approvedCommentsFragment.getComments().size()).filter(i -> approvedCommentsFragment.getComments().get(i).getId() == comment.getId()).findFirst();
                if (approvedIndex.isPresent()) {
                    Comment cmt = approvedCommentsFragment.getComments().get(approvedIndex.getAsInt());
                    cmt.setContent(content);
                    approvedCommentsFragment.changeStatus(cmt, approvedIndex.getAsInt());
                }
            }
        } else if (fragment.equals("ApprovedCommentsFragment")) {
            if (newStatus.equals("hold")) {
                Comment comment = approvedCommentsFragment.getComments().get(position);
                approvedCommentsFragment.removeCommentAtPosition(position);
                pendingCommentsFragment.addComment(comment);
                OptionalInt index = IntStream.range(0, allCommentsFragment.getComments().size()).filter(i -> allCommentsFragment.getComments().get(i).getId() == comment.getId()).findFirst();
                if (index.isPresent()) {
                    int positionInAllFragment = index.getAsInt();
                    allCommentsFragment.changeStatus(comment, positionInAllFragment);
                }
            } else if (newStatus.equals("spam")) {
                Comment comment = approvedCommentsFragment.getComments().get(position);
                approvedCommentsFragment.removeCommentAtPosition(position);
                spamCommentsFragment.addComment(comment);

                if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    allCommentsFragment.removeCommentBaseOnId(comment.getId());
                }

                if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
                }
                if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                    Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                    unrepliedCommentsFragment.addComment(unrepliedComment);
                }

//                if (comment.getAuthor() != 255839981) {
//                    if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> (comment1.getParent() == comment.getId() && comment1.getId() == comment.getId())).collect(Collectors.toList()).size() == 0) {
//                        unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
//                    }
//                }
            } else if (newStatus.equals("trash")) {
                Comment comment = approvedCommentsFragment.getComments().get(position);
                approvedCommentsFragment.removeCommentAtPosition(position);
                if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    allCommentsFragment.removeCommentBaseOnId(comment.getId());
                }
                if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
                }

                if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                    Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                    unrepliedCommentsFragment.addComment(unrepliedComment);
                }

//                if (comment.getAuthor() != 255839981) {
//                    if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> (comment1.getParent() == comment.getId() && comment1.getId() == comment.getId())).collect(Collectors.toList()).size() == 0) {
//                        unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
//                    }
//                }
                trashedCommentsFragment.addComment(comment);
            }
        } else if (fragment.equals("PendingCommentsFragment")) {
            if (newStatus.equals("approve")) {
                Comment comment = pendingCommentsFragment.getComments().get(position);
                pendingCommentsFragment.removeCommentAtPosition(position);
                approvedCommentsFragment.addComment(comment);

                OptionalInt index = IntStream.range(0, allCommentsFragment.getComments().size()).filter(i -> allCommentsFragment.getComments().get(i).getId() == comment.getId()).findFirst();
                if (index.isPresent()) {
                    int positionInAllFragment = index.getAsInt();
                    allCommentsFragment.changeStatus(comment, positionInAllFragment);
                }
            } else if (newStatus.equals("spam")) {
                Comment comment = pendingCommentsFragment.getComments().get(position);
                pendingCommentsFragment.removeCommentAtPosition(position);
                spamCommentsFragment.addComment(comment);

                if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    allCommentsFragment.removeCommentBaseOnId(comment.getId());
                }

                if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
                }

                if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                    Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                    unrepliedCommentsFragment.addComment(unrepliedComment);
                }

//                if (comment.getAuthor() != 255839981) {
//                    if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> (comment1.getParent() == comment.getId() && comment1.getId() == comment.getId())).collect(Collectors.toList()).size() == 0) {
//                        unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
//                    }
//                }
            } else if (newStatus.equals("trash")) {
                Comment comment = pendingCommentsFragment.getComments().get(position);
                pendingCommentsFragment.removeCommentAtPosition(position);

                if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    allCommentsFragment.removeCommentBaseOnId(comment.getId());
                }
                if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
                }
                if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                    Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                    unrepliedCommentsFragment.addComment(unrepliedComment);
                }

//                if (comment.getAuthor() != 255839981) {
//                    if (unrepliedCommentsFragment.getComments().stream().filter(comment1 -> (comment1.getParent() == comment.getId() && comment1.getId() == comment.getId())).collect(Collectors.toList()).size() == 0) {
//                        unrepliedCommentsFragment.removeCommentBaseOnId(comment.getId());
//                    }
//                }
                trashedCommentsFragment.addComment(comment);
            }
        } else if (fragment.equals("SpamCommentsFragment")) {
            if (newStatus.equals("approve")) {
                Comment comment = spamCommentsFragment.getComments().get(position);
                spamCommentsFragment.removeCommentAtPosition(position);
                approvedCommentsFragment.addComment(comment);
                allCommentsFragment.addComment(comment);
                if (comment.getAuthor() != 255839981) {
                    if (allCommentsFragment.getComments().stream().filter(comment1 -> (comment1.getParent() == comment.getId() && comment1.getId() != comment.getId())).collect(Collectors.toList()).size() == 0) {
                        unrepliedCommentsFragment.addComment(comment);
                    }
                }
                if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                    Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                    unrepliedCommentsFragment.removeCommentBaseOnId(unrepliedComment.getId());
                }
            } else if (newStatus.equals("trash")) {
                Comment comment = spamCommentsFragment.getComments().get(position);
                spamCommentsFragment.removeCommentAtPosition(position);
                trashedCommentsFragment.addComment(comment);
            }
        } else if (fragment.equals("TrashedCommentsFragment")) {
            if (newStatus.equals("approve")) {
                Comment comment = trashedCommentsFragment.getComments().get(position);
                trashedCommentsFragment.removeCommentAtPosition(position);
                approvedCommentsFragment.addComment(comment);
                allCommentsFragment.addComment(comment);
                if (comment.getAuthor() != 255839981) {
                    if (allCommentsFragment.getComments().stream().filter(comment1 -> (comment1.getParent() == comment.getId() && comment1.getId() != comment.getId())).collect(Collectors.toList()).size() == 0) {
                        unrepliedCommentsFragment.addComment(comment);
                    }
                }
                if (comment.getAuthor() == 255839981 && comment.getParent() != 0 && comment.getParent() != 255839981) {
                    Comment unrepliedComment = allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getParent()).findFirst().get();
                    unrepliedCommentsFragment.removeCommentBaseOnId(unrepliedComment.getId());
                }
            } else if (newStatus.equals("delete")) {
                trashedCommentsFragment.removeCommentAtPosition(position);
            }
        } else if (fragment.equals("UnrepliedCommentsFragment")) {
            if (newStatus.equals("approve")) {
                Comment comment = unrepliedCommentsFragment.getComments().get(position);
                approvedCommentsFragment.addComment(comment);
                pendingCommentsFragment.removeCommentBaseOnId(comment.getId());

                OptionalInt index = IntStream.range(0, allCommentsFragment.getComments().size()).filter(i -> allCommentsFragment.getComments().get(i).getId() == comment.getId()).findFirst();
                if (index.isPresent()) {
                    int positionInAllFragment = index.getAsInt();
                    allCommentsFragment.changeStatus(comment, positionInAllFragment);
                }
            } else if (newStatus.equals("pending")) {
                Comment comment = unrepliedCommentsFragment.getComments().get(position);
                pendingCommentsFragment.addComment(comment);
                approvedCommentsFragment.removeCommentBaseOnId(comment.getId());

                OptionalInt index = IntStream.range(0, allCommentsFragment.getComments().size()).filter(i -> allCommentsFragment.getComments().get(i).getId() == comment.getId()).findFirst();
                if (index.isPresent()) {
                    int positionInAllFragment = index.getAsInt();
                    allCommentsFragment.changeStatus(comment, positionInAllFragment);
                }
            } else if (newStatus.equals("spam")) {
                Comment comment = unrepliedCommentsFragment.getComments().get(position);
                spamCommentsFragment.addComment(comment);
                unrepliedCommentsFragment.removeCommentAtPosition(position);
                if (comment.getStatus().equals("hold")) {
                    pendingCommentsFragment.removeCommentBaseOnId(comment.getId());
                } else if (comment.getStatus().equals("approved")) {
                    approvedCommentsFragment.removeCommentBaseOnId(comment.getId());
                }

                if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    allCommentsFragment.removeCommentBaseOnId(comment.getId());
                }
            } else if (newStatus.equals("trash")) {
                Comment comment = unrepliedCommentsFragment.getComments().get(position);
                unrepliedCommentsFragment.removeCommentAtPosition(position);
                if (comment.getStatus().equals("hold")) {
                    pendingCommentsFragment.removeCommentBaseOnId(comment.getId());
                } else if (comment.getStatus().equals("approved")) {
                    approvedCommentsFragment.removeCommentBaseOnId(comment.getId());
                }

                if (allCommentsFragment.getComments().stream().filter(comment1 -> comment1.getId() == comment.getId()).collect(Collectors.toList()).size() != 0) {
                    allCommentsFragment.removeCommentBaseOnId(comment.getId());
                }
                trashedCommentsFragment.addComment(comment);
            }
        }
    }
}