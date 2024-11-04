package vn.edu.usth.wordpressclient.view.comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;

public class CommentDetailActivity extends AppCompatActivity {
    TextView authorName, title, content;
    EditText editText;
    LinearLayout replyCommentField;
    RelativeLayout approve, spam, like, more;
    String domain, status, editedContent, link;
    int id;
    CircleImageView authorAvatar;
    ImageView sendReply;
    CommentViewModel commentViewModel;
    int post;
    ImageView approvedIcon;
    TextView approvedText, spamText;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_detail);

        Toolbar toolbar = findViewById(R.id.comment_detail_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.comments));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        domain = DomainManager.getInstance().getSelectedDomain();
        authorAvatar = findViewById(R.id.author_avatar_comment_detail);
        authorName = findViewById(R.id.author_name_comment_detail);
        title = findViewById(R.id.post_title_comment_detail);
        content = findViewById(R.id.comment_content_in_comment_detail);
        sendReply = findViewById(R.id.upload_reply);
        editText = findViewById(R.id.input_reply);
        approve = findViewById(R.id.mark_approve_comment);
        spam = findViewById(R.id.mark_spam_comment);
        more = findViewById(R.id.more_action_on_comment);
        approvedIcon = findViewById(R.id.done_icon_comment_detail);
        approvedText = findViewById(R.id.done_text_comment_detail);
        spamText = findViewById(R.id.report_text_comment_detail);

        Picasso.get().load(getIntent().getStringExtra("authorAvatar")).error(R.drawable.blank_avatar).into(authorAvatar);
        authorName.setText(getIntent().getStringExtra("authorName"));
        content.setText(Html.fromHtml(getIntent().getStringExtra("content"), Html.FROM_HTML_MODE_LEGACY).toString());

        post = getIntent().getIntExtra("post", -1);
        status = getIntent().getStringExtra("status");
        id = getIntent().getIntExtra("commentId", -1);
        link = getIntent().getStringExtra("link");

        if (status.equals("approved")) {
            approvedIcon.setImageResource(R.drawable.baseline_done_green_24);
            approvedText.setTextColor(Color.parseColor("#26A41A"));
        }

        if (status.equals("spam")) {
            spamText.setText("Not Spam");
        }

        if (status.equals("trash")) {
            approvedIcon.setImageResource(R.drawable.baseline_restore_24);
            approvedText.setText("Restore");
        }

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        commentViewModel.getSuccessLiveData().observe(this, replyContent -> {
            if (!content.equals("")) {
                progressDialog.dismiss();
                editText.setText(Html.fromHtml(replyContent, Html.FROM_HTML_MODE_LEGACY).toString());
                Toast.makeText(this, "Reply comment successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Failed to reply coment", Toast.LENGTH_SHORT).show();
            }
        });

        commentViewModel.getPostOfComment().observe(this, jsonObject -> {
            try {
                title.setText(jsonObject.getString("title"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        commentViewModel.getPostOfComment(post);

        commentViewModel.getStatusLiveData().observe(this, success -> {
            if (success) {
//                progressDialog.dismiss();
                Toast.makeText(this, "Sucessfully change comment status", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to change comment status", Toast.LENGTH_SHORT).show();
            }
        });

        commentViewModel.getDeleteLiveData().observe(this, success -> {
            if (success) {
//                progressDialog.dismiss();
                Toast.makeText(this, "Deleted comment", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete comment", Toast.LENGTH_SHORT).show();
            }
        });

        sendReply.setOnClickListener(v -> {
            replyComment();
        });

        approve.setOnClickListener(v -> {
//            showLoadingDialog();
            if (status.equals("approved")) {
                commentViewModel.updateCommentStatus(id, "hold");
            } else if (status.equals("spam") || status.equals("hold") || status.equals("trash")) {
                commentViewModel.updateCommentStatus(id, "approve");
            }
        });

        spam.setOnClickListener(v -> {
//            showLoadingDialog();
            if (status.equals("spam")) {
                commentViewModel.updateCommentStatus(id, "approve");
            } else if (status.equals("approved") || status.equals("hold") || status.equals("trash")) {
                commentViewModel.updateCommentStatus(id, "spam");
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.equals("trash")) {
                    showPopupMenu(v);
                } else {
                    showTrashPopupMenu(v);
                }
            }
        });
    }

    public void replyComment() {
        showLoadingDialog();
        commentViewModel.replyComment(domain, editText.getText().toString(), id, post);
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.comment_detail_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.move_to_trash) {
//                    showLoadingDialog();
                    commentViewModel.updateCommentStatus(id, "trash");
                    return true;
                } else if (item.getItemId() == R.id.copy_address) {
                    //This feature is under development.
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("link", link);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(CommentDetailActivity.this, "copy address", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return true;
                }
            }
        });
        popupMenu.show();
    }

    private void showTrashPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.trash_comment_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                if (item.getItemId() == R.id.delete_forever) {
//                    showLoadingDialog();
                    commentViewModel.deleteComment(id);
                    return true;
                }else {
                    return true;
                }
            }
        });
        popupMenu.show();
    }

    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }
}