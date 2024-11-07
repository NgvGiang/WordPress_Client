package vn.edu.usth.wordpressclient.view.comment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;

public class CommentDetailActivity extends AppCompatActivity {
    TextView authorName, title, content,approvedText, spamText, likeText, moreText;
    EditText editText;
    LinearLayout replyCommentField;
    RelativeLayout approve, spam, like, more;
    String domain, status, editedContent, link;
    int id;
    CircleImageView authorAvatar;
    ImageView sendReply,approvedIcon,spamIcon,starIcon;
    CommentViewModel commentViewModel;
    int post;
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
        likeText = findViewById(R.id.like_comment_detail);
        moreText = findViewById(R.id.more_action_on_comment_detail);
        spamIcon = findViewById(R.id.spam_icon);
        like = findViewById(R.id.like_comment);
        starIcon = findViewById(R.id.like_icon_comment_detail);
        Picasso.get().load(getIntent().getStringExtra("authorAvatar")).error(R.drawable.blank_avatar).into(authorAvatar);
        authorName.setText(getIntent().getStringExtra("authorName"));
        content.setText(Html.fromHtml(getIntent().getStringExtra("content"), Html.FROM_HTML_MODE_LEGACY).toString());
        likeText.setText(R.string.like_a_comment);
        moreText.setText(R.string.more_action_on_cmt);

        post = getIntent().getIntExtra("post", -1);
        status = getIntent().getStringExtra("status");
        id = getIntent().getIntExtra("commentId", -1);
        link = getIntent().getStringExtra("link");
        like.setOnClickListener(view -> {
            Object tag = starIcon.getTag();
            if (tag != null && tag.equals("outlined")) {
                starIcon.setImageResource(R.drawable.baseline_star_24);
                starIcon.setTag("filled");
            } else {
                starIcon.setImageResource(R.drawable.baseline_star_outline_24);
                starIcon.setTag("outlined");
            }
        });
        if (status.equals("approved")) {
            approvedIcon.setImageResource(R.drawable.baseline_done_green_24);
            approvedText.setTextColor(Color.parseColor("#26A41A"));
            approvedText.setText(R.string.approve_status);
        }
        if (status.equals("hold")) {
            approvedIcon.setImageResource(R.drawable.baseline_done_24);
            approvedText.setTextColor(ContextCompat.getColor(this, R.color.onBackGround));
            approvedText.setText(R.string.approve_status);
        }

        if (status.equals("spam")) {
            spamText.setText(getString(R.string.not_spam_status));
            spamIcon.setImageResource(R.drawable.baseline_report_red_24);
        }else{
            spamText.setText(getString(R.string.spam_comment_status));
            spamIcon.setImageResource(R.drawable.baseline_report_24);
        }


        if (status.equals("trash")) {
            approvedIcon.setImageResource(R.drawable.baseline_restore_24);
            approvedText.setText(getString(R.string.restore_a_comment));
        }

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        commentViewModel.getSuccessLiveData().observe(this, replyContent -> {
            if (!content.equals("")) {
                progressDialog.dismiss();
                editText.setText(Html.fromHtml(replyContent, Html.FROM_HTML_MODE_LEGACY).toString());
                Snackbar.make(findViewById(android.R.id.content), R.string.reply_success, Snackbar.LENGTH_SHORT).show();
            }else {
                Snackbar.make(findViewById(android.R.id.content), R.string.reply_faild, Snackbar.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(this, getString(R.string.sucessfully_change_comment_status), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.failed_to_change_comment_status), Toast.LENGTH_SHORT).show();
            }
        });

        commentViewModel.getDeleteLiveData().observe(this, success -> {
            if (success) {
                progressDialog.dismiss();
                Toast.makeText(this, getString(R.string.deleted_comment), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, getString(R.string.failed_to_delete_comment), Toast.LENGTH_SHORT).show();
            }
        });

        sendReply.setOnClickListener(v -> {
            replyComment();
        });

        approve.setOnClickListener(v -> {
            showLoadingDialog();
            if (status.equals("approved")) {
                approvedIcon.setImageResource(R.drawable.baseline_done_24);
                approvedText.setTextColor(ContextCompat.getColor(this, R.color.onBackGround));
                commentViewModel.updateCommentStatus(id, "hold");
            } else if (status.equals("hold") || status.equals("trash") || status.equals("spam")) {
                approvedIcon.setImageResource(R.drawable.baseline_done_green_24);
                approvedText.setTextColor(Color.parseColor("#26A41A"));
                commentViewModel.updateCommentStatus(id, "approve");
            }
        });

        spam.setOnClickListener(v -> {
            showLoadingDialog();
            if (status.equals("spam")) {
                commentViewModel.updateCommentStatus(id, "approve");
            } else if (status.equals("approved") || status.equals("hold") || status.equals("trash")) {
                commentViewModel.updateCommentStatus(id, "spam");
            }
        });
//        spam.setOnClickListener(v -> {
//            if (status.equals("spam")) {
//                spamText.setText("Spam");
//                spamIcon.setImageResource(R.drawable.baseline_report_24);
//                status = "approved";
//            } else {
//                spamText.setText("Not Spam");
//                spamIcon.setImageResource(R.drawable.baseline_report_red_24);
//                status = "spam";
//            }
//            commentViewModel.updateCommentStatus(id, status);
//        });

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
    private void showPopupMenu(View anchorView) {

        View popupView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.wpopupmenu_comment_detail, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        int screenHeight = anchorView.getContext().getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorY = location[1];
        int anchorHeight = anchorView.getHeight();

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();

        int yOffset;
        if (anchorY + anchorHeight + popupHeight > screenHeight) {
            yOffset = anchorY - popupHeight;
        } else {
            yOffset = anchorY + anchorHeight;
        }

        popupWindow.showAtLocation(anchorView, 0, location[0], yOffset);

        popupView.findViewById(R.id.move_to_trash).setOnClickListener(view -> {
            // Dismiss popup and execute the "Move to Trash" action
            popupWindow.dismiss();
            // Original code from PopupMenu: finish() and update status to "trash"
            finish();
            commentViewModel.updateCommentStatus(id, "trash");
        });

        popupView.findViewById(R.id.copy_address).setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) anchorView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("link", link);
            clipboard.setPrimaryClip(clip);
            Snackbar.make(anchorView.getRootView(), R.string.copy_address, Snackbar.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }

    private void showTrashPopupMenu(View anchorView) {
        View popupView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.wpopupmenu_comment_trash, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        int screenHeight = anchorView.getContext().getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorY = location[1];
        int anchorHeight = anchorView.getHeight();

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();

        int yOffset;
        if (anchorY + anchorHeight + popupHeight > screenHeight) {
            yOffset = anchorY - popupHeight;
        } else {
            yOffset = anchorY + anchorHeight;
        }

        popupWindow.showAtLocation(anchorView, 0, location[0], yOffset);

        popupView.findViewById(R.id.delete_forever).setOnClickListener(view -> {
            popupWindow.dismiss();

            finish();
            commentViewModel.deleteComment(id);
        });
    }


    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }
}
